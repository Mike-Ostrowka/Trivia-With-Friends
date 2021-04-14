package com.cmpt276.team8project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class StartupActivity extends AppCompatActivity {

  final private String PARTITION_VALUE = "all";
  private Realm uiThreadRealm;
  private App app;
  private User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_startup);

    //set up realm app
    final String appID = "triviawithfriends-ljuog";
    app = new App(new AppConfiguration.Builder(appID).build());
    Credentials credentials = Credentials.anonymous();

    //login to realm app anonymously
    app.loginAsync(credentials, result -> {
      if (result.isSuccess()) {
        Log.v("QUICKSTART", "Successfully authenticated anonymously.");
        user = app.currentUser();
        if (user == null) {
          Log.v("QUICKSTART", "null user");
        }

        //build and set default realm
        SyncConfiguration config = new SyncConfiguration.Builder(
            user,
            PARTITION_VALUE).allowWritesOnUiThread(true).allowQueriesOnUiThread(true).build();
        Realm.setDefaultConfiguration(config);
        uiThreadRealm = Realm.getDefaultInstance();
        uiThreadRealm.close();
        Intent intent = new Intent();
        intent.setClass(StartupActivity.this, MainMenuActivity.class);
        startActivity(intent);

      } else {
        Log.e("QUICKSTART", "Failed to log in. Error: " + result.getError());
        Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
        System.exit(0);
      }
    });
  }


  //close realm and log out from mongodb
  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (uiThreadRealm != null) {
      uiThreadRealm.close();
    }
    app.currentUser().logOutAsync(result -> {
      if (result.isSuccess()) {
        Log.v("QUICKSTART", "Successfully logged out.");
      } else {
        Log.e("QUICKSTART", "Failed to log out, error: " + result.getError());
      }
    });
  }
}