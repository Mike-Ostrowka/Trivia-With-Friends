package com.example.team8project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;

public class LoginActivity extends AppCompatActivity {


  private Realm realm; //declare realm variable

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    Button mButton = findViewById(R.id.login_button);
    mButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        EditText nameEdit = findViewById(R.id.loginUsername);
        EditText passwordEdit = findViewById(R.id.loginPassword);
        final String name = nameEdit.getText().toString();
        final String password = passwordEdit.getText().toString();

        //open a realm and check if user name exists in database
        try {
          realm = Realm.getDefaultInstance();

          if (realm.where(Users.class).equalTo("userName", name).findFirst() != null) {
            //create temp for user to check password
            final Users currentUser = realm.where(Users.class).equalTo("userName", name)
                .findFirst();
            if (!currentUser.checkPassword(password)) {
              realm.executeTransaction(transactionRealm -> {
                Users temp = transactionRealm.where(Users.class).equalTo("userName", currentUser.getUserName()).findFirst();
                temp.setLogin();
              });

              Intent intent = new Intent();
              intent.setClass(LoginActivity.this, WelcomeActivity.class);
              startActivity(intent);
            }
          } else {
            Toast.makeText(getApplicationContext(), "Credentials are incorrect, please try again",
                Toast.LENGTH_LONG).show();

          }


        } finally {
          if (realm != null) {
            realm.close();
          }
        }
//        Toast.makeText(getApplicationContext(), getString(R.string.account_success) + name, Toast.LENGTH_LONG).show();
//        Intent intent = new Intent();
//        intent.setClass(NewAccountActivity.this,LoginActivity.class);
//        startActivity(intent);
      }
    });
  }
}