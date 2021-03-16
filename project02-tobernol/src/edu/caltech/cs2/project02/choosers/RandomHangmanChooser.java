package edu.caltech.cs2.project02.choosers;

import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class RandomHangmanChooser implements IHangmanChooser {
  private final String secretWord;
  private int guesses;
  private int maxGuesses;
  private SortedSet<Character> guessedChars;
  private static final Random randomGenerator = new Random();

  public RandomHangmanChooser(int wordLength, int maxGuesses) {
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
    int randomInteger = randomGenerator.nextInt(availWords.size());
    Iterator wordIterator = availWords.iterator();
    String chosenWord = "";
    for (int i = 0; i < randomInteger+1; i++) {
      chosenWord = wordIterator.next().toString();
    }
    this.secretWord = chosenWord;
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
    int counter = 0;
    for (int i = 0; i < this.secretWord.length(); i++){
      if (this.secretWord.charAt(i) == letter){
        counter += 1;
      }
    }
    if (counter == 0) {
      this.guesses += 1;
    }
    this.guessedChars.add(letter);
    return counter;
  }

  @Override
  public boolean isGameOver() {
    boolean charsGuessed = true;
    if (this.guesses == this.maxGuesses){
      return true;
    }
    for (int i = 0; i < this.secretWord.length(); i++) {
      if (!this.guessedChars.contains(this.secretWord.charAt(i))) {
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
    for (int i = 0; i < this.secretWord.length(); i++){
      if (this.guessedChars.contains(this.secretWord.charAt(i))){
        display += this.secretWord.charAt(i);
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
    return this.secretWord;
  }
}