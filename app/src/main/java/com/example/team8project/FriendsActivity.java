package com.example.team8project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.huhx0015.hxaudio.audio.HXMusic;
import com.huhx0015.hxaudio.audio.HXSound;

public class FriendsActivity extends AppCompatActivity {

  private Users current;
  private loginPreferences session;
  private String username;
  private int song;
  private int click_sound;
  ViewPager simpleViewPager;
  TabLayout tabLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_friends);

    //load toolbar
    Toolbar myToolbar = findViewById(R.id.toolbar_friends);
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

    simpleViewPager = (ViewPager) findViewById(R.id.simpleViewPager);
    tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);

    TabLayout.Tab firstTab = tabLayout.newTab();
    firstTab.setText("Friends");
    firstTab.setIcon(R.drawable.ic_friends_tab);
    tabLayout.addTab(firstTab);

    TabLayout.Tab secondTab = tabLayout.newTab();
    secondTab.setText("Inbox");
    secondTab.setIcon(R.drawable.ic_inbox_tab);
    tabLayout.addTab(secondTab);

    PagerAdapter adapter = new PagerAdapter
        (getSupportFragmentManager(), tabLayout.getTabCount());
    simpleViewPager.setAdapter(adapter);

    //change on swipe
    simpleViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    //change on button press
    tabLayout.setOnTabSelectedListener(onTabSelectedListener(simpleViewPager));
  }

  private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager pager) {
    return new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        HXSound.sound().load(R.raw.click).play(getApplicationContext());
        pager.setCurrentItem(tab.getPosition());
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {

      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {

      }
    };
  }

  private void playMusic() {
    song = R.raw.smooth_jazz;
    HXMusic.music().load(song).gapless(true).looped(true).play(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    HXSound.clear();
    HXMusic.stop();
    HXMusic.clear();
  }

  @Override
  protected void onPause() {
    super.onPause();
    HXSound.clear();
    HXMusic.stop();
    HXMusic.clear();
  }

}