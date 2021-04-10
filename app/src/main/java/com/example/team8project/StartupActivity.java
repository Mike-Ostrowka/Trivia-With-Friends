package com.example.team8project;

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
                SyncConfiguration config = new SyncConfiguration.Builder(
                        user,
                        PARTITION_VALUE).allowWritesOnUiThread(true).allowQueriesOnUiThread(true).build();
                Realm.setDefaultConfiguration(config);
                uiThreadRealm = Realm.getDefaultInstance();
//        addChangeListenerToRealm(uiThreadRealm);
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

    //  //change listener on local realm
//  private void addChangeListenerToRealm(Realm realm) {
//    // all tasks in the realm
//    RealmResults<Users> tasks = realm.where(Users.class).findAllAsync();
//    tasks.addChangeListener((collection, changeSet) -> {
//      // process deletions in reverse order if maintaining parallel data structures so indices don't change as you iterate
//      OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
//      for (OrderedCollectionChangeSet.Range range : deletions) {
//        Log.v("QUICKSTART",
//            "Deleted range: " + range.startIndex + " to " + (range.startIndex + range.length
//                - 1));
//      }
//      OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
//      for (OrderedCollectionChangeSet.Range range : insertions) {
//        Log.v("QUICKSTART",
//            "Inserted range: " + range.startIndex + " to " + (range.startIndex + range.length
//                - 1));
//      }
//      OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
//      for (OrderedCollectionChangeSet.Range range : modifications) {
//        Log.v("QUICKSTART",
//            "Updated range: " + range.startIndex + " to " + (range.startIndex + range.length
//                - 1));
//      }
//    });
//  }
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