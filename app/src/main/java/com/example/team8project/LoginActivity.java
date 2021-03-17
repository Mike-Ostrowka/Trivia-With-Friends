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

          realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

              if(realm.where(Users.class).equalTo("userName", name).findFirst() != null){
                Toast.makeText(getApplicationContext(), "Congrats it works" + name, Toast.LENGTH_LONG).show();

                //todo add logic to check password matches for user
              }
              else{
                Toast.makeText(getApplicationContext(), "Does Not Exist" + name, Toast.LENGTH_LONG).show();
                //add logic for inccorect login information
              }

            }
          });

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