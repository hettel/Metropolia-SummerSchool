package ch14_activity;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SolutionParallelSearch_Version4
{
   static class Task implements Callable<String>
   {

      @Override
      public String call()
      {
         String str = "";
         while ( Thread.currentThread().isInterrupted() == false )
         {
            str = Util.getRandomString(100);
            int hashValue = str.hashCode();

            if (0 < hashValue && hashValue < 500)
            {
               break;
            }
         }
         
         return str;
      }
      
   }
   
   public static void main(String[] args) throws Exception
   {
      System.out.println("Start searching");
      long startTime = System.currentTimeMillis();

      ExecutorService executor = Executors.newFixedThreadPool( 4 );
      CompletionService<String> completionService = new ExecutorCompletionService<>(executor);
      
      Task task1 = new Task();
      Task task2 = new Task();
      Task task3 = new Task();
      Task task4 = new Task();
 
      completionService.submit( task1 );
      completionService.submit( task2 );
      completionService.submit( task3 );
      completionService.submit( task4 );
      

      System.out.println("Result : " + completionService.take().get().hashCode() );
         
      executor.shutdownNow();
     
      
      long endTime = System.currentTimeMillis();

      System.out.println("Time elapsed     : " + (endTime - startTime) + " [ms]");
   }
}
