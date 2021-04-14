package com.example.team8project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.huhx0015.hxaudio.audio.HXMusic;
import com.huhx0015.hxaudio.audio.HXSound;
import io.realm.Realm;
import java.util.ArrayList;

public class FriendsProfileActivity extends AppCompatActivity {

  private int click_sound;
  private Realm realm;
  private Users friend;
  private loginPreferences session;
  private String username;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_friends_profile);

    //load toolbar
    Toolbar myToolbar = findViewById(R.id.toolbar_friend_profile);
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

    click_sound = R.raw.click;

    String sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");
    if (!(sessionId == null)) {
      username = sessionId;
    } else {
      Dialogs.intentDialog(getString(R.string.user_failed), this, FriendsActivity.class);
    }
    Realm realm = Realm.getDefaultInstance();
    friend = realm.where(Users.class).equalTo("_id", username).findFirst();

    Button viewFriendsProgress = findViewById(R.id.friends_view_progress);
    Button friendsChat = findViewById(R.id.friends_chat_room);
    ImageView profilePicture = findViewById(R.id.friends_profile_picture);
    TextView userBio = findViewById(R.id.friends_bio);
    TextView gamesPlayed = findViewById(R.id.friends_games_played);
    TextView gamesWon = findViewById(R.id.friends_games_won);
    TextView userName = findViewById(R.id.friend_name);

    // set text of friends stats
    userName.setText(username);
    String gamesPlayedString = getString(R.string.games_played) + "   " + friend.getGamesPlayed();
    gamesPlayed.setText(gamesPlayedString);
    String gamesWonString = getString(R.string.games_won) + "   " + friend.getGamesWon();
    gamesWon.setText(gamesWonString);

    // set friends profile picture
    byte[] bitmapData = friend.getProfilePictureByteArray();
    if (bitmapData != null) {
      Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
      profilePicture.setImageBitmap(bitmap);
    }

    // set friends bio
    String bio = friend.getBio();
    if (!(bio == null)) {
      userBio.setText(bio);
    }

    // set friends chat room button to show their name
    String friendsButtonText = username + "'s CHAT ROOM";
    friendsChat.setText(friendsButtonText);

    // view your friends progress
    viewFriendsProgress.setOnClickListener(view -> {
      HXSound.sound().load(click_sound).play(this);
      Intent intent = new Intent(FriendsProfileActivity.this, GraphActivity.class);
      ArrayList<Integer> sessionEloId = friend.eloList();
      intent.putExtra("EXTRA_SESSION_ID", sessionEloId);
      startActivity(intent);
    });

    // join friends chat room
    friendsChat.setOnClickListener(view -> {
      HXSound.sound().load(click_sound).play(this);
      Intent intent = new Intent(FriendsProfileActivity.this, ChatActivity.class);
      String sessionID = friend.getChannelKey();
      intent.putExtra("EXTRA_SESSION_ID", sessionID);
      startActivity(intent);
    });
  }

  private void playMusic() {
    int song = R.raw.smooth_jazz;
    HXMusic.music().load(song).gapless(true).looped(true).play(this);
  }


  @Override
  protected void onPause() {
    super.onPause();
    if (realm != null) {
      realm.close();
    }
    HXSound.clear();
    HXMusic.stop();
    HXMusic.clear();
  }
}
