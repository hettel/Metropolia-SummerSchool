package ch14_activity;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SolutionParallelSearch_Version2
{
   static class Task implements Callable<String>
   {

      @Override
      public String call()
      {
         String str = "";
         while (true)
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
      
      Task task1 = new Task();
      Task task2 = new Task();
      Task task3 = new Task();
      Task task4 = new Task();
 
      Future<String> future1 = executor.submit( task1 );
      Future<String> future2 = executor.submit( task2 );
      Future<String> future3 = executor.submit( task3 );
      Future<String> future4 = executor.submit( task4 );
      
      executor.shutdown();
      
      System.out.println("Result from task 1 : " + future1.get().hashCode() );
      System.out.println("Result from task 2 : " + future2.get().hashCode() );
      System.out.println("Result from task 3 : " + future3.get().hashCode() );
      System.out.println("Result from task 4 : " + future4.get().hashCode() );
      
      
      long endTime = System.currentTimeMillis();

      System.out.println("Time elapsed     : " + (endTime - startTime) + " [ms]");
   }
}
