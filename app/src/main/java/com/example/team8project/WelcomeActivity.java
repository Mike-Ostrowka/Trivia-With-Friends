package com.example.team8project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.huhx0015.hxaudio.audio.HXMusic;
import com.huhx0015.hxaudio.audio.HXSound;
import io.realm.Realm;
import io.realm.RealmConfiguration;

//todo fix java.lang.IllegalStateException: Cannot modify managed objects outside of a write transaction. at line 34 of this activity


public class WelcomeActivity extends AppCompatActivity {

  private Users current;
  private loginPreferences session;
  private String username;
  private int song;
  private int clickSound;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);

    clickSound = R.raw.click;

    //load preferences
    PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences, false);
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    boolean soundSwitch = sharedPref.getBoolean(SettingsActivity.KEY_PREF_SOUND_SWITCH, false);
    //if switch value is false, disable music
    if (soundSwitch) {
     playMusic();
    }

    //open a realm and find logged in user
    session = new loginPreferences(getApplicationContext());
    username = session.getusername();
    Realm realm = Realm.getDefaultInstance();
    current = realm.where(Users.class).equalTo("_id", username).findFirst();

    TextView greeting = findViewById(R.id.textViewGreeting);
    String greetingText = current.getUserName();
    greeting.setText(greetingText);

    Button mButtonLogout = findViewById(R.id.logout_button);
    mButtonLogout.setOnClickListener(v -> {

      HXSound.sound().load(clickSound).play(this);

      session.setusername("");

      Toast.makeText(getApplicationContext(), R.string.logout_message,
          Toast.LENGTH_LONG).show();
      realm.close();
      Intent intent = new Intent();
      intent.setClass(WelcomeActivity.this, MainMenuActivity.class);
      startActivity(intent);
    });
    Button mButtonFAQ = findViewById(R.id.btn_settings);
    mButtonFAQ.setOnClickListener(v -> {
      HXSound.sound().load(clickSound).play(this);
      Intent intent = new Intent();
      intent.setClass(WelcomeActivity.this, SettingsActivity.class);
      startActivity(intent);
    });

    Button mButtonSettings = findViewById(R.id.btn_faq);
    mButtonSettings.setOnClickListener(v -> {
      HXSound.sound().load(clickSound).play(this);
      Intent intent = new Intent();
      intent.setClass(WelcomeActivity.this, FaqActivity.class);
      startActivity(intent);
    });
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
  }

  private void playMusic() {
    song = R.raw.menu;
    HXMusic.music().load(song).gapless(true).looped(true).play(this);
  }

  @Override
  protected void onResume() {
    super.onResume();

    //load preferences
    PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences, false);
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    boolean soundSwitch = sharedPref.getBoolean(SettingsActivity.KEY_PREF_SOUND_SWITCH, false);
    //if switch value is false, disable music
    if (soundSwitch) {
      playMusic();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    HXMusic.stop();
    HXMusic.clear();
  }

  @Override
  protected void onPause() {
    super.onPause();
    HXMusic.stop();
    HXMusic.clear();
  }
}

