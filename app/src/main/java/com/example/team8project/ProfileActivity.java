package com.example.team8project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.huhx0015.hxaudio.audio.HXMusic;
import com.huhx0015.hxaudio.audio.HXSound;
import io.realm.Realm;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

  private int song;
  private int click_sound;
  private ImageView profilePicture;
  private String bio;
  private Realm realm;
  private Users current;
  private loginPreferences session;
  private String username;
//  private String currentPhotoPath;

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

    click_sound = R.raw.click;

    Button cameraButton = findViewById(R.id.camera_button);
    Button friends = findViewById(R.id.btn_friends);
    Button updateBio = findViewById(R.id.updateBio_button);
    profilePicture = findViewById(R.id.profile_picture);
    TextView userBio = findViewById(R.id.user_bio);


    friends.setOnClickListener(view -> {
      HXSound.sound().load(click_sound).play(this);
      Intent intent = new Intent(ProfileActivity.this, FriendsActivity.class);
      startActivity(intent);
    });

    // TODO need to take profile pic byte[] from realm, and use as profile pic
//    byte[] bitmapData = users.getProfilePictureByteArray;
//    if (bitmapData != null) {
//      Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
//      profilePicture.setImageBitmap(bitmap);
//    }

    // camera button will update profile picture and what is stored in realm database
    cameraButton.setOnClickListener(view -> {
      HXSound.sound().load(click_sound).play(this);
      Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

      if (intent.resolveActivity(getPackageManager()) != null) {
          startActivityForResult(intent, 0);
      }
    });

    //todo check if bio is present in realm
//    bio = users.getBio;
//    if (!(bio==null){
//      userBio.setText(bio);
//    }
    //todo make button update realm bio with whatever user typed into the textbox
    updateBio.setOnClickListener(view ->{
      HXSound.sound().load(click_sound).play(this);

    });

    //todo present users stats on page
//    TextView gamesPlayed = findViewById(R.id.games_played);
//    String gamesPlayedString = getString(R.string.games_played) + users.getGamesPlayed;
//    gamesPlayed.setText(gamesPlayedString);
//
//    TextView gamesWon = findViewById(R.id.games_won);
//    String gamesWonString = getString(R.string.games_played) + users.getGamesWon;
//    gamesWon.setText(gamesWonString);

    //todo present elo tracker graph
  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    Bitmap bp = (Bitmap) data.getExtras().get("data");
    profilePicture.setImageBitmap(bp);
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
    byte[] byteArray = stream.toByteArray();
    //TODO save byte[] byteArray to Realm
    // something like:
    // users.setProfilePictureByteArray(byteArray);
  }

//  private File createImageFile() throws IOException {
//    // create an image file name
//    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//    String imageFileName = "JPEG_" + timeStamp + "_";
//    File storageDir = getExternalFilesDir((Environment.DIRECTORY_PICTURES));
//    File image = File.createTempFile(
//        imageFileName,
//        ".jpg",
//        storageDir
//    );
//    currentPhotoPath = image.getAbsolutePath();
//    return image;
//  }

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
