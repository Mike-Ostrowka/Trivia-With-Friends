package com.cmpt276.team8project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.huhx0015.hxaudio.audio.HXMusic;
import io.realm.Realm;

public class PostGameActivity extends AppCompatActivity {

  private Realm realm;
  private Game currentGame;
  private Users currentUser;
  private String playerOneText;
  private String playerTwoText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_post_game);

    //load toolbar
    Toolbar myToolbar = findViewById(R.id.toolbar_post_game);
    setSupportActionBar(myToolbar);
    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);
    ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.green)));

    //load preferences
    PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences, false);
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    boolean soundSwitch = sharedPref.getBoolean(SettingsActivity.KEY_PREF_SOUND_SWITCH, false);
    //if switch value is false, disable music
    if (soundSwitch) {
      playMusic();
    }

    boolean winner = getIntent().getBooleanExtra("Winner", true);
    long currentGameId = getIntent().getLongExtra("GameID", 0);

    //if user lost, convert background to red
    if (!winner) {
      ImageView image = findViewById(R.id.victor_banner);
      View background = findViewById(R.id.post_game_layout);
      image.setImageResource(0);
      image.setBackgroundResource(R.drawable.image_defeat);
      background.setBackgroundColor(getResources().getColor(R.color.red));
      ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red)));
    }

    //get current user and game from realm
    realm = Realm.getDefaultInstance();
    loginPreferences session = new loginPreferences(getApplicationContext());
    String username = session.getUsername();
    currentUser = realm.where(Users.class).equalTo("_id", username).findFirst();
    currentGame = realm.where(Game.class).equalTo("_id", currentGameId).findFirst();

    TextView playerOneScore = findViewById(R.id.player_one_score);
    TextView playerTwoScore = findViewById(R.id.player_two_score);
    TextView elo = findViewById(R.id.elo_updated);

    //check if User is player one or two and set score
    if (currentGame.getPlayerOne().equals(currentUser.getUserName())) {
      playerOneText = getString(R.string.user_score) + "\t" + currentGame.getPlayerOneScore();
      playerTwoText = currentGame.getPlayerTwo() + getString(R.string.apostrophe)  +" score\t" + currentGame.getPlayerTwoScore();
    } else {
      playerOneText = getString(R.string.user_score) + "\t" + currentGame.getPlayerTwoScore();
      playerTwoText = currentGame.getPlayerOne() + getString(R.string.apostrophe)  +" score\t" + currentGame.getPlayerOneScore();
    }

    String userElo = getString(R.string.user_elo) + "\t" + currentUser.getElo();

    playerOneScore.setText(playerOneText);
    playerTwoScore.setText(playerTwoText);
    elo.setText(userElo);

    //check if player is null
    if(currentGame.getPlayerOne() == null || currentGame.getPlayerTwo() == null) {
      playerTwoScore.setText("");
      elo.setText(getString(R.string.single_player_elo));
    }

    //button click for play again
    Button playAgain = findViewById(R.id.btn_play_again);
    playAgain.setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.setClass(PostGameActivity.this, GameActivity.class);
      startActivity(intent);
    });

    //button click for main menu
    Button mainMenu = findViewById(R.id.btn_to_welcome);
    mainMenu.setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.setClass(PostGameActivity.this, WelcomeActivity.class);
      startActivity(intent);
    });
  }

  private void playMusic() {
    int song = R.raw.menu;
    HXMusic.music().load(song).gapless(true).looped(true).play(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (realm != null) {
      realm.close();
    }
    HXMusic.stop();
    HXMusic.clear();
  }
}