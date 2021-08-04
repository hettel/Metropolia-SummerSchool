package ch06_activity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;


public class Solution
{

   public static void main(String[] args) throws IOException
   {
      Path path = Paths.get( "Homer-Odyssey-UTF8-Coding.txt");
      String content = Files.readString(path, Charset.forName("UTF8"));
      String words[] = content.split("\\s+");
      System.out.println("Number of words " + words.length );
      
      long startTime = System.currentTimeMillis();
      Optional<String> longestWord =
      Arrays.stream(words)
            .parallel()
            .map( String::trim )
            //.map( str -> str.replaceAll("[\",!?.';:]", "")) 
            .filter( str -> str.matches("[A-Za-z'-]+") )
            .reduce( (str1,str2) -> str1.length() > str2.length() ? str1 : str2 );
      long endTime = System.currentTimeMillis();   
      System.out.println("Elapsed time : " + (endTime - startTime) + "[ms]");

      longestWord.ifPresent( System.out::println );
    
   }

}
