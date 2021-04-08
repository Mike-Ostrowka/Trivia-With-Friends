package com.example.team8project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import com.huhx0015.hxaudio.audio.HXMusic;
import com.huhx0015.hxaudio.audio.HXSound;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

  private int song;
  private int click_sound;
  private ImageView profilePicture;
  private String currentPhotoPath;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);

    //load toolbar
    Toolbar myToolbar = findViewById(R.id.toolbar_profile);
    setSupportActionBar(myToolbar);
    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);

//    //load preferences
//    PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences, false);
//    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//    boolean soundSwitch = sharedPref.getBoolean(SettingsActivity.KEY_PREF_SOUND_SWITCH, false);
//    //if switch value is false, disable music
//    if (soundSwitch) {
//      playMusic();
//    }

    click_sound = R.raw.click;

    Button cameraButton = findViewById(R.id.camera_button);
    Button friends = findViewById(R.id.btn_friends);
    profilePicture = findViewById(R.id.profile_picture);

    friends.setOnClickListener(view -> {
      HXSound.sound().load(click_sound).play(this);
      Intent intent = new Intent(ProfileActivity.this, FriendsActivity.class);
      startActivity(intent);
    });

    cameraButton.setOnClickListener(view -> {
      HXSound.sound().load(click_sound).play(this);

      Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//      if (intent.resolveActivity(getPackageManager()) != null) {
//        /* creating file where photo should go */
//        File photoFile = null;
//        try {
//          photoFile = createImageFile();
//        } catch (IOException e) {
//          e.printStackTrace();
//        }
//        if (photoFile != null) {
//          Uri photoURI = FileProvider.getUriForFile(this,
//              "com.example.android.fileprovider",
//              photoFile);
//          intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
          startActivityForResult(intent, 0);
//        }
//      }
    });
  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    Bitmap bp = (Bitmap) data.getExtras().get("data");
    profilePicture.setImageBitmap(bp);
  }

  private File createImageFile() throws IOException {
    // create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "JPEG_" + timeStamp + "_";
    File storageDir = getExternalFilesDir((Environment.DIRECTORY_PICTURES));
    File image = File.createTempFile(
        imageFileName,
        ".jpg",
        storageDir
    );
    currentPhotoPath = image.getAbsolutePath();
    return image;
  }

  private void playMusic() {
    song = R.raw.smooth_jazz;
    HXMusic.music().load(song).gapless(true).looped(true).play(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    HXSound.clear();
  }

  @Override
  protected void onPause() {
    super.onPause();
    HXSound.clear();
  }
}
