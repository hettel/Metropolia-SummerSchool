package exam.exercise1;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class ExerciseB
{
   public static void main(String[] args) throws Exception
   {
      Path path = Paths.get( "Homer-Odyssey-UTF8-Coding.txt");
      String content = Files.readString(path, Charset.forName("UTF8"));
      String words[] = content.split("\\s+");
      
      // Arrays.stream(words) creates an stream of strings. 
      // To consider only word that contains normal characters
      // you can apply  word.matches("[a-zA-Z-]*") as a filter
   }
}
