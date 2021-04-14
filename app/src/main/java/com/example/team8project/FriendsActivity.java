package com.example.team8project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.design.widget.TabLayout.TabLayoutOnPageChangeListener;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import com.huhx0015.hxaudio.audio.HXMusic;
import com.huhx0015.hxaudio.audio.HXSound;
import io.realm.Realm;

public class FriendsActivity extends AppCompatActivity {

  ViewPager simpleViewPager;
  TabLayout tabLayout;
  private Users current;
  private loginPreferences session;
  private String username;
  private int song;
  private Realm realm;


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

    //open a realm and find logged in user
    session = new loginPreferences(getApplicationContext());
    username = session.getusername();
    realm = Realm.getDefaultInstance();
    current = realm.where(Users.class).equalTo("_id", username).findFirst();
    realm.close();

    simpleViewPager = findViewById(R.id.simpleViewPager);
    tabLayout = findViewById(R.id.simpleTabLayout);

    //create first tab
    Tab firstTab = tabLayout.newTab();
    firstTab.setText("Add Friends");
    firstTab.setIcon(R.drawable.ic__add_friend);
    tabLayout.addTab(firstTab);

    //create second tab
    Tab secondTab = tabLayout.newTab();
    secondTab.setText("Friends List");
    secondTab.setIcon(R.drawable.ic_friends_tab);
    tabLayout.addTab(secondTab);

    //add tabs to adapter
    PagerAdapter adapter = new PagerAdapter
        (getSupportFragmentManager(), tabLayout.getTabCount());
    simpleViewPager.setAdapter(adapter);

    //change listener on swipe
    simpleViewPager.addOnPageChangeListener(new TabLayoutOnPageChangeListener(tabLayout));

    //change listener on button press
    tabLayout.setOnTabSelectedListener(onTabSelectedListener(simpleViewPager));

  }

  private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager pager) {
    return new TabLayout.OnTabSelectedListener() {

      //play sound and change tab
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

  //play background music
  private void playMusic() {
    song = R.raw.smooth_jazz;
    HXMusic.music().load(song).gapless(true).looped(true).play(this);
  }

  //clear HXMusic
  @Override
  protected void onPause() {
    super.onPause();
    HXSound.clear();
    HXMusic.stop();
    HXMusic.clear();
  }

}