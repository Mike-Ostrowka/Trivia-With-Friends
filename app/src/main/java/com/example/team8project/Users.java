package com.example.team8project;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class Users extends RealmObject {

  private final RealmList<Users> friends = new RealmList<>();
  //  private RealmList<Integer> eloTrackerList = new RealmList<>(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000);
  protected Integer eloOne = 1000;
  protected int eloTwo = 1000;
  protected int eloThree = 1000;
  protected int eloFour = 1000;
  protected int eloFive = 1000;
  protected int eloSix = 1000;
  protected int eloSeven = 1000;
  protected int eloEight = 1000;
  protected int eloNine = 1000;
  protected int eloTen = 1000;
  private byte[] profilePictureByteArray;
  @PrimaryKey
  private String _id; //username
  private int elo = 1000; //default value is 1000
  @Required
  private String password;
  private String bio;
  private String securityAnswer;
  private int gamesPlayed = 0;
  private int gamesWon = 0;
  private String channelKey;

  public Users(String name) {
    _id = name;
    password = "";
  }

  public Users(String name, String pswrd) {
    _id = name;
    try {
      password = generateEncryption(pswrd);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
    }
  }

  public Users() {
  }


  // This code was found and adapted from:
  // L. Gupta, M. Rajavel, and M. Taylor, “Java Secure Hashing - MD5, SHA256, SHA512, PBKDF2, BCrypt, SCrypt,”
  //  HowToDoInJava, 25-Dec-2020. [Online]. Available:
  //  https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/#PBKDF2WithHmacSHA1.
  //  [Accessed: 10-Mar-2021].

  private static boolean validatePassword(String originalPassword, String storedPassword)
      throws NoSuchAlgorithmException, InvalidKeySpecException {

    String[] breakApart = storedPassword.split(":");
    int iterations = Integer.parseInt(breakApart[0]);
    byte[] salt = fromHex(breakApart[1]); // could just bring it back in bytes
    byte[] hash = fromHex(breakApart[2]); // could just bring it back in bytes

    PBEKeySpec cryptoSpec = new PBEKeySpec(originalPassword.toCharArray(),
        salt, iterations, hash.length * 8);
    SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    byte[] checkHash = key.generateSecret(cryptoSpec).getEncoded();

    // bitwise XOR checking
    int difference = hash.length ^ checkHash.length;

    // checks if the bits in the two hashes are the same (checking if the passwords match)
    for (int i = 0; i < hash.length && i < checkHash.length; ++i) {
      difference |= hash[i] ^ checkHash[i];
    }

    return difference == 0;
  }

  // generates password to be stored in the form of "iterations : salt in hex : hash in hex
  private static String generateEncryption(String password)
      throws NoSuchAlgorithmException, InvalidKeySpecException {

    int iterations = 500;
    char[] characters = password.toCharArray();
    byte[] salt = getSalt();

    PBEKeySpec cryptoSpec = new PBEKeySpec(characters, salt, iterations, 64 * 8);
    SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    byte[] hash = key.generateSecret(cryptoSpec).getEncoded();
    return iterations + ":" + toHex(salt) + ":" + toHex(hash);
  }

  // generates salt to be used in encryption
  private static byte[] getSalt()
      throws NoSuchAlgorithmException {

    SecureRandom randomSalt = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt = new byte[16];
    randomSalt.nextBytes(salt);
    return salt;
  }

  // generates hex from the byte array
  private static String toHex(byte[] array) {
    BigInteger bigInt = new BigInteger(1, array);
    String hex = bigInt.toString(16);
    int paddingLength = array.length * 2 - hex.length();
    return paddingLength > 0 ? String.format("%0" + paddingLength + "d", 0) + hex : hex;
  }

  // generates byte array from hex
  private static byte[] fromHex(String hex) {
    byte[] bytes = new byte[hex.length() / 2];
    for (int i = 0; i < bytes.length; ++i) {
      bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
    }
    return bytes;
  }

  byte[] getProfilePictureByteArray() {
    return profilePictureByteArray;
  }

  void setProfilePictureByteArray(byte[] newByteArray) {
    profilePictureByteArray = newByteArray;
  }

  String getUserName() {
    return _id;
  }

  int getElo() {
    return elo;
  }

  String getBio() {
    return bio;
  }

  void setBio(String updatedBio) {
    bio = updatedBio;
  }

  void addFriend(Users user) {
    friends.add(user);
  }

  boolean friendExists(Users user) {
    return friends.contains(user);
  }

  void removeFriend(int index) {
    friends.remove(index);
  }

  void removeFriend(Users user) {
    friends.remove(user);
  }

  int getSizeFriends() {
    return friends.size();
  }

  Users getFriend(int index) {
    return friends.get(index);
  }

  String getChannelKey() {
    return channelKey;
  }

  void setChannelKey(String newKey) {
    channelKey = newKey;
  }

  //takes parameter of other players elo and updates users elo and stats on win
  void calculateEloOnWin(int otherElo) {
    int eloChange;
    int difference = (otherElo - elo);
    if (difference >= 128) {
      eloChange = 4;
    } else if (difference >= 64) {
      eloChange = 2;
    } else {
      eloChange = 1;
    }
    updateElo(eloChange);
    gamesPlayed++;
    gamesWon++;
  }

  //takes parameter of other players elo and updates users elo and stats on loss
  void calculateEloOnLoss(int otherElo) {
    int eloChange;
    int difference = (otherElo - elo);
    if (difference >= 128) {
      eloChange = -1;
    } else {
      eloChange = -2;
    }
    updateElo(eloChange);
    gamesPlayed++;
  }

  //parameter is a positive or negative change in elo
  void updateElo(int eloChange) {
    //factor for calculating elo
    int ELO_K_FACTOR = 32;
    elo += ELO_K_FACTOR * eloChange;
    updateEloTracker(elo);
  }


  int getGamesPlayed() {
    return gamesPlayed;
  }

  int getGamesWon() {
    return gamesWon;
  }

  // updates elo tracker list for analysis
  void updateEloTracker(int newElo) {
    eloOne = eloTwo;
    eloTwo = eloThree;
    eloThree = eloFour;
    eloFour = eloFive;
    eloFive = eloSix;
    eloSix = eloSeven;
    eloSeven = eloEight;
    eloEight = eloNine;
    eloNine = eloTen;
    eloTen = newElo;
  }

  // stores elo history
  ArrayList<Integer> eloList() {
    return new ArrayList<Integer>() {
      {
        add(eloOne);
        add(eloTwo);
        add(eloThree);
        add(eloFour);
        add(eloFive);
        add(eloSix);
        add(eloSeven);
        add(eloEight);
        add(eloNine);
        add(eloTen);
      }
    };
  }

  // set security question answer in case password is lost or forgotten
  void setSecurityAnswer(String answer) {
    try {
      securityAnswer = generateEncryption(answer);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
    }
  }

  boolean checkSecurityAnswer(String answer) {
    try {
      return validatePassword(answer, this.securityAnswer);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
    }
    return false;
  }

  boolean securityExists() {
    return securityAnswer != null;
  }

  //new password must be different from old one, contain a number
  //and a capital letter, and be at least 7 characters
  boolean updatePassword(String newPassword) {
    if (resetPassword(newPassword)) {
      try {
        password = generateEncryption(newPassword);
      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      } catch (InvalidKeySpecException e) {
        e.printStackTrace();
      }
      return true;
    } else {
      return false;
    }
  }

  boolean resetPassword(String newPassword) {
    if (checkCapital(newPassword)) {
      if (checkNumber(newPassword)) {
        return checkLength(newPassword);
      }
    }
    return false;
  }

  boolean checkPassword(String newPassword) {
    try {
      return validatePassword(newPassword, this.password);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
    }
    return false;
  }

  boolean checkCapital(String newPassword) {
    for (int i = 0; i < newPassword.length(); i++) {
      char c = newPassword.charAt(i);
      if (c > 64 && c < 91) { //c is a capital letter
        return true;
      }
    }
    return false;
  }

  boolean checkNumber(String newPassword) {
    for (int i = 0; i < newPassword.length(); i++) {
      char c = newPassword.charAt(i);
      if (c > 47 && c < 58) { //c is a number
        return true;
      }
    }
    return false;
  }

  boolean checkLength(String newPassword) {
    return newPassword.length() > 6;
  }

  void updateBio(String newBio) {
    bio = newBio;
  }
}
