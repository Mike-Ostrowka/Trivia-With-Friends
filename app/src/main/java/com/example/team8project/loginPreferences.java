package com.example.team8project;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

//class to keep a user logged in until they log out
public class loginPreferences {

  private final SharedPreferences prefs;

  public loginPreferences(Context cntx) {
    prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
  }

  public void setUsername(String username) {
    prefs.edit().putString("username", username).apply();
  }

  public String getUsername() {
    return prefs.getString("username", "");
  }
}
