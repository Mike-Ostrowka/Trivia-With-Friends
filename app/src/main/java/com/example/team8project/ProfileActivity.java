package com.example.team8project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.huhx0015.hxaudio.audio.HXMusic;
import com.huhx0015.hxaudio.audio.HXSound;
import io.realm.Realm;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

  private int song;
  private int click_sound;
  private ImageView profilePicture;
  private String bio;
  private Realm realm;
  private Users current;
  private loginPreferences session;
  private String username;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);

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

    click_sound = R.raw.click;

    Button cameraButton = findViewById(R.id.camera_button);
    Button viewProgress = findViewById(R.id.friends_view_progress);
    Button updateBio = findViewById(R.id.updateBio_button);
    Button globalChat = findViewById(R.id.global_chatroom);
    Button myChat = findViewById(R.id.friends_chat_room);
    profilePicture = findViewById(R.id.friends_profile_picture);
    TextView userBio = findViewById(R.id.friends_bio);
    TextView gamesWon = findViewById(R.id.friends_games_won);
    TextView gamesPlayed = findViewById(R.id.friends_games_played);

//    if(!(current.getChannelKey()==null)){
//      System.out.println("users channel key is: " + current.getChannelKey());
//      if((globalChat.getTag()==null)) {
//        globalChat.setTag(current.getChannelKey());
//        System.out.println("chat tag is channel key now: " + current.getChannelKey());
//      } else {
//        System.out.println("there was already a chat tag: " + globalChat.getTag());
//      }
//    } else {
//      System.out.println("users channel key is null");
//    }

    // set text of users stats
    String gamesPlayedString = getString(R.string.games_played) + "   " + current.getGamesPlayed();
    gamesPlayed.setText(gamesPlayedString);
    String gamesWonString = getString(R.string.games_won) + "   " + current.getGamesWon();
    gamesWon.setText(gamesWonString);

    // retrieve profile picture from database, if present
    byte[] bitmapData = current.getProfilePictureByteArray();
    if (bitmapData != null) {
      Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
      profilePicture.setImageBitmap(bitmap);
    }

    // Display bio saved in database, if present
    bio = current.getBio();
    if (!(bio == null)) {
      userBio.setText(bio);
      userBio.setCursorVisible(false);
      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    // camera button will update profile picture and what is stored in realm database
    cameraButton.setOnClickListener(view -> {
      if (getApplicationContext().getPackageManager()
          .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
        HXSound.sound().load(click_sound).play(this);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
          startActivityForResult(intent, 0);
        }
      } else {
        // no camera on this device
        Dialogs.buildDialog(getString(R.string.no_camera), this);
      }
    });

    userBio.setOnClickListener(view -> userBio.setCursorVisible(true));

    // update database with new bio, censor if needed
    updateBio.setOnClickListener(view -> {
      HXSound.sound().load(click_sound).play(this);

      String text = userBio.getText().toString();
      if (text.equals("") || text.equals(current.getBio())) {
        return;
      }
      text = BadWordFilter
          .getCensoredText(text, getApplicationContext(), getString(R.string.censored_bio));
      String finalText = text;

      Realm realmBio = Realm.getDefaultInstance();
      realmBio.executeTransaction(transactionRealm -> current.setBio(finalText));
      userBio.setText(finalText);
      Toast.makeText(getApplicationContext(), getString(R.string.bio_updated),
          Toast.LENGTH_SHORT).show();
      realmBio.close();
    });

    // allows users to track their elo progress across their last 10 games
    viewProgress.setOnClickListener(view -> {
      HXSound.sound().load(click_sound).play(this);
      Intent intent = new Intent(ProfileActivity.this, GraphActivity.class);
      ArrayList<Integer> sessionEloId = current.eloList();
      intent.putExtra("EXTRA_SESSION_ID", sessionEloId);
      startActivity(intent);
    });

    // will allow user to enter their own chat room, and send messages to all their friends,
    // similar to posting stuff on your own social media page
    myChat.setOnClickListener(view -> {
      HXSound.sound().load(click_sound).play(this);
      Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
      String sessionId = current.getChannelKey();
      intent.putExtra("EXTRA_SESSION_ID", sessionId);
      startActivity(intent);
    });

    // will allow users to send messages to entire playerbase in a global chat room
    globalChat.setOnClickListener(view -> {
      HXSound.sound().load(click_sound).play(this);
      Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
      startActivity(intent);
    });

//    friendbtn.setOnClickListener(view -> {
//      HXSound.sound().load(click_sound).play(this);
//      Intent intent = new Intent(thisActibvity.this, FriendsProfileActivity.class);
//      String sessionId = friend.username;
//      intent.putExtra("EXTRA_SESSION_ID", sessionId);
//      startActivity(intent);
//    });
  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 0) {
      if (resultCode == RESULT_OK) {
        //Pic taken
        Bitmap bp = (Bitmap) data.getExtras().get("data");
        profilePicture.setImageBitmap(bp);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        if (realm == null) {
          realm = Realm.getDefaultInstance();
        }
        realm.executeTransaction(transactionRealm -> {
          Users temp = transactionRealm.where(Users.class).equalTo("_id", current.getUserName())
              .findFirst();
          temp.setProfilePictureByteArray(byteArray);
        });
        realm.close();
      } else {
        Dialogs.buildDialog(getString(R.string.no_camera), this);
      }
    }
  }


  private void playMusic() {
    song = R.raw.smooth_jazz;
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


