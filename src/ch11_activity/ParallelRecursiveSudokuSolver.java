package ch11_activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

import ch11_activity.SudokuBoard.FieldPosition;
import ch11_activity.SudokuBoard.FieldValue;



public class ParallelRecursiveSudokuSolver
{
  @SuppressWarnings("serial")
  private static class SearchTask extends RecursiveTask<List<SudokuBoard>>
  {
    public static AtomicInteger counter = new AtomicInteger(0);
    
    private final SudokuBoard board;
    
    SearchTask(SudokuBoard board)
    {
      this.board = board;
      counter.getAndIncrement();
    }

    @Override
    protected List<SudokuBoard> compute()
    {
      // --- Work phase ---
      if( board.isComplete() )
      {
        return List.of(board);
      }
      
      FieldPosition nextFreeField = board.getFreePositionWithLeastCandidates();
      Set<FieldValue> candidates = board.getValueCandidates(nextFreeField.row, nextFreeField.col);
      
      if( candidates.isEmpty() )
      {
        return Collections.emptyList();
      }
      
      // --- Split phase ---
      List<SearchTask> tasks = new ArrayList<>();
      for( FieldValue field : candidates )
      {
         SudokuBoard newBoard = SudokuBoard.copy(board);
         newBoard.setValue(nextFreeField.row, nextFreeField.col, field );
         newBoard.pack();
         tasks.add( new SearchTask(newBoard) );
      }
      
      invokeAll(tasks);
     
      // --- Combine phase ---
      List<SudokuBoard> resultList = new ArrayList<>();
      tasks.forEach( task -> {
          resultList.addAll( task.join() );
      } );
      
      return resultList;
    }
  }
  

  public static void main(String[] args) throws Exception
  {
    File file = new File("board01.txt");
    SudokuBoard board = SudokuBoard.load(file);
    board.print();
    
    System.out.println("Solve game: ");
    
    long start = System.currentTimeMillis();
    
    // Create and execute the root task
    SearchTask rootTask = new SearchTask(board);
    ForkJoinPool.commonPool().execute(rootTask);
    
    // Wait for the results
    List<SudokuBoard> boards = rootTask.join();
    System.out.println("Found " + boards.size());

    // Get the first result
    SudokuBoard res = boards.get(0);
    System.out.println("Duration " + (System.currentTimeMillis() - start) + " [ms]");
    res.print();
    
    //System.out.println("tasks " + SearchTask.counter.get() );
    
    System.out.println("done with ForkJoin");
  }
}
