package com.example.team8project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.huhx0015.hxaudio.audio.HXSound;

import io.realm.Realm;

public class LoginActivity extends AppCompatActivity {


    private Realm realm; //declare realm variable
    private Users currentUser;
    private loginPreferences session;
    private int click_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        click_sound = R.raw.click;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkLogin();
        Button mButton = findViewById(R.id.login_button);
        mButton.setOnClickListener(v -> {
            HXSound.sound().load(click_sound).play(this);
            EditText nameEdit = findViewById(R.id.loginUsername);
            EditText passwordEdit = findViewById(R.id.loginPassword);
            final String name = nameEdit.getText().toString();
            final String password = passwordEdit.getText().toString();

            //open a realm and check if user name exists in database
            try {
                realm = Realm.getDefaultInstance();

                if (realm.where(Users.class).equalTo("_id", name).findFirst() != null) {
                    //create temp for user to check password
                    currentUser = realm.where(Users.class).equalTo("_id", name).findFirst();
                    if (currentUser == null) {
                        Toast.makeText(getApplicationContext(), "Username does not exist, please try again",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (currentUser.checkPassword(password)) {
                        session = new loginPreferences(getApplicationContext());
                        session.setusername(currentUser.getUserName());

                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Credentials are incorrect, please try again",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "User does not exist, please try again",
                            Toast.LENGTH_LONG).show();

                }


            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        });
        Button forgot = findViewById(R.id.btn_forgot);
        forgot.setOnClickListener(view -> {
            HXSound.sound().load(click_sound).play(this);
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, ResetActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    //check if user is already logged in
    public void checkLogin() {
        session = new loginPreferences(getApplicationContext());
        String username = session.getusername();
        if (!username.equals("")) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }
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