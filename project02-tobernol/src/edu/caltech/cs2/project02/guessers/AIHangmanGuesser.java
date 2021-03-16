package edu.caltech.cs2.project02.guessers;

import edu.caltech.cs2.project02.interfaces.IHangmanGuesser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class AIHangmanGuesser implements IHangmanGuesser {
  private static String wordString;

  public AIHangmanGuesser(){
    Scanner scan = null;
    try {
      scan = new Scanner(new File("data/scrabble.txt"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    while (scan.hasNextLine()) {
      String line = scan.nextLine();
      this.wordString += ' ' + line;
    }
  }

  @Override
  public char getGuess(String pattern, Set<Character> guesses){
    String[] wordLst = this.wordString.split(" ");
    List<String> possibleWords = new ArrayList<>();
    for (int z = 0; z < wordLst.length; z++){
      if (wordLst[z].length() == pattern.length()){
        possibleWords.add(wordLst[z]);
        for (int i = 0; i < wordLst[z].length(); i++) {
          if (guesses.contains(wordLst[z].charAt(i))) {
            possibleWords.remove(wordLst[z]);
          }
        }
      }
    }
    for (int i = 0; i < possibleWords.size(); i++){
      String word = possibleWords.get(i);
      for (int z = 0; z < pattern.length(); z++){
        if (pattern.charAt(z) == '-'){
        }
        else if (pattern.charAt(z) != word.charAt(z)){
          possibleWords.remove(word);
        }
      }
    }
    Map<Character, Integer> charMap = new HashMap<>();
    for (char alphabet = 'a'; alphabet < 'z'; alphabet++){
      if (!guesses.contains(alphabet)){
        charMap.put(alphabet, 0);
      }
    }
    for (int i = 0; i < possibleWords.size(); i++){
      String word = possibleWords.get(i);
      for (int z = 0; z < word.length(); z++) {
        for (char c : charMap.keySet()){
          if (word.charAt(z) == c){
            charMap.replace(c, (charMap.get(c) + 1));
          }
        }
      }
    }
    int bestCharInt = -1;
    char bestChar = '-';
    for (char c : charMap.keySet()){
      if (charMap.get(c) > bestCharInt){
        bestChar = c;
        bestCharInt = charMap.get(c);
      }
    }
    return bestChar;
  }
}
