package com.example.team8project;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import io.realm.Realm;

public class SecurityActivity extends AppCompatActivity {
  private loginPreferences session;
  private String username;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_security);

    //load toolbar
    Toolbar myToolbar = findViewById(R.id.my_toolbar);
    setSupportActionBar(myToolbar);
    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);

    EditText question = findViewById(R.id.tf_security);
    Button save = findViewById(R.id.btn_confirm_question);
    save.setOnClickListener(view -> {
      String text = question.getText().toString();
      if(text.equals("")) {
        return;
      }

      //open a realm and find logged in user
      session = new loginPreferences(getApplicationContext());
      username = session.getusername();
      Realm realm = Realm.getDefaultInstance();
      realm.executeTransaction(transactionRealm-> {
        Users current = transactionRealm.where(Users.class).equalTo("_id", username).findFirst();
        current.setSecurityAnswer(text);
      });

      Toast.makeText(getApplicationContext(), getString(R.string.security_success), Toast.LENGTH_LONG).show();
      Intent intent = new Intent();
      intent.setClass(SecurityActivity.this, WelcomeActivity.class);
      startActivity(intent);
    });
  }
}