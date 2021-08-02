package ch02_activity;

import java.math.BigInteger;
import java.util.stream.IntStream;

public class Solution
{

  public static void main(String[] args)
  {
    System.out.println("Start search");
    long startTime = System.currentTimeMillis(); 
    
    long count = IntStream.range(1_000_000, 2_000_000)
                          .parallel()
                          .mapToObj( candidate -> BigInteger.valueOf(candidate) ) // BigInteger::valueOf )
                          .filter( bInt -> bInt.isProbablePrime(1000) )
                          .count();
    
    long endTime = System.currentTimeMillis();
    
    System.out.println("Elapsed Time " + (endTime - startTime) + " [ms]");
    System.out.println("Number of prims " + count);
  }
}
