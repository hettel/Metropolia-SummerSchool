package exam.exercise1.solution;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ExerciseC_UserDefinedCollector
{
   static class VowelCollector implements Collector<Character, int[], Map<Character, Integer>>
   {
      // use internally for counting vowels an int array
      // int[5]  -> a,e,i,o,u

      @Override
      public Supplier<int[]> supplier()
      {
         return () -> new int[5];
      }

      @Override
      public BiConsumer<int[], Character> accumulator()
      {
         return (array,c) -> {
            switch(c)
            {
            case 'a':
               array[0]++;
               break;
            case 'e':
               array[1]++;
               break;
            case 'i':
               array[2]++;
               break;
            case 'o':
               array[3]++;
               break;
            case 'u':
               array[4]++;
               break;
            }
         };
      }

      @Override
      public BinaryOperator<int[]> combiner()
      {
         return (left,right) -> {
            left[0] += right[0];
            left[1] += right[1];
            left[2] += right[2];
            left[3] += right[3];
            left[4] += right[4];
            return left;
         };
      }

      @Override
      public Function<int[], Map<Character, Integer>> finisher()
      {
         return (array) ->  Map.of('a', array[0], 'e', array[1], 'i', array[2], 'o', array[3], 'u', array[4]);
      }

      @Override
      public Set<Characteristics> characteristics()
      {
         return Set.of( Characteristics.UNORDERED );
      }
      
   }
   
   public static void main(String[] args) throws Exception
   {
      Path path = Paths.get( "Homer-Odyssey-UTF8-Coding.txt");
      String content = Files.readString(path, Charset.forName("UTF8"));
     
      Map<Character, Integer> map = content.chars()
                          .map( Character::toLowerCase )
                          .mapToObj( c -> Character.valueOf( (char)c) )
                          .collect( new VowelCollector() );
      
      map.entrySet().stream().forEach( entry -> {
         System.out.println( entry.getKey() + " : " + entry.getValue() );
      });

   }

}
