package exam.exercise1.solution;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExerciseA
{

   public static void main(String[] args) throws Exception
   {
      Path path = Paths.get( "Homer-Odyssey-UTF8-Coding.txt");
      String content = Files.readString(path, Charset.forName("UTF8"));
     
      long count1 = content.chars()
                           .map( Character::toLowerCase )
                           .filter( c -> c == 'a')
                           .count();
      
      long count2 = content.chars()
                           .map( Character::toLowerCase )
                           .filter( c -> c == 'a' )
                           .reduce(0, (i,j) ->  i+1  );

      System.out.println("Number of a " + count1 );
      System.out.println("Number of a " + count2 );
   }

}
