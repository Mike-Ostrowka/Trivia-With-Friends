package com.example.team8project;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import io.realm.Realm;

public class ResetActivity extends AppCompatActivity {

  private Realm realm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reset);

    //load toolbar
    Toolbar myToolbar = findViewById(R.id.my_toolbar);
    setSupportActionBar(myToolbar);
    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);

    Realm realm = Realm.getDefaultInstance();

    Button save = findViewById(R.id.btn_confirm_change_pass);
    save.setOnClickListener(view -> {
      EditText nameField = findViewById(R.id.tf_username);
      EditText passwordField = findViewById(R.id.pf_reset_pass);
      EditText passwordConfirmField = findViewById(R.id.pf_reset_pass_confirm);
      EditText answerField = findViewById(R.id.tf_answer);

      String name = nameField.getText().toString();
      String password = passwordField.getText().toString();
      String passwordConfirm = passwordConfirmField.getText().toString();
      String answer = answerField.getText().toString();

      Users current = realm.where(Users.class).equalTo("_id", name).findFirst();
      if (current == null) {
        Toast.makeText(getApplicationContext(), getString(R.string.user_failed), Toast.LENGTH_LONG)
            .show();
        return;
      }
      if (!current.securityExists()) {
        Toast.makeText(getApplicationContext(), getString(R.string.security_missing),
            Toast.LENGTH_LONG).show();
        return;
      }
      if (password.equals("")) {
        return;
      }
      if (!password.equals(passwordConfirm)) {
        Toast.makeText(getApplicationContext(), getString(R.string.different_password),
            Toast.LENGTH_LONG).show();
        return;
      }
      if (!current.checkSecurityAnswer(answer)) {
        Toast.makeText(getApplicationContext(), getString(R.string.security_failed),
            Toast.LENGTH_LONG).show();
        return;
      }
      realm.executeTransaction(transactionRealm -> {
        Users temp = transactionRealm.where(Users.class).equalTo("_id", name)
            .findFirst();
        if (temp.updatePassword(password)) {
          Toast.makeText(getApplicationContext(), getString(R.string.password_success),
              Toast.LENGTH_LONG).show();
          realm.close();
          Intent intent = new Intent();
          intent.setClass(ResetActivity.this, LoginActivity.class);
          startActivity(intent);
        } else {
          Toast.makeText(getApplicationContext(), getString(R.string.account_fail),
              Toast.LENGTH_SHORT).show();
        }
      });
    });
  }
}