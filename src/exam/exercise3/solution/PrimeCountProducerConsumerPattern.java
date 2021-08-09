package exam.exercise3.solution;

import java.math.BigInteger;
import java.util.OptionalInt;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PrimeCountProducerConsumerPattern
{
   static class Producer implements Runnable
   {
      private final BlockingQueue<OptionalInt> outQueue;

      Producer(BlockingQueue<OptionalInt> outQueue)
      {
         this.outQueue = outQueue;
      }

      public void run()
      {
         try
         {
            for (int i = 1_000_000; i < 2_000_000; i++)
            {
               outQueue.put(OptionalInt.of(i));
            }
            outQueue.put(OptionalInt.empty());
         }
         catch (InterruptedException exce)
         {
            exce.printStackTrace();
         }
      }
   }

   static class Filter implements Runnable
   {
      private final BlockingQueue<OptionalInt> inQueue;
      private final BlockingQueue<OptionalInt> outQueue;

      Filter(BlockingQueue<OptionalInt> inQueue, BlockingQueue<OptionalInt> outQueue)
      {
         this.inQueue = inQueue;
         this.outQueue = outQueue;
      }

      public void run()
      {
         try
         {
            while(true)
            {
               OptionalInt value = inQueue.take();
               if( value.isEmpty() )
               {
                  outQueue.put(OptionalInt.empty());
                  break;
               }
               else
               {
                  if( BigInteger.valueOf( value.getAsInt() ).isProbablePrime(1000) )
                  {
                     outQueue.put(value);
                  }
               }
            }
         }
         catch (InterruptedException exce)
         {
            exce.printStackTrace();
         }
      }
   }

   static class Consumer implements Runnable
   {
      private final BlockingQueue<OptionalInt> inQueue;

      Consumer(BlockingQueue<OptionalInt> inQueue)
      {
         this.inQueue = inQueue;
      }

      public void run()
      {
         try
         {
            int counter = 0;
            while(true)
            {
               OptionalInt value = inQueue.take();
               if( value.isEmpty() )
                  break;
               else
                  counter++;
            }
            System.out.println( "Count " + counter );
         }
         catch (InterruptedException exce)
         {
            exce.printStackTrace();
         }
      }
   }

   public static void main(String[] args)
   {
      BlockingQueue<OptionalInt> queue1 = new ArrayBlockingQueue<>(100);
      BlockingQueue<OptionalInt> queue2 = new ArrayBlockingQueue<>(100);
      
      Producer producer = new Producer(queue1);
      Filter filter = new Filter(queue1, queue2);
      Consumer consumer = new Consumer(queue2);
      
      new Thread( producer ).start();
      new Thread( filter ).start();
      new Thread( consumer ).start();

   }

}
