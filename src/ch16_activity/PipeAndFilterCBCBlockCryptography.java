package ch16_activity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PipeAndFilterCBCBlockCryptography
{
   private static final int CaesarShift = 42;
   private static final int InitVector = 47011;
   
   static class ReaderTask implements Runnable
   {
      private final BlockingQueue<Optional<Integer>> queue;
      private final String filname;

      ReaderTask(String filname, BlockingQueue<Optional<Integer>> queue)
      {
         this.filname = filname;
         this.queue = queue;
      }

      @Override
      public void run()
      {
         try (InputStream input = new FileInputStream(filname))
         {
            // read buffer (reading an int, 4 bytes)
            byte[] block = new byte[4];

            int bytesRead = 0;
            while (bytesRead != -1)
            {
               bytesRead = input.read(block);
               if (bytesRead > 0)
               {
                  int intValue = fromByteArray(block);
                  queue.put(Optional.of(intValue));
               }
               else

               {
                  queue.put(Optional.empty());
               }
            }
         }
         catch (Exception exce)
         {
            exce.printStackTrace();
         }
      }
   }

   static class WriterTask implements Runnable
   {
      private final BlockingQueue<Optional<Integer>> queue;
      private final String filname;

      WriterTask(String filname, BlockingQueue<Optional<Integer>> queue)
      {
         this.filname = filname;
         this.queue = queue;
      }

      @Override
      public void run()
      {
         try (OutputStream output = new FileOutputStream(filname))
         {
            while (true)
            {
               Optional<Integer> optValue = queue.take();
               if (optValue.isEmpty())
               {
                  break;
               }
               else
               {
                  int value = optValue.get();
                  byte[] block = toByteArray(value);
                  output.write(block, 0, block.length);
               }
            }
         }
         catch (Exception exce)
         {
            exce.printStackTrace();
         }
      }
   }

   static class EncryptionTask implements Runnable
   {
      private final BlockingQueue<Optional<Integer>> inQueue;
      private final BlockingQueue<Optional<Integer>> outQueue;

      EncryptionTask(BlockingQueue<Optional<Integer>> inQueue, BlockingQueue<Optional<Integer>> outQueue)
      {
         this.inQueue = inQueue;
         this.outQueue = outQueue;
      }

      @Override
      public void run()
      {
         final int initVector = InitVector;
         int salt = initVector;

         try
         {
            while (true)
            {
               Optional<Integer> optInteger = inQueue.take();
               if (optInteger.isEmpty())
               {
                  outQueue.put(Optional.empty());
                  break;
               }
               else
               {
                  int item = optInteger.get();
                  // XOR operation
                  int value = (salt ^ item);

                  // encrypt value using a simple Caesar cipher (shift 42)
                  value = (value + CaesarShift);
                  salt = value;

                  outQueue.put(Optional.of(value));
               }
            }
         }
         catch (Exception exce)
         {
            exce.printStackTrace();
         }
      }
   }

   static class DecryptionTask implements Runnable
   {
      private final BlockingQueue<Optional<Integer>> inQueue;
      private final BlockingQueue<Optional<Integer>> outQueue;

      DecryptionTask(BlockingQueue<Optional<Integer>> inQueue, BlockingQueue<Optional<Integer>> outQueue)
      {
         this.inQueue = inQueue;
         this.outQueue = outQueue;
      }

      @Override
      public void run()
      {
         final int initVector = InitVector;
         int salt = initVector;

         try
         {
            while (true)
            {
               Optional<Integer> optInteger = inQueue.take();
               if (optInteger.isEmpty())
               {
                  outQueue.put(Optional.empty());
                  break;
               }
               else
               {
                  int item = optInteger.get();
                  // decrypt using a simple Caesar cipher (shift 42)
                  int value = (item - CaesarShift);

                  // XOR operation
                  value = (salt ^ value);
                  salt = item;

                  outQueue.put(Optional.of(value));
               }
            }
         }
         catch (Exception exce)
         {
            exce.printStackTrace();
         }
      }

   }

   public static void main(String[] args) throws Exception
   {
      String inputFileName = "Homer-Odyssey-UTF8-Coding.txt";
      String encryptedFileName = "Homer-Odyssey-UTF8-Coding_encrypted.txt";
      String decryptedFileName = "Homer-Odyssey-UTF8-Coding_decrypted.txt";

      ExecutorService executor = Executors.newFixedThreadPool(3);
      
      System.out.println("Encrypt " + inputFileName);
      System.out.println("to file " + encryptedFileName);

      long startTime = System.currentTimeMillis();
      
      BlockingQueue<Optional<Integer>> intputQueue = new ArrayBlockingQueue<>(100);
      BlockingQueue<Optional<Integer>> outputQueue = new ArrayBlockingQueue<>(100);
      
      ReaderTask readerForEncryption = new ReaderTask(inputFileName, intputQueue);
      EncryptionTask encryption = new EncryptionTask(intputQueue, outputQueue);
      WriterTask writerForEncryption = new WriterTask(encryptedFileName, outputQueue);
      Future<?> readerForEncryptionDone = executor.submit(readerForEncryption);
      Future<?> encryptionDone = executor.submit(encryption);
      Future<?> writerForEncryptionDone = executor.submit(writerForEncryption);
      
      readerForEncryptionDone.get();
      encryptionDone.get();
      writerForEncryptionDone.get();
      long endTime = System.currentTimeMillis();

      System.out.println("Elapsed time for encryption " + (endTime - startTime) + "[ms]");

      System.out.println("Decrypt " + encryptedFileName);
      System.out.println("to file " + decryptedFileName);

      startTime = System.currentTimeMillis();
      ReaderTask readerForDecryption = new ReaderTask(encryptedFileName, intputQueue);
      DecryptionTask decryption = new DecryptionTask(intputQueue, outputQueue);
      WriterTask writerForDecryption = new WriterTask(decryptedFileName, outputQueue);
      Future<?> readerForDecryptionDone = executor.submit(readerForDecryption);
      Future<?> decryptionDone = executor.submit(decryption);
      Future<?> writerForDecryptionDone = executor.submit(writerForDecryption);
      
      readerForDecryptionDone.get();
      decryptionDone.get();
      writerForDecryptionDone.get();
      endTime = System.currentTimeMillis();

      System.out.println("Elapsed time for decryption " + (endTime - startTime) + "[ms]");

      executor.shutdown();
      System.out.println("done");
   }

   static byte[] toByteArray(int value)
   {
      return ByteBuffer.allocate(4).putInt(value).array();
   }

   static int fromByteArray(byte[] bytes)
   {
      return ByteBuffer.wrap(bytes).getInt();
   }
}
