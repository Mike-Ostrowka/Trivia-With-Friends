package com.example.team8project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import io.realm.Realm;

public class MainMenuActivity extends AppCompatActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_menu);
    checkLogin();
  }


  @Override
  protected void onResume() {
    super.onResume();
    Button btn_log_in, btn_reg;
    //Set the buttons
    btn_log_in = findViewById(R.id.btn_login);
    btn_reg = findViewById(R.id.btn_register);

    //Set the event for the login button
    btn_log_in.setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.setClass(MainMenuActivity.this, LoginActivity.class);
      startActivity(intent);
    });

    //Set the event for register button
    btn_reg.setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.setClass(MainMenuActivity.this, NewAccountActivity.class);
      startActivity(intent);
    });
  }

//check if user is already logged in
  public void checkLogin() {
    Realm realm = Realm.getDefaultInstance();
    if (realm.where(Users.class).equalTo("loginStatus", true).findFirst() != null) {
      realm.close();
      Intent intent = new Intent();
      intent.setClass(MainMenuActivity.this, WelcomeActivity.class);
      startActivity(intent);
    }
    realm.close();
  }


}