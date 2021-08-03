package ch04_activity;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Solution
{
   public static void main(String[] args)
   {
      final int SIZE = 700;

      double[][] A = Util.getRandomMatrix(SIZE, SIZE);
      double[][] B = Util.getRandomMatrix(SIZE, SIZE);

      long startTime, endTime;

      startTime = System.currentTimeMillis();
      double[][] C1 = mult(A, B, SIZE);
      endTime = System.currentTimeMillis();
      System.out.println("Elapsed time for standard ijk-multiplication : " + (endTime - startTime) + "[ms]");
      
      startTime = System.currentTimeMillis();
      double[][] C2 = mult_ikj(A, B, SIZE);
      endTime = System.currentTimeMillis();
      System.out.println("Elapsed time for standard ikj-multiplication : " + (endTime - startTime) + "[ms]");
      
      startTime = System.currentTimeMillis();
      double[][] C3 = mult_ikj_parallel(A, B, SIZE);
      endTime = System.currentTimeMillis();
      System.out.println("Elapsed time for parallel ikj-multiplication : " + (endTime - startTime) + "[ms]");

      startTime = System.currentTimeMillis();
      double[][] C4 = mult_ikj_parallel_opt(A, B, SIZE);
      endTime = System.currentTimeMillis();
      System.out.println("Elapsed time for optimized parallel ikj-multiplication : " + (endTime - startTime) + "[ms]");

      //Check results by comparison
      System.out.println("C1 == C2 : " + Arrays.deepEquals(C1, C2));
      System.out.println("C1 == C3 : " + Arrays.deepEquals(C1, C3));
      System.out.println("C1 == C4 : " + Arrays.deepEquals(C1, C4));
      System.out.println("done");
   }

   public static double[][] mult_ikj_parallel_opt(double[][] A, double[][] B, int size)
   {
      double[][] C = new double[size][size];

      IntStream.range(0, size).parallel().forEach(i -> {
         {
            for (int k = 0; k < size; k++)
            {
               double tmp = A[i][k];
               for (int j = 0; j < size; j++)
               {
                  C[i][j] += tmp * B[k][j];
               }
            }
         }
      });

      return C;
   }

   public static double[][] mult_ikj_parallel(double[][] A, double[][] B, int size)
   {
      double[][] C = new double[size][size];

      IntStream.range(0, size).parallel().forEach(i -> {
         {
            for (int k = 0; k < size; k++)
            {
               for (int j = 0; j < size; j++)
               {
                  C[i][j] += A[i][k] * B[k][j];
               }
            }
         }
      });

      return C;
   }

   public static double[][] mult_ikj(double[][] A, double[][] B, int size)
   {
      double[][] C = new double[size][size];

      for (int i = 0; i < size; i++)
      {
         for (int k = 0; k < size; k++)
         {
            for (int j = 0; j < size; j++)
            {

               C[i][j] += A[i][k] * B[k][j];
            }
         }
      }

      return C;
   }

   // Multiplication of two square matrices
   // The arguments are not checked for validity
   public static double[][] mult(double[][] A, double[][] B, int size)
   {
      double[][] C = new double[size][size];

      for (int i = 0; i < size; i++)
      {
         for (int j = 0; j < size; j++)
         {
            for (int k = 0; k < size; k++)
            {
               C[i][j] += A[i][k] * B[k][j];
            }
         }
      }

      return C;
   }
}
