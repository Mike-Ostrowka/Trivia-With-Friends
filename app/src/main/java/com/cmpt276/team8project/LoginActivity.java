package com.cmpt276.team8project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.huhx0015.hxaudio.audio.HXSound;
import io.realm.Realm;

public class LoginActivity extends AppCompatActivity {


  private Realm realm; //declare realm variable
  private Users currentUser;
  private loginPreferences session;
  private int click_sound;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    click_sound = R.raw.click;

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    checkLogin();

    //click listener to login
    Button mButton = findViewById(R.id.login_button);
    mButton.setOnClickListener(v -> {

      HXSound.sound().load(click_sound).play(this);
      EditText nameEdit = findViewById(R.id.loginUsername);
      EditText passwordEdit = findViewById(R.id.loginPassword);
      final String name = nameEdit.getText().toString();
      final String password = passwordEdit.getText().toString();

      //open a realm and check if user name exists in database
      try {
        realm = Realm.getDefaultInstance();

        if (realm.where(Users.class).equalTo("_id", name).findFirst() != null) {

          //create temp for user to check password
          currentUser = realm.where(Users.class).equalTo("_id", name).findFirst();
          if (currentUser == null) {
            Dialogs.buildDialog(getString(R.string.user_failed), this);
            return;
          }

          //check if password matches hashed password associated with the username
          if (currentUser.checkPassword(password)) {
            session = new loginPreferences(getApplicationContext());
            session.setUsername(currentUser.getUserName());

            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, WelcomeActivity.class);
            startActivity(intent);
          } else {
            Dialogs.buildDialog(getString(R.string.credentials_incorrect), this);
          }
        } else {
          Dialogs.buildDialog(getString(R.string.user_failed), this);

        }


      } finally {
        if (realm != null) {
          realm.close();
        }
      }
    });

    //Set event for Text View Forgot password
    TextView tv_forgot_password = findViewById(R.id.tv_Forgot_Password);
    tv_forgot_password.setOnClickListener(view -> {
      HXSound.sound().load(click_sound).play(this);
      Intent intent = new Intent();
      intent.setClass(LoginActivity.this, ResetActivity.class);
      startActivity(intent);
    });

    //Set event for Text View New user
    TextView tv_new_user = findViewById(R.id.tv_No_Account);
    tv_new_user.setOnClickListener(view -> {
      HXSound.sound().load(click_sound).play(this);
      Intent intent = new Intent();
      intent.setClass(LoginActivity.this, NewAccountActivity.class);
      startActivity(intent);
      finish();
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    checkLogin();
  }

  //check if user is already logged in
  public void checkLogin() {
    session = new loginPreferences(getApplicationContext());
    String username = session.getUsername();
    if (!username.equals("")) {
      Intent intent = new Intent();
      intent.setClass(LoginActivity.this, WelcomeActivity.class);
      startActivity(intent);
    }
  }

  //close realm
  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (realm != null) {
      realm.close();
    }
  }
}