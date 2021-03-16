package com.example.team8project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class WelcomeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);
    TextView greeting = findViewById(R.id.textViewGreeting);
    greeting.setText(getString(R.string.greeting));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}
