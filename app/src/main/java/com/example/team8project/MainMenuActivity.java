package com.example.team8project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.huhx0015.hxaudio.audio.HXSound;
import io.realm.Realm;

public class MainMenuActivity extends AppCompatActivity {

  private loginPreferences session;
  private SoundPool soundPool;
  private int C_sound;
  private int clickSound;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_menu);
    addAdmin();
    checkLogin();
    clickSound = R.raw.click;

  }

  @Override
  protected void onResume() {
    super.onResume();
    checkLogin();
    Button btn_log_in, btn_reg;
    //Set the buttons
    btn_log_in = findViewById(R.id.btn_login);
    btn_reg = findViewById(R.id.btn_register);

    //Set the event for the login button
    btn_log_in.setOnClickListener(v -> {
      HXSound.sound().load(clickSound).play(this);
      Intent intent = new Intent();
      intent.setClass(MainMenuActivity.this, LoginActivity.class);
      startActivity(intent);
    });

    //Set the event for register button
    btn_reg.setOnClickListener(v -> {
      HXSound.sound().load(clickSound).play(this);
      Intent intent = new Intent();
      intent.setClass(MainMenuActivity.this, NewAccountActivity.class);
      startActivity(intent);
    });
  }

  //check if user is already logged in
  public void checkLogin() {
    session = new loginPreferences(getApplicationContext());
    String username = session.getusername();
    if (!username.equals("")) {
      Intent intent = new Intent();
      intent.setClass(MainMenuActivity.this, WelcomeActivity.class);
      startActivity(intent);
    }
  }

  public void addAdmin() {
    Realm realm = Realm.getDefaultInstance();
    if(realm.where(Users.class).equalTo("_id", "adminUser").findFirst() == null) {
      Users temp = new Users("adminUser", "Password123");
      realm.executeTransaction(transactionRealm -> transactionRealm.insert(temp));
    }
  }


}