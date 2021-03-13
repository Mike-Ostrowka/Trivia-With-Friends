package com.example.myfirstapp;

public class Users {

  private String userName;
  private int elo = 1000; //default value is 1000
  private final int ELO_K_FACTOR = 32; //factor for calculating elo
  private String password;
  private boolean accountActive; //if false, user must give new password
  private String bio;

  Users(String name, String pswrd) {
    userName = name;
    if (updatePassword(pswrd)) {
      password = pswrd;
      accountActive = true;
    } else {
      password = "";
      accountActive = false;
    }
  }

  String getUserName() {
    return userName;
  }

  int getElo() {
    return elo;
  }

  boolean getStatus() {
    return accountActive;
  }

  String getBio() {
    return bio;
  }

  //takes parameter of other players elo
  void calculateElo(int otherElo) {
    //TODO: Calculate expected value for both players, calculate elo change, pass to updateElo()
  }

  //parameter is a positive or negative change in elo
  void updateElo(int eloChange) {
    elo += ELO_K_FACTOR * eloChange;
  }

  //TODO: check that name is acceptable
  boolean updateUsername(String newName) {
    if (checkName(newName)) {
      userName = newName;
      return true;
    } else {
      return false;
    }
  }

  //TODO: check for appropriate name (no vulgar language)
  boolean checkName(String name) {
    return true;
  }

  //new password must be different from old one, contain a number
  //and a capital letter, and be at least 7 characters
  boolean updatePassword(String newPassword) {
    if (resetPassword(newPassword)) {
      if (accountActive) {
        password = newPassword;
      }
      return true;
    } else {
      return false;
    }
  }

  boolean resetPassword(String newPassword) {
    if (checkPassword(newPassword)) {
      if (checkCapital(newPassword)) {
        if (checkNumber(newPassword)) {
          return checkLength(newPassword);
        }
      }
    }
    return false;
  }

  boolean checkPassword(String newPassword) {
    return newPassword.equals(password);
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
