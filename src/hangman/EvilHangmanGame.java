package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
    TreeSet<Character> guessed_letters = new TreeSet<>();
    Set<String>word_dictionary = new HashSet<>();
    int num_guesses = 0;
    int my_word_length = 0;
    String my_word;
    char my_guess;

    public EvilHangmanGame() {} //Constructor

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        Scanner my_scanner = new Scanner(dictionary);

        // SET word length and initial "------" string
        setWordLength(wordLength);
        StringBuilder temp_builder = new StringBuilder();
        temp_builder.append("-".repeat(getWordLength()));
        setWordInitial(temp_builder.toString());

        // Input dictionary from file name
        while(my_scanner.hasNext()) {
            String temp = my_scanner.next();
            if(temp.length() == getWordLength()) {
                word_dictionary.add(temp);
            }
        }

        // IF dictionary is empty throw an error
        if(getDictionary().size() == 0) {
            throw new EmptyDictionaryException();
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        setGuess(Character.toLowerCase(guess));

        if(getGuessedLetters().contains(getGuess())) {
            throw new GuessAlreadyMadeException();
        }

        addGuessedLetter(getGuess());
        word_dictionary = getNextSet();
        return getDictionary();
    }

    public Set<String> getNextSet() { // return the longest set of possible words
        HashMap<String, Set<String>> my_map = new HashMap<>();
        for (String s : getDictionary()) {
            if(s.length() != getWordLength()) {
                continue;
            }
            String curr_key = getKey(s);
            if (my_map.containsKey(curr_key)) {
                my_map.get(curr_key).add(s);
            }
            else { // key does not exist yet
                HashSet<String> add_set = new HashSet<>();
                add_set.add(s);
                my_map.put(curr_key, add_set);
            }
        }
        return getNextSetHelper(my_map);
    }

    public Set<String> getNextSetHelper(Map<String, Set<String>>word_map) { //returns the set that we will be using
        if(word_map.isEmpty()) return null;
        var entry_set = word_map.entrySet();
        int highest_num = 0; //highest number of items so far
        String send_key = "";

        for(var entry : entry_set) {
            if(entry.getValue().size() > highest_num) { //if the size of the current set of the map is bigger, then we want that one more
                send_key = entry.getKey();
                highest_num = entry.getValue().size();
            }
            else if(entry.getValue().size() == highest_num) {
                // TB#1 - get the one that does not contain the letter
                if(entry.getKey().indexOf(getGuess()) == -1) {
                    send_key = entry.getKey();
                }
                else if(send_key.indexOf(getGuess()) == -1) {
                    // DON'T DO ANYTHING
                }
                // TB#2 - get the one with fewer letters
                else if(getNumChars(entry.getKey(), getGuess()) < getNumChars(send_key,getGuess())) {
                    //System.out.println("I like the smaller one better");
                    send_key = entry.getKey();
                }
                else if(getNumChars(entry.getKey(), getGuess()) > getNumChars(send_key,getGuess())) {
                    // DON'T DO ANYTHING
                }
                // TB#3 - get rightmost
                else {
                    send_key = getRightMost(send_key, entry.getKey());
                }
            }
        }
        setWord(send_key);
        return word_map.get(send_key);
    }

    // HELPER FUNCTION THAT RETURNS NUMBER OF A SPECIFIC CHAR C IN A STRING S
    public int getNumChars(String s, char c) {
        int count = 0;
        for(int i=0; i<s.length(); i++) {
            if(s.charAt(i) == c) count++;
        }
        return count;
    }

    public String getRightMost(String s1, String s2) {
        for(int i =0; i< s1.length(); i++) {
            if(s1.charAt(i) != s2.charAt(i)) {
                if(s1.charAt(i) == getGuess()) {
                    return s2;
                }
                else return s1;
            }
        }
        return null;
    }

    // RETURNS key given a string
    public String getKey(String curr_word) {
        StringBuilder temp_builder = new StringBuilder();
        for(int i=0; i<curr_word.length(); i++) {
            if(curr_word.charAt(i) == getGuess()) {
                temp_builder.append(getGuess());
            }
            else {
                temp_builder.append('-');
            }
        }
        return temp_builder.toString();
    }


    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessed_letters;
    }

    public void addGuessedLetter(char c) {
        //System.out.printf("We will add %c\n", c);
        guessed_letters.add(c);
    }

    public void setNumGuesses(int x) {
        num_guesses = x;
    } public int getNum_guesses() {
        return num_guesses;
    }

    public void setGuess(char guess) {
        my_guess = guess;
    } public char getGuess () {
        return my_guess;
    }

    public void setWordLength(int i) {
        my_word_length = i;
    } public int getWordLength () {
        return my_word_length;
    }


    public void setWordInitial(String new_word) {
        my_word = new_word;

    } public void setWord(String new_word) {
        StringBuilder temp_builder = new StringBuilder();
        for(int i=0; i<new_word.length(); i++) {
            if (getWord().charAt(i) == '-') {
                temp_builder.append(new_word.charAt(i));
            } else {
                temp_builder.append(getWord().charAt(i));
            }
        }
        setWordInitial(temp_builder.toString());

    } public String getWord () {
        return my_word;
    }

    public Set<String> getDictionary() {
        return word_dictionary;
    }

    public List getFinalList() {
        List<String> arr = new ArrayList<>(getDictionary());
        for(String s : getDictionary()) {
            arr.add(s);
        }
        return arr;
    }

    public String getUsedString() {
        return getGuessedLetters().toString();
    }

    int getNumShows() {
        int num_shows = 0;
        for(int i =0; i<getWordLength(); i++) {
            if(getWord().charAt(i) == getGuess()) {
                num_shows++;
            }
        }
        return num_shows;
    }
}
