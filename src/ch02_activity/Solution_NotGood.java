package ch02_activity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Solution_NotGood
{
   static class Task implements Runnable
   {
      private final int start;
      private final int end;

      // candidate for a race condition
      // no coordinated read/write access
      private int count = 0;

      public Task(int start, int end)
      {
         this.start = start;
         this.end = end;
      }

      @Override
      public void run()
      {
         for (int candidate = this.start; candidate < this.end; candidate++)
         {
            BigInteger bInt = BigInteger.valueOf(candidate);
            if (bInt.isProbablePrime(1000))
            {
               count++;
            }
         }
      }

      public int getCount()
      {
         return count;
      }
   }

   public static void main(String[] args) throws InterruptedException
   {
      System.out.println("Start search");
      long startTime = System.currentTimeMillis();

      // Get number of available cores
      // Cores with hyper threading are counted twice
      final int numOfCores = Runtime.getRuntime().availableProcessors();

      // Use all cores
      final int numChunks = numOfCores;

      final int start = 1_000_000;
      final int end = 2_000_000;

      // Create tasks
      List<Thread> threads = new ArrayList<>();
      List<Task> tasks = new ArrayList<>();
      final int delta = (end - start) / numChunks;
      for (int i = 0; i < numChunks; i++)
      {
         // Note: the end value of the last task must be adjusted
         int startIndex = start + i * delta;
         int endIndex = i == numChunks - 1 ? end : start + (i + 1) * delta;  
         Task task = new Task(startIndex, endIndex);
         tasks.add(task);
         threads.add(new Thread(task));
      }

      // Start tasks
      threads.forEach(th -> th.start());

      // Wait until all tasks are finished
      for (Thread th : threads)
      {
         th.join();
      }

      int count = 0;
      for (Task task : tasks)
      {
         count += task.getCount();
      }
      
      long endTime = System.currentTimeMillis();
      
      System.out.println("Elapsed Time " + (endTime - startTime) + " [ms]");
      System.out.println("Number of prims " + count);
   }
}
