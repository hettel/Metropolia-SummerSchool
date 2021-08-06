package exam.exercise1;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class ExerciseC
{
   public static void main(String[] args) throws Exception
   {
      Path path = Paths.get( "Homer-Odyssey-UTF8-Coding.txt");
      String content = Files.readString(path, Charset.forName("UTF8"));
      
      // content.chars() creates an IntStream. 
      // The int values in the stream correspond to the 
      // character values
   }
}
