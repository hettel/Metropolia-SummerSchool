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
      Path path = Paths.get("Homer-Odyssey-UTF8-Coding.txt");
      String content = Files.readString(path, Charset.forName("UTF8"));
      String words[] = content.split("\\s+");

      String longestWord = words[0];
      for (int i = 1; i < words.length; i++)
      {
         if (words[i].matches("[a-zA-Z]*"))
         {
            if (words[i].length() > longestWord.length())
            {
               longestWord = words[i];
            }
         }
      }

      System.out.println("Longest word " + longestWord);
      System.out.println("Longest word length " + longestWord.length());
      System.out.println("Number of words " + words.length);
   }

}
