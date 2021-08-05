package ch14_activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SolutionParallelSearch_Version1
{
   static class Task implements Runnable
   {

      @Override
      public void run()
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
         
         System.out.println(Thread.currentThread().getName() + " found hash value : " + str.hashCode() );
      }
      
   }
   
   public static void main(String[] args)
   {
      System.out.println("Start searching");
      long startTime = System.currentTimeMillis();

      ExecutorService executor = Executors.newFixedThreadPool( 4 );
      
      Task task1 = new Task();
      Task task2 = new Task();
      Task task3 = new Task();
      Task task4 = new Task();
 
      executor.execute( task1 );
      executor.execute( task2 );
      executor.execute( task3 );
      executor.execute( task4 );
      
      executor.shutdown();
      
      long endTime = System.currentTimeMillis();

      System.out.println("Time elapsed     : " + (endTime - startTime) + " [ms]");
   }
}
