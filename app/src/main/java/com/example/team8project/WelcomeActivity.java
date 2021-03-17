package com.example.team8project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class WelcomeActivity extends AppCompatActivity {
  private Realm realm;
  private Users current;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);

    //open a realm and find logged in user
    try {
      realm = Realm.getDefaultInstance();
      current = realm.where(Users.class).equalTo("userName", "admin").findFirst();


    } finally {
      if (realm != null) {
        realm.close();
      }
    }

    TextView greeting = findViewById(R.id.textViewGreeting);
    String greetingText = getString(R.string.greeting) + current.getUserName();
    greeting.setText(greetingText);

    Button mButton = findViewById(R.id.login_button);
    mButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        current.setLogin();
        Toast.makeText(getApplicationContext(), R.string.logout_message + current.getUserName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setClass(WelcomeActivity.this, MainMenuActivity.class);
        startActivity(intent);
      }
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}
