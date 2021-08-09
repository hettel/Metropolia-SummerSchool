package exam.exercise1.solution;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExerciseB
{
   public static void main(String[] args) throws IOException
   {
      Path path = Paths.get( "Homer-Odyssey-UTF8-Coding.txt");
      String content = Files.readString(path, Charset.forName("UTF8"));
      String words[] = content.split("\\s+");
      System.out.println("Number of words " + words.length );
      
      Map<String, Long> map = Arrays.stream(words)
                                    .map( String::toLowerCase )
                                    .filter( str -> str.length() > 15 )
                                    .filter( str -> str.matches("[a-zA-Z-]*"))
                                    .collect( Collectors.groupingBy( Function.identity(), Collectors.counting() ));
      
      map.entrySet().stream()
                    .forEach( entry -> System.out.println( entry.getKey() + " : " + entry.getValue() ));  

   }
}
