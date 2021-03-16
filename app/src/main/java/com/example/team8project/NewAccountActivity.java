package com.example.team8project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import io.realm.Realm;

public class NewAccountActivity extends AppCompatActivity {

  private Realm realm; //declare realm variable
  private Users temp; //temp user to register new account

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_account);
    Button mButton = findViewById(R.id.registerButton);
    mButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        EditText nameEdit = findViewById(R.id.registerName);
        EditText passwordEdit = findViewById(R.id.registerPassword);
        String name = nameEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        temp = new Users(name, password);

        //open a realm and save temp to it
        try {
          realm = Realm.getDefaultInstance();

          realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
              realm.copyToRealmOrUpdate(temp);
            }
          });

        } finally {
          if (realm != null) {
            realm.close();
          }
        }

      }
    });


  }

}