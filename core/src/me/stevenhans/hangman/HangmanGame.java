package me.stevenhans.hangman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

public class HangmanGame {
    private ArrayList<String> wordlist;
    private int life = 5;
    private Set<Character> guessed;
    private String currentWord;
    private StringBuilder currentFill;

    public HangmanGame(ArrayList<String> wordlist) {
        this.wordlist = wordlist;
        guessed = new TreeSet<>();

        restartGame();
    }

    public void restartGame() {
        life = 5;
        guessed.clear();
        int rand = ThreadLocalRandom.current().nextInt(0, this.wordlist.size());
        currentWord = this.wordlist.get(rand);
        currentFill = new StringBuilder(String.join("", Collections.nCopies(currentWord.length(), "_")));
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public String getCurrentFill() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < currentFill.length(); ++i) {
            res.append(currentFill.charAt(i));
            if (i != currentFill.length()-1) {
                res.append(" ");
            }
        }
        return res.toString();
    }

    private void fillGuess(char c) {
        for (int i = 0; i < currentWord.length(); ++i) {
            if (currentWord.charAt(i) == c) {
                currentFill.setCharAt(i, c);
            }
        }
    }

    public int getLife() {
        return life;
    }

    private void decreaseLife() {
        life--;
    }

    public int getNumberOfGuesses() {
        return guessed.size();
    }

    public GuessResult guess(char c) {
        Character guess = c;

        if (guessed.contains(guess)) {
            return GuessResult.ALREADY_GUESSED;
        } else {
            guessed.add(guess);
            if (currentWord.contains(String.valueOf(c))) {
                fillGuess(c);
                return GuessResult.CORRECT;
            } else {
                decreaseLife();
                return GuessResult.INCORRECT;
            }
        }
    }

    public boolean isFinished() {
        return (life == 0 || isWin());
    }

    public boolean isWin() {
        return currentFill.toString().equalsIgnoreCase(currentWord);
    }
}
