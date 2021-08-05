package ch14_activity;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SolutionParallelSearch
{
   static class SearchTask implements Callable<String>
   {
      @Override
      public String call() throws Exception
      {
         // Gets the executing thread
         Thread thread = Thread.currentThread();
         System.out.println("Start searching: " + thread.getName());

         // Check if the work should proceed
         while (thread.isInterrupted() == false)
         {
            String candidate = Util.getRandomString(100);
            int hashValue = candidate.hashCode();

            if (0 < hashValue && hashValue < 500)
            {
               return candidate;
            }
         }

         //System.out.println("Thread aborted " + thread.getName());
         throw new InterruptedException();
      }

   }

   public static void main(String[] args) throws Exception
   {
      System.out.println("Start searching");
      long startTime = System.currentTimeMillis();

      int nThreads = Runtime.getRuntime().availableProcessors();

      ExecutorService executor = Executors.newFixedThreadPool(nThreads);

      CompletionService<String> completionService = new ExecutorCompletionService<>(executor);

      for (int i = 0; i < nThreads; i++)
      {
         completionService.submit(new SearchTask());
      }

      String result = completionService.take().get();
      long endTime = System.currentTimeMillis();
      System.out.println("Elapsed Time " + (endTime - startTime) + " [ms]");
      System.out.println("Found " + result.hashCode());
      
      // Interrupt all worker in the pool
      executor.shutdownNow();
   }
}
