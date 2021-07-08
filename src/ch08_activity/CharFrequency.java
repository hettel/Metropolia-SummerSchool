package ch08_activity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class CharFrequency
{

   public static void main(String[] args) throws IOException
   {
      Path path = Paths.get( "Homer-Odyssey-UTF8-Coding.txt");
      String content = Files.readString(path, Charset.forName("UTF8"));
      
      long count = content.chars().filter( Character::isLetter ).count();
      System.out.println("Number of characters " + count );
      
      // Insert code for a frequency analysis
   }

}
