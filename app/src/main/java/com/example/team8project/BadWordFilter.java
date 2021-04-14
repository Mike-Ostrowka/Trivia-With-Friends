package com.example.team8project;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

public class BadWordFilter {

  //  private static Map<String, String> allBadWords = new HashMap<String, String>();
  private static final ArrayList<String> allBadWords = new ArrayList<>();
  private static int largestWordLength = 0;

  /**
   * Iterates over a String input and checks whether any cuss or possible scammer words found,
   * replaces the cuss/scam word with asterisks unless in username, then ask user to pick another
   * username.
   */

  public static String getCensoredText(final String input, Context context, String toast) {
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

        if (allBadWords.contains(wordToCheck)) {
          boolean ignore = false;

          if (!ignore) {
            badWordsFound.add(wordToCheck);
          }
        }
      }
    }
    if (badWordsFound.size() > 0) {
      Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
    }

    String inputToReturn = input;
    for (String swearWord : badWordsFound) {
      char[] charsStars = new char[swearWord.length()];
      Arrays.fill(charsStars, '*');
      final String stars = new String(charsStars);

      // The "(?i)" is to make the replacement case insensitive.
      inputToReturn = inputToReturn.replaceAll("(?i)" + swearWord, stars);
    }

    return inputToReturn;
  }

  private static void loadBannedWords() {
    String currentLine = "";
    try {
      // The following bad word text file is from https://github.com/areebbeigh/profanityfilter/blob/master/profanityfilter/data/badwords.txt
      // it was modified to include words that could be used to scam users
      InputStream file = BadWordFilter.class.getClassLoader()
          .getResourceAsStream("DONOTREADbannedWordsList.txt");
      BufferedReader reader = new BufferedReader(
          new InputStreamReader(file, Charset.defaultCharset()));

      while ((currentLine = reader.readLine()) != null) {
        currentLine = currentLine.toLowerCase();

        if (currentLine.length() > largestWordLength) {
          largestWordLength = currentLine.length();
        }

        allBadWords.add(currentLine);
      }
      file.close();
      reader.close();

    } catch (IOException e) {
      Log.wtf("BadWordFilter", "Error reading file on line " + currentLine, e);
      e.printStackTrace();
    }
  }

  public boolean isBannedWordUsed(String userName) {
    loadBannedWords();
    boolean bannedWordUsed = false;

    // remove leetspeak
    userName = userName.replaceAll("1", "i").replaceAll("!", "i").replaceAll("3", "e")
        .replaceAll("4", "a").replaceAll("@", "a").replaceAll("5", "s").replaceAll("7", "t").
            replaceAll("0", "o").replaceAll("9", "g");

    // ignore any character that is not a letter
    userName = userName.toLowerCase().replaceAll("[^a-zA-Z]", "");

    if (allBadWords.contains(userName)) {
      bannedWordUsed = true;
    }
    return bannedWordUsed;
  }
}