package com.example.team8project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import com.huhx0015.hxaudio.audio.HXMusic;
import com.huhx0015.hxaudio.audio.HXSound;
import io.realm.Realm;
import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {

  private int song;
  private Realm realm;
  private Users current;
  private loginPreferences session;
  private String username;
//  private ArrayList lineEntries;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_graph);

    //load toolbar
    Toolbar myToolbar = findViewById(R.id.toolbar_profile);
    setSupportActionBar(myToolbar);
    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);

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

    ArrayList<Integer> graphData =  current.eloList();

    realm.close();
  }


  private void playMusic() {
    song = R.raw.smooth_jazz;
    HXMusic.music().load(song).gapless(true).looped(true).play(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    HXSound.clear();
    HXMusic.stop();
    HXMusic.clear();
  }
}
