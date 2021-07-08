package ch07_streams_collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Example_05
{

   public static void main(String[] args)
   {
      // Create a list with random strings
      List<String> strings = new ArrayList<>();
      for(int i=0; i < 10_000; i++)
      {
        strings.add( getRandomString(10) );
      }
      
      
      // Using a downstream collector to count the strings
      Map<String, Long> stringMapCount = strings.stream().parallel()
                    .collect( Collectors.groupingBy( str -> str.substring(0, 1), 
                              Collectors.counting() ) );

      for( Entry<String, Long> entry : stringMapCount.entrySet() )
      {
         System.out.println( entry.getKey() + " -> " + entry.getValue() );
      }
   }
   
   private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();

   public static String getRandomString(int len)
   {
     StringBuilder strBuilder = new StringBuilder();
     for (int i = 0; i < len; i++)
     {
       int idx = ThreadLocalRandom.current().nextInt(ALPHABET.length);
       strBuilder.append(ALPHABET[idx]);
     }

     return strBuilder.toString();
   }

}
