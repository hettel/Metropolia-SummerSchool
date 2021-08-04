package ch08_activity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;



public class Solution
{

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
             .filter( c -> c >= 'a' && c <= 'z')   
             .mapToObj( c -> Character.valueOf( (char) c) ) // map char to Character: Stream<Character>
             .collect( Collectors.groupingBy( Function.identity() , Collectors.counting() ));
      long endTime = System.currentTimeMillis();
      System.out.println("Elapsed time : " + (endTime - startTime) + "[ms]");
      
     for(Entry<Character, Long> entry : map.entrySet() )
     {
        System.out.printf("%s : %5d \n", entry.getKey(), entry.getValue() );
     }
   }

}
