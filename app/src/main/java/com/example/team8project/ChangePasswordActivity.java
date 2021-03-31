package com.example.team8project;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import io.realm.Realm;
import io.realm.mongodb.User;

public class ChangePasswordActivity extends AppCompatActivity {

  private Users current;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_change_password);
    //create toolbar
    Toolbar myToolbar = findViewById(R.id.my_toolbar);
    setSupportActionBar(myToolbar);
    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);

    //get user
    loginPreferences session = new loginPreferences(getApplicationContext());
    String username = session.getusername();
    Realm realm = Realm.getDefaultInstance();
    current = realm.where(Users.class).equalTo("_id", username).findFirst();
    realm.close();

    Button mButton = findViewById(R.id.btn_confirm_password);
    mButton.setOnClickListener(view -> {
      TextView oldPass = findViewById(R.id.pf_old_password);
      TextView newPass = findViewById(R.id.pf_new_password);
      TextView newPassAgain = findViewById(R.id.pf_new_password_again);
      String oldPassword = oldPass.getText().toString();
      String newPassword = newPass.getText().toString();
      String newPasswordAgain = newPassAgain.getText().toString();
      if (current.checkPassword(oldPassword)) {
        if (newPassword.equals(newPasswordAgain)) {
          realm.executeTransaction(transactionRealm -> {
            Users temp = transactionRealm.where(Users.class).equalTo("_id", current.getUserName())
                .findFirst();
            if (temp.updatePassword(newPassword)) {
              Toast.makeText(getApplicationContext(), getString(R.string.password_success),
                  Toast.LENGTH_LONG).show();
              Intent intent = new Intent();
              intent.setClass(ChangePasswordActivity.this, SettingsActivity.class);
              startActivity(intent);
            } else {
              Toast.makeText(getApplicationContext(), getString(R.string.account_fail),
                  Toast.LENGTH_SHORT).show();
            }
          });
        } else {
          Toast.makeText(getApplicationContext(),
              getString(R.string.different_password),
              Toast.LENGTH_LONG).show();
        }
      } else {
        Toast.makeText(getApplicationContext(), getString(R.string.credentials_incorrect),
            Toast.LENGTH_LONG).show();
      }
    });
  }
}