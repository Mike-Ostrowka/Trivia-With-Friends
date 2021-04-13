package com.example.team8project;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import com.huhx0015.hxaudio.audio.HXMusic;
import com.huhx0015.hxaudio.audio.HXSound;

import java.io.ByteArrayOutputStream;

import io.realm.Realm;

public class ProfileActivity extends AppCompatActivity {

  private int song;
  private int click_sound;
  private ImageView profilePicture;
  private String bio;
  private Realm realm;
  private Users current;
  private loginPreferences session;
  private String username;
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
    Button friends = findViewById(R.id.btn_friends);
    Button updateBio = findViewById(R.id.updateBio_button);
    Button chat = findViewById(R.id.btn_chat);
    profilePicture = findViewById(R.id.profile_picture);
    TextView userBio = findViewById(R.id.user_bio);

    friends.setOnClickListener(view -> {
      HXSound.sound().load(click_sound).play(this);
      Intent intent = new Intent(ProfileActivity.this, FriendsActivity.class);
      startActivity(intent);
    });

    // TODO need to take profile pic byte[] from realm, and use as profile pic
    byte[] bitmapData = current.getProfilePictureByteArray();
    if (bitmapData != null) {
      Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
      profilePicture.setImageBitmap(bitmap);
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

    // Display bio saved in database, if any
    bio = current.getBio();
    if (!(bio == null)) {
      userBio.setText(bio);
      userBio.setCursorVisible(false);
      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    // update database with new bio, censor if needed
    userBio.setOnClickListener(view -> userBio.setCursorVisible(true));
    updateBio.setOnClickListener(view -> {
      HXSound.sound().load(click_sound).play(this);

      String text = userBio.getText().toString();
      if (text.equals("")) {
        return;
      }
      text = BadWordFilter
          .getCensoredText(text, getApplicationContext(), getString(R.string.censored_bio));

      String finalText = text;
      realm.executeTransaction(transactionRealm -> current.setBio(finalText));
      userBio.setText(finalText);
      Toast.makeText(getApplicationContext(), getString(R.string.bio_updated),
          Toast.LENGTH_SHORT).show();
    });

    chat.setOnClickListener(view -> {
      Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
      startActivity(intent);
    });

    TextView gamesPlayed = findViewById(R.id.games_played);
    String gamesPlayedString = getString(R.string.games_played) + "   " + current.getGamesPlayed();
    gamesPlayed.setText(gamesPlayedString);

    TextView gamesWon = findViewById(R.id.games_won);
    String gamesWonString = getString(R.string.games_won) + "   " + current.getGamesWon();
    gamesWon.setText(gamesWonString);

    //todo present elo tracker graph

  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    Bitmap bp = (Bitmap) data.getExtras().get("data");
    profilePicture.setImageBitmap(bp);
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
    byte[] byteArray = stream.toByteArray();
    if(realm == null) {
      realm = Realm.getDefaultInstance();
    }
    realm.executeTransaction(transactionRealm -> {
      Users temp = transactionRealm.where(Users.class).equalTo("_id", current.getUserName())
          .findFirst();
      temp.setProfilePictureByteArray(byteArray);
    });
    realm.close();
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


