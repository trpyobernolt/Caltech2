package edu.caltech.cs2.project02.choosers;

import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class EvilHangmanChooser implements IHangmanChooser {
  private int guesses;
  private int maxGuesses;
  private SortedSet<Character> guessedChars;
  private static final Random randomGenerator = new Random();
  private SortedSet<String> availWords;

  public EvilHangmanChooser(int wordLength, int maxGuesses) {
    guessedChars = new TreeSet<>();
    if (wordLength < 1 || maxGuesses < 1) {
      throw new IllegalArgumentException();
    }
    this.maxGuesses = maxGuesses;
    Scanner scan = null;
    try {
      scan = new Scanner(new File("data/scrabble.txt"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    List<String> wordLst = new ArrayList<>();
    SortedSet<String> availWords = new TreeSet<>();
    while (scan.hasNextLine()) {
      String line = scan.nextLine();
      wordLst.add(line);
    }
    for (int i = 0; i < wordLst.size(); i++) {
      if (wordLst.get(i).length() == wordLength) {
        availWords.add(wordLst.get(i));
      }
    }
    if (availWords.isEmpty()) {
      throw new IllegalStateException();
    }
    this.availWords = availWords;
  }

  @Override
  public int makeGuess(char letter) {
    if (this.maxGuesses - this.guesses < 1){
      throw new IllegalStateException();
    }
    if(this.guessedChars.contains(letter)){
      throw new IllegalArgumentException();
    }
    if (!Character.isLowerCase(letter)){
      throw new IllegalArgumentException();
    }
    this.guessedChars.add(letter);
    Map<String, SortedSet<String>> patternMap = new TreeMap<>();
    for (String word : availWords){
      String pattern = "";
      for (int i = 0; i < word.length(); i++){
        if (guessedChars.contains(word.charAt(i))){
          pattern += word.charAt(i);
        }
        else {
          pattern += '-';
        }
      }
      if (patternMap.containsKey(pattern)){
        patternMap.get(pattern).add(word);
      }
      else {
        SortedSet newPattern = new TreeSet();
        newPattern.add(word);
        patternMap.put(pattern, newPattern);
      }
    }
    int best = -1;
    for (String key : patternMap.keySet()){
      if (patternMap.get(key).size() > best){
        best = patternMap.get(key).size();
        this.availWords = patternMap.get(key);
      }
    }
    int counter = 0;
    for (int i = 0; i < this.availWords.first().length(); i++){
      if (this.availWords.first().charAt(i) == letter){
        counter += 1;
      }
    }
    if (counter ==0) {
      this.guesses += 1;
    }
    return counter;
  }

  @Override
  public boolean isGameOver() {
    boolean charsGuessed = true;
    if (this.guesses == this.maxGuesses){
      return true;
    }
    for (int i = 0; i < this.availWords.first().length(); i++) {
      if (!this.guessedChars.contains(this.availWords.first().charAt(i))) {
        charsGuessed = false;
      }
    }
    if (charsGuessed){
      return true;
    }
    return false;
  }

  @Override
  public String getPattern() {
    String display = new String();
    for (int i = 0; i < this.availWords.first().length(); i++){
      if (this.guessedChars.contains(this.availWords.first().charAt(i))){
        display += this.availWords.first().charAt(i);
      }
      else {
        display += '-';
      }
    }
    return display;
  }

  @Override
  public SortedSet<Character> getGuesses() {
    return this.guessedChars;
  }

  @Override
  public int getGuessesRemaining() {
    return (this.maxGuesses - this.guesses);
  }

  @Override
  public String getWord() {
    this.guesses = this.maxGuesses;
    return this.availWords.first();
  }
}