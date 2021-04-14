package com.example.team8project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.huhx0015.hxaudio.audio.HXMusic;
import com.huhx0015.hxaudio.audio.HXSound;
import io.realm.Realm;
import java.util.Objects;

//todo fix java.lang.IllegalStateException: Cannot modify managed objects outside of a write transaction. at line 34 of this activity


@SuppressWarnings("FieldCanBeLocal")
public class WelcomeActivity extends AppCompatActivity {

  private Users current;
  private loginPreferences session;
  private String username;
  private int song;
  private int click_sound;
  private NavigationView navigationView;
  private DrawerLayout drawerLayout;
  private long mExitTime;


  @SuppressLint("NonConstantResourceId")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_ACTION_BAR);

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);

    click_sound = R.raw.click;

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
    username = session.getUsername();
    Realm realm = Realm.getDefaultInstance();
    current = realm.where(Users.class).equalTo("_id", username).findFirst();

    TextView greeting = findViewById(R.id.textViewGreeting);
    String greetingText = current.getUserName();
    greeting.setText(greetingText);

    //click listener for profile button
    Button mButtonProfile = findViewById(R.id.profile_button);
    mButtonProfile.setOnClickListener(v -> {
      HXSound.sound().load(click_sound).play(this);
      Intent intent = new Intent();
      intent.setClass(WelcomeActivity.this, ProfileActivity.class);
      startActivity(intent);
    });

    //click listener for new game button
    Button newGameBtn = findViewById(R.id.newgame_button);
    newGameBtn.setOnClickListener(v -> {
      HXSound.sound().load(click_sound).play(this);
      Intent intent = new Intent();
      intent.setClass(WelcomeActivity.this, GameActivity.class);
      startActivity(intent);
    });
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

    //open navigation drawer
    Toolbar toolbar = findViewById(R.id.toolbar_WA);
    setSupportActionBar(toolbar);
    toolbar.setNavigationOnClickListener(v -> finish());

    //opens friends list on options button press (displayed as friends logo)
    toolbar.setOnMenuItemClickListener(menuItem -> {
      if (menuItem.getItemId() == R.id.ic_friends_wa) {
        Intent intent = new Intent();
        intent.setClass(WelcomeActivity.this, FriendsActivity.class);
        startActivity(intent);
      }
      return true;
    });

    Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    drawerLayout = findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawerLayout, toolbar, R.string.open, R.string.close);
    drawerLayout.addDrawerListener(toggle);

    toggle.syncState();

    navigationView = findViewById(R.id.nav_view);
    navigationView.setBackgroundColor(getResources().getColor(R.color.white));

    //listens for item clicks inside of navigation drawer
    navigationView.setNavigationItemSelectedListener(menuItem -> {
      Intent intent;
      switch (menuItem.getItemId()) {

        //settings button
        case R.id.nm_setting:
          intent = new Intent();
          intent.setClass(WelcomeActivity.this, SettingsActivity.class);
          startActivity(intent);
          break;

        //faq button
        case R.id.nm_faq:
          intent = new Intent();
          intent.setClass(WelcomeActivity.this, FaqActivity.class);
          startActivity(intent);
          break;

        //logout button
        case R.id.nm_logout:
          session.setUsername("");

          Toast.makeText(getApplicationContext(), R.string.logout_message,
              Toast.LENGTH_LONG).show();
          realm.close();
          intent = new Intent();
          intent.setClass(WelcomeActivity.this, MainMenuActivity.class);
          startActivity(intent);
          break;

        //friends list button
        case R.id.nm_friends:
          intent = new Intent();
          intent.setClass(WelcomeActivity.this, FriendsActivity.class);
          startActivity(intent);
          break;

        //open personal chatroom on click
        case R.id.nm_my_chatroom:
          intent = new Intent();
          intent.setClass(WelcomeActivity.this, ChatActivity.class);
          String sessionId = current.getChannelKey();
          intent.putExtra("EXTRA_SESSION_ID", sessionId);
          startActivity(intent);
          break;

        //open global chat on click
        case R.id.nm_global_chat_room:
          intent = new Intent();
          intent.setClass(WelcomeActivity.this, ChatActivity.class);
          startActivity(intent);
          break;
      }
      drawerLayout.closeDrawers();
      return false;
    });
    drawerLayout = findViewById(R.id.drawer_layout);

  }

  //open friends list on click
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.toolbar_wa, menu);
    return true;
  }

  //open navigation drawer on click
  @Override
  public void onBackPressed() {
    drawerLayout = findViewById(R.id.drawer_layout);
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
      drawerLayout.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }


  private void playMusic() {
    song = R.raw.menu;
    HXMusic.music().load(song).gapless(true).looped(true).play(this);
  }

  //restart sound on resume
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

  //clear resources
  @Override
  protected void onDestroy() {
    super.onDestroy();
    HXMusic.stop();
    HXMusic.clear();
  }
}

