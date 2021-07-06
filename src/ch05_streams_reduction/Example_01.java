package ch05_streams_reduction;

import java.math.BigInteger;
import java.util.stream.IntStream;

public class Example_01
{
   public static void main(String[] args)
   {
      // Build-in reduction
      long count1 = IntStream.range(0, 1_000)
                             .mapToObj( BigInteger::valueOf )
                             .filter( bigInt -> bigInt.isProbablePrime(1000) )
                             .count();

      // User-defined reduction
      long count2 = IntStream.range(0, 1_000)
                             .mapToObj( BigInteger::valueOf )
                             .filter( bigInt -> bigInt.isProbablePrime(1000) )
                             .mapToInt( BigInteger::intValue )
                             .reduce(0, (a, b) -> a + 1);

      System.out.println("Sum1 : " + count1 );
      System.out.println("Sum2 : " + count2 );
   }
}
