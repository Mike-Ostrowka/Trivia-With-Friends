package com.example.team8project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

public class SettingsActivity  extends AppCompatActivity {
  public static final String KEY_PREF_SOUND_SWITCH = "sound_enabled";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    //load toolbar
    Toolbar myToolbar = findViewById(R.id.my_toolbar);
    setSupportActionBar(myToolbar);
    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);
    getSupportFragmentManager().beginTransaction()
        .replace(android.R.id.content, new SettingsFragment())
        .commit();
    Button mButton = findViewById(R.id.btn_change_password);
    mButton.setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.setClass(SettingsActivity.this, ChangePasswordActivity.class);
      startActivity(intent);
    });
  }
}