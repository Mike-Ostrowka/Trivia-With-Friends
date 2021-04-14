package com.cmpt276.team8project;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import io.realm.Realm;

public class SecurityActivity extends AppCompatActivity {

  private loginPreferences session;
  private String username;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_security);

    //load toolbar
    Toolbar myToolbar = findViewById(R.id.toolbar_security);
    setSupportActionBar(myToolbar);
    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);

    //load views
    EditText question = findViewById(R.id.tf_security);
    Button save = findViewById(R.id.btn_confirm_question);

    //click listener on save button
    save.setOnClickListener(view -> {
      String text = question.getText().toString();

      //if answer is blank, return
      if (text.equals("")) {
        return;
      }

      //open a realm and find logged in user
      session = new loginPreferences(getApplicationContext());
      username = session.getUsername();
      Realm realm = Realm.getDefaultInstance();

      //set security answer
      realm.executeTransaction(transactionRealm -> {
        Users current = transactionRealm.where(Users.class).equalTo("_id", username).findFirst();
        current.setSecurityAnswer(text);
      });
      Dialogs.intentDialog(getString(R.string.security_success), this, WelcomeActivity.class);
    });
  }
}