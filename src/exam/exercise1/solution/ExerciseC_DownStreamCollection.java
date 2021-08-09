package exam.exercise1.solution;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExerciseC_DownStreamCollection
{
   public static void main(String[] args) throws Exception
   {
      Path path = Paths.get( "Homer-Odyssey-UTF8-Coding.txt");
      String content = Files.readString(path, Charset.forName("UTF8"));
     
      Map<Character, Long> map = content.chars()
                          .map( Character::toLowerCase )
                          .filter( c -> (c == 'a') || (c == 'e' || (c == 'i') || (c == 'o') || (c == 'u')))
                          .mapToObj( c -> Character.valueOf( (char)c) )
                          .collect( Collectors.groupingBy( Function.identity(), Collectors.counting()) );
      
      map.entrySet().stream().forEach( entry -> {
         System.out.println( entry.getKey() + " : " + entry.getValue() );
      });

   }

}
