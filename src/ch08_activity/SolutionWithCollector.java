package ch08_activity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;




public class SolutionWithCollector
{
   static class FrequencyCollector implements Collector<Character, int[], Map<Character, Long>>
   {

      @Override
      public Supplier<int[]> supplier()
      {
         return () -> new int['z' - 'a'+1];
      }

      @Override
      public BiConsumer<int[], Character> accumulator()
      {
         return (array, c) -> {
            char charValue = c.charValue();
            if( charValue >= 'a' && charValue <= 'z') 
            {
               array[charValue-'a']++;
            } 
         };
      }

      @Override
      public BinaryOperator<int[]> combiner()
      {
         return (left,right) -> {
            for(int i=0; i< left.length; i++)
            {
               left[i] += right[i];
            }
            return left;
         };
      }

      @Override
      public Function<int[], Map<Character, Long>> finisher()
      {
         return array -> {
            Map<Character, Long> map = new HashMap<>();
            for(int i = 0; i < array.length; i++)
            {
               map.put( Character.valueOf( (char) (i+'a')), Long.valueOf(array[i]) );
            }
            
            return map;
         };
      }

      @Override
      public Set<Characteristics> characteristics()
      {
         return Set.of(Characteristics.UNORDERED);
      }
   }
   
   
   public static void main(String[] args) throws IOException
   {
      Path path = Paths.get( "Homer-Odyssey-UTF8-Coding.txt");
      String content = Files.readString(path, Charset.forName("UTF8"));
      
      long count = content.chars().filter( Character::isLetter ).count();
      System.out.println("Number of characters " + count );
      
      long startTime = System.currentTimeMillis();
      Map<Character, Long> map = 
      content.chars().parallel()
             .filter( Character::isLetter )
             .map( Character::toLowerCase )
             .filter( Character::isBmpCodePoint )  // to make the cast from int to Character save
             .mapToObj( c -> Character.valueOf( (char) c) )  // map char to Character
             .collect(  new FrequencyCollector() );
      long endTime = System.currentTimeMillis();
      System.out.println("Elapsed time : " + (endTime - startTime) + "[ms]");
      
     for(Entry<Character, Long> entry : map.entrySet() )
     {
        System.out.printf("%s : %5d \n", entry.getKey(), entry.getValue() );
     }
   }

}
