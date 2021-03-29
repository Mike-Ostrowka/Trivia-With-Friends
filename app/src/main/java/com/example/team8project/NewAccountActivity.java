package com.example.team8project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import io.realm.Realm;

public class NewAccountActivity extends AppCompatActivity {

  private Realm realm; //declare realm variable
  private Users temp; //temp user to register new account

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_account);
    Button mButton = findViewById(R.id.registerButton);
    mButton.setOnClickListener(v -> {
      EditText nameEdit = findViewById(R.id.registerName);
      EditText passwordEdit = findViewById(R.id.registerPassword);
      String name = nameEdit.getText().toString();
      String password = passwordEdit.getText().toString();

      //open a realm
      realm = Realm.getDefaultInstance();
      Users nameExists = realm.where(Users.class).equalTo("_id", name).findFirst();

      if (nameExists != null) {
        Toast.makeText(getApplicationContext(), getString(R.string.account_exists),
            Toast.LENGTH_SHORT).show();
        return;
      }
      temp = new Users(name, "");
      if (!temp.updatePassword(password)) {
        Toast.makeText(getApplicationContext(), getString(R.string.account_fail),
            Toast.LENGTH_SHORT).show();
        return;
      }
      //save temp to realm

        realm.executeTransaction(transactionRealm -> transactionRealm.insert(temp));
        if (realm != null) {
          realm.close();
        }
      Toast.makeText(getApplicationContext(), getString(R.string.account_success) + name,
          Toast.LENGTH_LONG).show();
      Intent intent = new Intent();
      intent.setClass(NewAccountActivity.this, LoginActivity.class);
      startActivity(intent);
    });


  }


}