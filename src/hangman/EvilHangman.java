package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class EvilHangman {
    public static void main(String[] args) {
        EvilHangmanGame user_game = new EvilHangmanGame();

        // ARGUMENTS: filename, word length, number of guesses
        File my_file = new File(args[0]);
        int word_length = Integer.parseInt(args[1]);
        int num_guesses = Integer.parseInt(args[2]);

        // STEP 1: try to start the game with the given arguments, will start as long as the files is valid
        try {
            user_game.startGame(my_file, word_length);
            user_game.setNumGuesses(num_guesses);
        } catch(IOException e) {
            System.out.println("IOException");
        } catch(EmptyDictionaryException em) {
            System.out.println("EmptyDictionaryException");
        }

        // DISPLAY game start information
        System.out.printf("java EvilHangman %s %d %d\n", my_file.toString(), user_game.getWordLength(), user_game.getNum_guesses());

        // STEP 2: Play game until user finds the set with one word or runs out of guesses
        while(user_game.getWord().indexOf('-') != -1) {

            // DISPLAY game information
            System.out.printf("You have %d guesses left\n",user_game.getNum_guesses());
            System.out.printf("Used letters: %s\n", user_game.getUsedString());
            System.out.printf("Word: %s\n", user_game.getWord());

            // IF there are zero guesses you lose
            if(user_game.getNum_guesses() == 0) {
                if(user_game.getFinalList().size() != 0) {
                    System.out.printf("Sorry you lost! The word was: %s\n", user_game.getFinalList().get(0));
                    return;
                }
            }

            // TEST USER GUEST
            String guess;
            do {
                System.out.println("Enter Guess: ");
                Scanner in = new Scanner(System.in);
                guess = in.nextLine();
           } while(!checkGuess(guess));

            // TRY MAKING THE GUESS
            try {
                user_game.makeGuess(guess.charAt(0));

            } catch(GuessAlreadyMadeException gm) {
                System.out.println("Guess already made!\n");
                continue;
            }

            // GET and display number of appearances of each char
            if(user_game.getNumShows() == 0) {
                System.out.printf("Sorry, there are no %c\n\n", user_game.getGuess());
                user_game.setNumGuesses(user_game.getNum_guesses()-1);
            }
            else {
                System.out.printf("Yes, there is %d %c\n\n", user_game.getNumShows(), user_game.getGuess());
            }
        }
        System.out.printf("You win! You guessed the word: %s", user_game.getFinalList().get(0));
    }

    // HELPER FUNCTION: if the string is valid then return true, a valid string is a single alpha
    public static boolean checkGuess(String s) {
        boolean is_valid = true;
        if(s.length()>1) { // strings with a length greater than 1
            is_valid = false;
        }
        if(s.isEmpty()) { // empty strings
            is_valid = false;
        }
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if (!Character.isLetter(s.charAt(i))) { // strings with any non-alpha chars
                is_valid = false;
            }
        }
        if(!is_valid) {
            System.out.printf("Invalid Input! ");
        }
        return is_valid;
    }
}
