package ch04_activity;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class EffectOfArrayLayoutDemo
{
   public static void main(String[] args)
   {
      final int SIZE = 15_000;
      double[][] array = new double[SIZE][SIZE]; // 225.000.000 slots
      long start, end;

      System.out.println("Sequential initialisation");

      start = System.currentTimeMillis();
      for (int row = 0; row < SIZE; row++)
      {
         for (int col = 0; col < SIZE; col++)
         {
            array[row][col] = ThreadLocalRandom.current().nextDouble();
         }
      }
      end = System.currentTimeMillis();
      System.out.println("Time elapsed (row/col) : " + (end - start));

      start = System.currentTimeMillis();
      for (int col = 0; col < SIZE; col++)
      {
         for (int row = 0; row < SIZE; row++)
         {
            array[row][col] = ThreadLocalRandom.current().nextDouble();
         }
      }
      end = System.currentTimeMillis();
      System.out.println("Time elapsed (col/row) : " + (end - start));

      // =================================================================================

      System.out.println("Parallel initialisation (outer loop parallelisation)");

      start = System.currentTimeMillis();
      IntStream.range(0, SIZE).parallel().forEach(row -> {
         IntStream.range(0, SIZE).forEach(col -> {
            array[row][col] = ThreadLocalRandom.current().nextDouble();
         });
      });
      end = System.currentTimeMillis();
      System.out.println("Time elapsed (row/col) : " + (end - start));

      start = System.currentTimeMillis();
      IntStream.range(0, SIZE).parallel().forEach(col -> {
         IntStream.range(0, SIZE).forEach(row -> {
            array[row][col] = ThreadLocalRandom.current().nextDouble();
         });
      });
      end = System.currentTimeMillis();
      System.out.println("Time elapsed (col/row) : " + (end - start));

      // =================================================================================

      System.out.println("Parallel initialisation (inner loop parallelisation)");

      start = System.currentTimeMillis();
      IntStream.range(0, SIZE).forEach(row -> {
         IntStream.range(0, SIZE).parallel().forEach(col -> {
            array[row][col] = ThreadLocalRandom.current().nextDouble();
         });
      });
      end = System.currentTimeMillis();
      System.out.println("Time elapsed (row/col) : " + (end - start));

      start = System.currentTimeMillis();
      IntStream.range(0, SIZE).forEach(col -> {
         IntStream.range(0, SIZE).parallel().forEach(row -> {
            array[row][col] = ThreadLocalRandom.current().nextDouble();
         });
      });
      end = System.currentTimeMillis();
      System.out.println("Time elapsed (col/row) : " + (end - start));

      // =================================================================================

      System.out.println("Parallel initialisation (outer and inner loop parallelisation)");

      start = System.currentTimeMillis();
      IntStream.range(0, SIZE).parallel().forEach(row -> {
         IntStream.range(0, SIZE).parallel().forEach(col -> {
            array[row][col] = ThreadLocalRandom.current().nextDouble();
         });
      });
      end = System.currentTimeMillis();
      System.out.println("Time elapsed (row/col) : " + (end - start));

      start = System.currentTimeMillis();
      IntStream.range(0, SIZE).parallel().forEach(col -> {
         IntStream.range(0, SIZE).parallel().forEach(row -> {
            array[row][col] = ThreadLocalRandom.current().nextDouble();
         });
      });
      end = System.currentTimeMillis();
      System.out.println("Time elapsed (col/row) : " + (end - start));
   }
}
