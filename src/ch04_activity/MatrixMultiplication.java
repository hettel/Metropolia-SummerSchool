package ch04_activity;


public class MatrixMultiplication
{

   public static void main(String[] args)
   {
      final int SIZE = 700;
      
      
      double[][] A = Util.getRandomMatrix(SIZE, SIZE);
      double[][] B = Util.getRandomMatrix(SIZE, SIZE);
      
      long start = System.currentTimeMillis();
      double[][] C = mult(A,B,SIZE);
      long end = System.currentTimeMillis();
      
      System.out.println("Duration : " + (end - start) + "[ms]");
      
      System.out.println("The first element C[0][0] = " + C[0][0] );
      System.out.println("done");
   }
   
   // Multiplication of two square matrices
   // The arguments are not checked for validity
   public static double[][] mult(double[][] A, double[][] B, int SIZE)
   {
      double[][] C = new double[SIZE][SIZE];

      for (int i = 0; i < SIZE; i++)
      {
         for (int j = 0; j < SIZE; j++)
         {
            for (int k = 0; k < SIZE; k++)
            {
               C[i][j] += A[i][k] * B[k][j];
            }
         }
      }

      return C;
   }

}
