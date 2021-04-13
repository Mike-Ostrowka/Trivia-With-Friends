package com.example.team8project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import com.huhx0015.hxaudio.audio.HXMusic;

public class SettingsActivity extends AppCompatActivity {

  public static final String KEY_PREF_SOUND_SWITCH = "sound_enabled";

  private int song;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    getSupportFragmentManager().beginTransaction()
        .replace(android.R.id.content, new SettingsFragment())
        .commit();

    //load preferences
    PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences, false);
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    boolean soundSwitch = sharedPref.getBoolean(SettingsActivity.KEY_PREF_SOUND_SWITCH, false);
    //if switch value is false, disable music
    if (soundSwitch) {
      playMusic();
    }
  }

  private void playMusic() {
    song = R.raw.smooth_jazz;
    HXMusic.music().load(song).gapless(true).looped(true).play(this);
  }

  public void changePass(View view) {
    Intent intent = new Intent();
    intent.setClass(SettingsActivity.this, ChangePasswordActivity.class);
    startActivity(intent);
  }

  public void saveSettings(View view) {
    HXMusic.stop();
    HXMusic.clear();
    Intent intent = new Intent();
    intent.setClass(SettingsActivity.this, WelcomeActivity.class);
    startActivity(intent);
  }

  public void securityQuestions(View view) {
    Intent intent = new Intent();
    intent.setClass(SettingsActivity.this, SecurityActivity.class);
    startActivity(intent);
  }

  @Override
  protected void onPause() {
    super.onPause();
    HXMusic.stop();
    HXMusic.clear();
  }
}