package com.example.team8project;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;
import com.huhx0015.hxaudio.audio.HXMusic;


public class FaqActivity extends AppCompatActivity {

  private int song;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_faq);
    //load toolbar
    Toolbar myToolbar = findViewById(R.id.my_toolbar);
    setSupportActionBar(myToolbar);
    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);
    TextView mtextView = findViewById(R.id.tv_faq);
    mtextView.setText(Html.fromHtml(getString(R.string.faq_text)));

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

  @Override
  protected void onDestroy() {
    super.onDestroy();
    HXMusic.stop();
    HXMusic.clear();
  }

}

