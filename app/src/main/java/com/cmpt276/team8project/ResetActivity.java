package com.cmpt276.team8project;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import io.realm.Realm;

public class ResetActivity extends AppCompatActivity {

  private Realm realm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reset);

    //load toolbar
    Toolbar myToolbar = findViewById(R.id.toolbar_reset);
    setSupportActionBar(myToolbar);
    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);

    Realm realm = Realm.getDefaultInstance();

    //listener on reset password button
    Button save = findViewById(R.id.btn_confirm_change_pass);
    save.setOnClickListener(view -> {

      //declare views
      EditText nameField = findViewById(R.id.tf_username);
      EditText passwordField = findViewById(R.id.pf_reset_pass);
      EditText passwordConfirmField = findViewById(R.id.pf_reset_pass_confirm);
      EditText answerField = findViewById(R.id.tf_answer);

      String name = nameField.getText().toString();
      String password = passwordField.getText().toString();
      String passwordConfirm = passwordConfirmField.getText().toString();
      String answer = answerField.getText().toString();

      //check if given username exists
      Users current = realm.where(Users.class).equalTo("_id", name).findFirst();
      if (current == null) {
        Dialogs.buildDialog(getString(R.string.user_failed), this);
        return;
      }

      //check if current has a security question associated with it
      if (!current.securityExists()) {
        Dialogs.buildDialog(getString(R.string.security_missing), this);
        return;
      }

      //check if given password is null
      if (password.equals("")) {
        return;
      }

      //check if the 2 passwords given are the same
      if (!password.equals(passwordConfirm)) {
        Dialogs.buildDialog(getString(R.string.different_password), this);
        return;
      }

      //check if security question answered correctly
      if (!current.checkSecurityAnswer(answer)) {
        Dialogs.buildDialog(getString(R.string.security_failed), this);
        return;
      }

      //if passes all checks, open up realm for final check
      realm.executeTransaction(transactionRealm -> {
        Users temp = transactionRealm.where(Users.class).equalTo("_id", name)
            .findFirst();

        //check if password has 7 characters, a number, and a capital letter
        if (temp.updatePassword(password)) {
          Dialogs.intentDialog(getString(R.string.password_success), this, LoginActivity.class);
        } else {
          Dialogs.buildDialog(getString(R.string.account_fail), this);
        }
      });
    });
  }
}