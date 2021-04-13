package com.example.team8project;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class loginPreferences {

  private final SharedPreferences prefs;

  public loginPreferences(Context cntx) {
    prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
  }

  public void setusername(String username) {
    prefs.edit().putString("username", username).apply();
  }

  public String getusername() {
    return prefs.getString("username", "");
  }
}
