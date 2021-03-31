package com.example.team8project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BadWordFilter {

  private static int largestWordLength = 0;


  private static Map<String, String[]> allBadWords = new HashMap<String, String[]>();

  /**
   * Iterates over a String input and checks whether any cuss word was found - and for any/all cuss
   * or possible scammer words found, replace the cuss/scam word with asterisks unless in username,
   * then ask user to pick another username.
   */

  public static String getCensoredText(final String input) {
    loadBannedWords();
    if (input == null) {
      return "";
    }

    String modifiedInput = input;

    // remove leetspeak
    modifiedInput = modifiedInput.replaceAll("1", "i").replaceAll("!", "i").replaceAll("3", "e")
        .replaceAll("4", "a").replaceAll("@", "a").replaceAll("5", "s").replaceAll("7", "t").
            replaceAll("0", "o").replaceAll("9", "g");

    // ignore any character that is not a letter
    modifiedInput = modifiedInput.toLowerCase().replaceAll("[^a-zA-Z]", "");

    ArrayList<String> badWordsFound = new ArrayList<>();

    // iterate over each letter in the word
    for (int start = 0; start < modifiedInput.length(); start++) {
      // from each letter, keep going to find bad words until either the end of
      // the sentence is reached, or the max word length is reached.
      for (int offset = 1;
          offset < (modifiedInput.length() + 1 - start) && offset < largestWordLength; offset++) {

        String wordToCheck = modifiedInput.substring(start, start + offset);

        if (allBadWords.containsKey(wordToCheck)) {

          String[] ignoreCheck = allBadWords.get(wordToCheck);
          boolean ignore = false;

          for (int stringIndex = 0; stringIndex < ignoreCheck.length; stringIndex++) {

            if (modifiedInput.contains(ignoreCheck[stringIndex])) {
              ignore = true;
              break;
            } // end if statement
          } // end of innermost for loop

          if (!ignore) {
            badWordsFound.add(wordToCheck);
          } // end if statement
        } // end if statement
      } // end of inner for loop
    } // end of outside for loop

    String inputToReturn = input;
    for (String swearWord : badWordsFound) {
      char[] charsStars = new char[swearWord.length()];
      Arrays.fill(charsStars, '*');
      final String stars = new String(charsStars);

      // The "(?i)" is to make the replacement case insensitive.
      inputToReturn = inputToReturn.replaceAll("(?i)" + swearWord, stars);
    }

    return inputToReturn;
  } // end getCensoredText


  public boolean isBannedWordUsed(String userName) {

    loadBannedWords();
    boolean bannedWordUsed = false;

    // remove leetspeak
    userName = userName.replaceAll("1", "i").replaceAll("!", "i").replaceAll("3", "e")
        .replaceAll("4", "a").replaceAll("@", "a").replaceAll("5", "s").replaceAll("7", "t").
            replaceAll("0", "o").replaceAll("9", "g");

    // ignore any character that is not a letter
    userName = userName.toLowerCase().replaceAll("[^a-zA-Z]", "");

    if(allBadWords.containsKey(userName)){
      bannedWordUsed = true;
    }
    return bannedWordUsed;
  }


  private static void loadBannedWords() {
//    int readCounter = 0;
    try {
      // The following bad word text file is from https://github.com/areebbeigh/profanityfilter/blob/master/profanityfilter/data/badwords.txt
      // it was modified to include words that could be used to scam users

      FileReader file = new FileReader("DONOTREADbannedWordsList.txt");
      BufferedReader reader = new BufferedReader(file);

      String currentLine;

      while ((currentLine = reader.readLine()) != null) {
        currentLine = currentLine.toLowerCase();
//        readCounter++;

        if (currentLine.length() > largestWordLength) {
          largestWordLength = currentLine.length();
        }

        String[] ignore_in_combination_with_words = new String[]{"a", "a"};
        allBadWords.put(currentLine, ignore_in_combination_with_words);

//        System.out.println("Added word to badwords");
      }

//      while ((currentLine = reader.readLine()) != null) {
//        readCounter++;
//        String[] content = null;
//        try {
//          if (1 == readCounter) {
//            continue;
//          }
//
//          content = currentLine.split(",");
//          if (content.length == 0) {
//            continue;
//          }
//
//          final String word = content[0];
//
//          if (word.startsWith("-----")) {
//            continue;
//          }
//
//          if (word.length() > largestWordLength) {
//            largestWordLength = word.length();
//          }
//
//          String[] ignore_in_combination_with_words = new String[]{};
//          if (content.length > 1) {
//            ignore_in_combination_with_words = content[1].split("_");
//          }
//
//          // Make sure there are no capital letters in the spreadsheet
//          allBadWords.put(word.replaceAll(" ", "").toLowerCase(), ignore_in_combination_with_words);
//        } catch (Exception except) {
//        }
//      } // end while
      file.close();
      reader.close();
    } catch (IOException except) {
    }


  } // end loadBadWords

}