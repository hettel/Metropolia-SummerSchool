package ch06_activity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FindLongestWord
{

   public static void main(String[] args) throws IOException
   {
      Path path = Paths.get( "Homer-Odyssey-UTF8-Coding.txt");
      String content = Files.readString(path, Charset.forName("UTF8"));
      String words[] = content.split("\\s+");
      
      long count = content.chars().filter( Character::isLetter ).count();

      System.out.println("Number of characters " + count );
      System.out.println("Number of words " + words.length );
   }

}
