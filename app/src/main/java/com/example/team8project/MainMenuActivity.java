package com.example.team8project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainMenuActivity extends AppCompatActivity {

  private Realm realm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_menu);

    //start realm
    Realm.init(this);
    RealmConfiguration config = new RealmConfiguration.Builder()
        .allowQueriesOnUiThread(true)
        .allowWritesOnUiThread(true)
        .build();
    realm = Realm.getInstance(config);
    Log.v("EXAMPLE", "Successfully opened a realm at: " + realm.getPath());

    //open a realm and save temp to it if it does not exist
    try {
      realm = Realm.getDefaultInstance();
      Users defaultExists = realm.where(Users.class).equalTo("userName", "admin").findFirst();

      if (defaultExists == null) {
        //default user
        final Users temp = new Users("admin", "Password123");
        realm.executeTransaction(new Realm.Transaction() {
          @Override
          public void execute(Realm realm) {
            realm.copyToRealmOrUpdate(temp);
          }
        });
      }
    } finally {
      if (realm != null) {
        realm.close();
      }
    }

    Button btn_log_in, btn_reg;
    //Set the buttons
    btn_log_in = findViewById(R.id.btn_login);
    btn_reg = findViewById(R.id.btn_register);

    //Set the event for the login button
    btn_log_in.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(MainMenuActivity.this, LoginActivity.class);
        startActivity(intent);
      }
    });

    //Set the event for register button
    btn_reg.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(MainMenuActivity.this, NewAccountActivity.class);
        startActivity(intent);
      }
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (realm != null) {
      realm.close();
    }
  }
}