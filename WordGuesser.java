/**
 * Name: Daniel Le
 * Section: CS143 7707
 * <p>
 * <p>
 * Description: The WordGuesser class is a tool for playing a word guessing game. When
 * constructed, it takes in a collection of words, a desired length for the words to
 * be guessed, and a maximum number of guesses allowed. It then creates a set of
 * considered words that fit the desired length and narrows it down as the game progresses.
 * The class has several methods for interacting with the game. The words() method returns
 * the set of words still being considered. The guessesLeft() method returns the number of
 * guesses remaining. The guesses() method returns a set of characters that the user has
 * guessed so far. The pattern() method generates and returns the current pattern for the
 * guessing game. This method uses the guessed characters to narrow down the list of words to
 * a single pattern that represents the current state of the game. It returns this pattern as
 * a string, with unguessed letters represented by dashes. The record(char guess) method takes
 * in a guessed character and returns the number of times that character appears in the current
 * pattern. It narrows down the list of considered words using the guessed character and
 * updates the maximum number of guesses remaining.
 */
package Project3;

import java.util.*;

public class WordGuesser {
    private int length;
    private int max;
    private Set<Character> guesses;
    private Set<String> consideredWords;

    public WordGuesser(Collection<String> words, int length, int max) {
        if (length < 1 || max < 0) {
            throw new IllegalArgumentException();
        }
        this.length = length;
        this.max = max;
        this.guesses = new TreeSet<>();
        this.consideredWords = new TreeSet<>(words);
        Iterator<String> iterator = consideredWords.iterator();
        while (iterator.hasNext()) {
            String word = iterator.next();
            if (word.length() != length) {
                iterator.remove();
            }
        }
    }

    /**
     * This method returns the set of considered words.
     *
     * @return Set<String> - the set of considered words.
     */
    public Set<String> words() {
        return consideredWords;
    }

    /**
     * This method returns the number of guesses left.
     *
     * @return int - the number of guesses left.
     */
    public int guessesLeft() {
        return this.max;
    }

    /**
     * This method returns a set of characters that the user has guessed so far.
     *
     * @return Set<Character> - a set of characters that the user has guessed.
     */
    public Set<Character> guesses() {
        return this.guesses;
    }

    /**
     * This method generates and returns the current pattern for the
     * guessing game.
     * Precondition: The consideredWords collection should not be empty.
     * If it is empty, the method throws an IllegalStateException.
     *
     * @return String - the current pattern for the guessing game.
     * @throws IllegalStateException - if the consideredWords
     *                               collection is empty.
     */
    public String pattern() {
        String chosenPattern = "";
        if (consideredWords.isEmpty()) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < length; i++) {
            if (i != length) {
                chosenPattern += "- ";
            } else {
                chosenPattern += "- ";
            }
        }
        if (guesses.isEmpty()) {
            return chosenPattern;
        }
        chosenPattern = pickPattern();
        return chosenPattern;
    }

    /**
     * This method uses the parameter guess to narrow down the list of words
     * using the guessed characters.
     * Preconditions: The consideredWords collection should not be empty.
     * The number of guessesLeft() should be greater than or equal to 1. The
     * guess character should not be already present in the guesses collection.
     * If any of these preconditions is not satisfied, the program throws an
     * IllegalStateException or an IllegalArgumentException.
     *
     * @param guess - the character guessed by the user
     * @return int - the number of times the guess character
     * occurs in the pattern.
     * throws "IllegalStateException"
     * -if the consideredWords collection is empty or the
     * number of guesses left is less than 1.
     * throws "IllegalArgumentException"
     * -if the guess character is already present in the guesses
     * collection.
     */

    public int record(char guess) {
        if (consideredWords.isEmpty() || guessesLeft() < 1) {
            throw new IllegalStateException();
        }
        if (guesses.contains(guess)) {
            throw new IllegalArgumentException();
        }
        guesses.add(guess);
        Map<String, Set<String>> families = organizeWords();
        String chosenPattern = pickPattern();
        consideredWords = families.get(chosenPattern);
        int count = countOccurrences(chosenPattern, guess);
        if (count == 0) {
            this.max--;
        }
        if (consideredWords.isEmpty()) {
            throw new IllegalStateException();
        }
        return count;
    }

    /**
     * Generates a pattern string for a given word where the
     * guessed letters are revealed and the unguessed letters
     * are represented with hyphens
     *
     * @param word - The word for which the pattern is to
     *             be generated
     * @return The pattern string
     */
    private String getPattern(String word) {
        StringBuilder patternBuilder = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            if (guesses.contains(word.charAt(i))) {
                patternBuilder.append(word.charAt(i));
            } else {
                patternBuilder.append("- ");
            }
        }
        return patternBuilder.toString();
    }

    /**
     * Counts the occurrences of a given character in a string
     *
     * @param str - The string to be searched
     * @param c   - The character to count occurrences of
     * @return The count of occurrences of the character in the string
     */
    private int countOccurrences(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    /**
     * Organizes the words in the consideredWords collection into
     * families of words with the same pattern. A TreeMap is used
     * to ensure that the patterns are sorted.
     *
     * @return A Map containing the families of words
     */
    private Map<String, Set<String>> organizeWords() {
        Map<String, Set<String>> families = new TreeMap<>();
        for (String word : consideredWords) {
            String pattern = getPattern(word);
            if (!families.containsKey(pattern)) {
                families.put(pattern, new TreeSet<>());
            }
            families.get(pattern).add(word);
        }
        return families;
    }

    /**
     * Picks the pattern with the most words in the corresponding
     * family of words and the smallest pattern in case of a tie
     *
     * @return The chosen pattern string
     */
    private String pickPattern() {
        Map<String, Set<String>> families = organizeWords();
        int mostWords = 0;
        String chosenPattern = "";
        for (String pattern : families.keySet()) {
            int size = families.get(pattern).size();
            if (size > mostWords) {
                mostWords = size;
                chosenPattern = pattern;
            } else if (size == mostWords &&
                    pattern.compareTo(chosenPattern) < 0) {
                chosenPattern = pattern;
            }
        }
        return chosenPattern;
    }
}
