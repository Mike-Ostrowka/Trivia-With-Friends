package com.example.team8project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.huhx0015.hxaudio.audio.HXSound;
import io.realm.Realm;
import java.util.ArrayList;
import java.util.Random;

public class NewAccountActivity extends AppCompatActivity {

  private Realm realm; //declare realm variable
  private Users temp; //temp user to register new account
  private int click_sound;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    click_sound = R.raw.click;

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_account);
    Button mButton = findViewById(R.id.registerButton);

    mButton.setOnClickListener(v -> {

      HXSound.sound().load(click_sound).play(this);
      EditText nameEdit = findViewById(R.id.registerName);
      EditText passwordEdit = findViewById(R.id.registerPassword);

      // get inputted user name and password
      String name = nameEdit.getText().toString();
      String password = passwordEdit.getText().toString();

      //open a realm
      if (realm == null) {
        realm = Realm.getDefaultInstance();
      }
      Users nameExists = realm.where(Users.class).equalTo("_id", name).findFirst();

      // inform player if username is already taken
      if (nameExists != null) {
        Toast.makeText(getApplicationContext(), getString(R.string.account_exists),
            Toast.LENGTH_SHORT).show();
        return;
      }

      // ensure that selected username does not contain banned words
      BadWordFilter vulgarityCheck = new BadWordFilter();
      if (vulgarityCheck.isBannedWordUsed(name)) {
        Toast.makeText(getApplicationContext(), getString(R.string.user_name_banned),
            Toast.LENGTH_SHORT).show();
        return;
      }

      // ensure that password meets requirements
      temp = new Users(name);
      if (!temp.updatePassword(password)) {
        Toast.makeText(getApplicationContext(), getString(R.string.account_fail),
            Toast.LENGTH_SHORT).show();
        return;
      }

      // array with all of the available channel keys
      ArrayList<String> channelKeys = new ArrayList<String>() {{
        add("J7Ws0kKbptiDV3NH");
        add("HWAaPjyiYzRc1g1o");
        add("vitEgpZ301qBAzZS");
        add("cIIbNj2NtmhYLh1V");
        add("pu9hSudeD0Lar5NF");
        add("6YCDMRfk7Lh4hAgQ");
        add("jfN3f6BA4wtajEmf");
        add("0pps9KPD9hfU6fwH");
        add("hEzXoOyPIUek28it");
        add("Q8HhAkYQT6pijYDe");
        add("SOipt35X1ytKfgd2");
        add("zaeSZnD7swcBn2Mi");
        add("2QY63czNUgstrQkz");
        add("HbGUWLxCKTcIZBuI");
        add("97HQx9IpcFJ4b8Hz");
        add("ffPc9lcFVxzibdnN");
        add("pIGzbQKFtNumFP3W");
        add("hbOQsIYf35BmD9RY");
        add("dDZd7lhPWuRcxntI");
        add("iLTsCTpWUEcNpMwy");
      }};

      // assign a random channel key to a user upon account creation
      // multiple users may share a key to allow players to socialize with on another
      Random random = new Random();
      int randomKey = random.nextInt(20);
      temp.setChannelKey(channelKeys.get(randomKey));

      // save User to realm database
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

    //Set the event for Text View Already have an account
    TextView mTextview = findViewById(R.id.tv_have_account);
    mTextview.setOnClickListener(v -> {
      HXSound.sound().load(click_sound).play(this);
      Intent intent = new Intent();
      intent.setClass(NewAccountActivity.this, LoginActivity.class);
      startActivity(intent);
      finish();
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    HXSound.clear();
  }

  @Override
  protected void onPause() {
    super.onPause();
    HXSound.clear();
  }
}