package exam.exercise2;

import java.util.concurrent.ThreadLocalRandom;

public class RecursiveCount
{

   public static void main(String[] args)
   {
      int[] array = getRandomIntArray(1_000_000);
      
      int count = search42(array, 0, array.length);
      System.out.println("Count result : " + count );
   }
   
   public static int search42(int[] array, int start, int end)
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
      int leftCount = search42(array,start, mid);
      int rightCount = search42(array,mid, end);
      
      return leftCount + rightCount;
   }

   public static int[] getRandomIntArray(int len)
   {
      return ThreadLocalRandom.current().ints(len, 0, 1_000).toArray();
   }
}
