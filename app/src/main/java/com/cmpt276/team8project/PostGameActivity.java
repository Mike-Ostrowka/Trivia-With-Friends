package com.cmpt276.team8project;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

    boolean winner = getIntent().getBooleanExtra("Winner", true);
    long currentGameId = getIntent().getLongExtra("GameID", 0);

    //if user lost, convert background to red
    if (!winner) {
      ImageView image = findViewById(R.id.victor_banner);
      View background = findViewById(R.id.post_game_layout);
      image.setBackgroundResource(R.drawable.image_defeat);
      background.setBackgroundColor(getResources().getColor(R.color.red));
    }

    //get current user and game from realm
    realm = Realm.getDefaultInstance();
    loginPreferences session = new loginPreferences(getApplicationContext());
    String username = session.getUsername();
    currentUser = realm.where(Users.class).equalTo("_id", username).findFirst();
    currentGame = realm.where(Game.class).equalTo("_id", currentGameId).findFirst();

    TextView playerOneScore = findViewById(R.id.player_one_score);
    TextView playerTwoScore = findViewById(R.id.player_two_score);

    //check if User is player one or two and set score
    if (currentGame.getPlayerOne().equals(currentUser.getUserName())) {
      playerOneText = getString(R.string.user_score) + "\t" + currentGame.getPlayerOneScore();
      playerTwoText = getString(R.string.enemy_score) + "\t" + currentGame.getPlayerTwoScore();
    } else {
      playerOneText = getString(R.string.user_score) + "\t" + currentGame.getPlayerTwoScore();
      playerTwoText = getString(R.string.enemy_score) + "\t" + currentGame.getPlayerOneScore();
    }

    playerOneScore.setText(playerOneText);
    playerTwoScore.setText(playerTwoText);

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

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (realm != null) {
      realm.close();
    }
  }
}