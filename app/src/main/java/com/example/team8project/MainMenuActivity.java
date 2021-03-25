package com.example.team8project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import io.realm.OrderedCollectionChangeSet;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class MainMenuActivity extends AppCompatActivity {

  private Realm uiThreadRealm;
  private Realm realm;
  private App app;
  private User user;
  final private String PARTITION_VALUE = "all";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_menu);
    Realm.init(this);
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    if (!prefs.getBoolean("firstTime", false)) {
      //start realm

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
          SyncConfiguration config = new SyncConfiguration.Builder(
              user,
              PARTITION_VALUE).allowWritesOnUiThread(true).allowQueriesOnUiThread(true).build();
          Realm.setDefaultConfiguration(config);
          uiThreadRealm = Realm.getDefaultInstance();
          addChangeListenerToRealm(uiThreadRealm);
          SharedPreferences.Editor editor = prefs.edit();
          editor.putBoolean("firstTime", true);
          editor.apply();
          checkLogin();
        } else {
          Log.e("QUICKSTART", "Failed to log in. Error: " + result.getError());
          Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
          System.exit(0);
        }
      });
    }
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


  //change listener on local realm
  private void addChangeListenerToRealm(Realm realm) {
    // all tasks in the realm
    RealmResults<Users> tasks = realm.where(Users.class).findAllAsync();
    tasks.addChangeListener((collection, changeSet) -> {
      // process deletions in reverse order if maintaining parallel data structures so indices don't change as you iterate
      OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
      for (OrderedCollectionChangeSet.Range range : deletions) {
        Log.v("QUICKSTART",
            "Deleted range: " + range.startIndex + " to " + (range.startIndex + range.length
                - 1));
      }
      OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
      for (OrderedCollectionChangeSet.Range range : insertions) {
        Log.v("QUICKSTART",
            "Inserted range: " + range.startIndex + " to " + (range.startIndex + range.length
                - 1));
      }
      OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
      for (OrderedCollectionChangeSet.Range range : modifications) {
        Log.v("QUICKSTART",
            "Updated range: " + range.startIndex + " to " + (range.startIndex + range.length
                - 1));
      }
    });
  }


    public void checkLogin() {

//      //Open realm and check if logged in
//      SyncConfiguration config = new SyncConfiguration.Builder(
//          user,
//          PARTITION_VALUE)
//          .build();
//
//      Realm.setDefaultConfiguration(config);
      Realm realm = Realm.getDefaultInstance();

      if (realm.where(Users.class).equalTo("loginStatus", true).findFirst() != null) {
        realm.close();
        Intent intent = new Intent();
        intent.setClass(MainMenuActivity.this, WelcomeActivity.class);
        startActivity(intent);
      }

      //open a realm and save temp to it if it does not exist

      Users defaultExists = realm.where(Users.class).equalTo("_id", "admin").findFirst();

      if (defaultExists == null) {
        //default user
        final Users temp = new Users("admin", "Password123");
        realm.executeTransaction(transactionRealm -> transactionRealm.insert(temp));
      }
      realm.close();
    }



  @Override
  protected void onDestroy() {
    super.onDestroy();
    if(uiThreadRealm != null) {
      uiThreadRealm.close();
    }
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    if (!prefs.getBoolean("firstTime", false)) {
      SharedPreferences.Editor editor = prefs.edit();
      editor.putBoolean("firstTime", false);
      editor.apply();
      checkLogin();
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