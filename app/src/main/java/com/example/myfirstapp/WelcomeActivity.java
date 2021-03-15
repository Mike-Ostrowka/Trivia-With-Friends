package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class WelcomeActivity extends AppCompatActivity {

  private Realm realm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);

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

    Intent myIntent = new Intent(this, NewAccountActivity.class);
    startActivity(myIntent);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (realm != null) {
      realm.close();
    }
  }
}
