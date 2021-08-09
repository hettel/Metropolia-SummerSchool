package exam.exercise2.solution;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadLocalRandom;

public class ForkJoinRecursiveCount
{
   @SuppressWarnings("serial")
   static class CountTask extends RecursiveTask<Integer>
   {
      private final int[] array;
      private final int start;
      private final int end;
      
      CountTask(int[] array, int start, int end)
      {
         this.array = array;
         this.start = start;
         this.end = end;
      }
      
      @Override
      protected Integer compute()
      {
         if( end - start < 100 )
         {
            int count = 0;
            for(int i = start; i < end; i++)
            {
               if(  array[i] == 42 )
                  count++;
            }
            return count;
         }
         
         int mid = (start + end)/2;
         CountTask left = new CountTask(array,start, mid);
         CountTask right = new CountTask(array,mid, end);
         
         invokeAll(left,right);
         
         return left.join() + right.join();
      }
   }
   

   public static void main(String[] args)
   {
      int[] array = getRandomIntArray(1_000_000);
      
      CountTask root = new CountTask(array,0, array.length);
      
      int count = ForkJoinPool.commonPool().invoke(root);
      System.out.println("number of 42 : " + count );
   }
   
   public static int[] getRandomIntArray(int len)
   {
      return ThreadLocalRandom.current().ints(len, 0, 1_000).toArray();
   }

}
