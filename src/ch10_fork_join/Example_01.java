package ch10_fork_join;

import java.util.concurrent.ThreadLocalRandom;

public class Example_01
{
   private static final int THRESHOLD = 10;
   
   public static int max(int[] array, int start, int end) {   
     // work phase
     if( end - start < THRESHOLD ) {
        int maxIndex = start;
        for(int i=start+1; i<end; i++) {
          if( array[i] > array[maxIndex] ) {
            maxIndex = i;
          }
        }
        return array[maxIndex];
     }
     else {
       // split phase
       int mid = (start + end)/2;
       int leftValue = max(array, start, mid);
       int rightValue = max(array, mid, end);
       
       // combine phase
       return Math.max(leftValue, rightValue);
     }
   }
   
   public static void main(String[] args)
   {
      int[] array = ThreadLocalRandom.current().ints(1_000).toArray();
      
      int max = max(array, 0, array.length);
      System.out.println("Max value : " + max );
   }
}
