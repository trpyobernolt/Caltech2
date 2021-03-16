package edu.caltech.cs2.project02.interfaces;

import java.util.Set;
import java.util.SortedSet;

public interface IHangmanGuesser {
  public char getGuess(String pattern, Set<Character> guesses);
}
