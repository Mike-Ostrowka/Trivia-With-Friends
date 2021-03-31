package com.example.team8project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import io.realm.Realm;
import io.realm.RealmConfiguration;

//todo fix java.lang.IllegalStateException: Cannot modify managed objects outside of a write transaction. at line 34 of this activity


public class WelcomeActivity extends AppCompatActivity {

  private Users current;
  private loginPreferences session;
  private String username;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);

    //open a realm and find logged in user
    session = new loginPreferences(getApplicationContext());
    username = session.getusername();
    Realm realm = Realm.getDefaultInstance();
    current = realm.where(Users.class).equalTo("_id", username).findFirst();

    TextView greeting = findViewById(R.id.textViewGreeting);
    String greetingText = current.getUserName();
    greeting.setText(greetingText);

    Button mButtonLogout = findViewById(R.id.logout_button);
    mButtonLogout.setOnClickListener(v -> {

      session.setusername("");

      Toast.makeText(getApplicationContext(), R.string.logout_message + " " + current.getUserName(),
          Toast.LENGTH_LONG).show();
      realm.close();
      Intent intent = new Intent();
      intent.setClass(WelcomeActivity.this, MainMenuActivity.class);
      startActivity(intent);
    });
    Button mButtonFAQ = findViewById(R.id.btn_settings);
    mButtonFAQ.setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.setClass(WelcomeActivity.this, SettingsActivity.class);
      startActivity(intent);
    });

    Button mButtonSettings = findViewById(R.id.btn_faq);
    mButtonSettings.setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.setClass(WelcomeActivity.this, FaqActivity.class);
      startActivity(intent);
    });
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}

