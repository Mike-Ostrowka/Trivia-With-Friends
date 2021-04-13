package com.example.team8project;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import io.realm.Realm;
import java.util.UUID;


public class GameActivity extends AppCompatActivity {

  private final boolean gameFinished = false;
  int questionCount = 0;
  int playerScore = 0, playerTwoScore = 0;
  long _ID = UUID.randomUUID().getMostSignificantBits();

  //declaring all of the layout objects
  Button answerOneBtn, answerTwoBtn, answerThreeBtn, answerFourBtn;
  TextView questionTextView, playerScoreText, playerTwoText;
  // Player playerTwo;

  //declaring current game, handler for rounds, and player one and two


  Handler gameHandler = new Handler();
  Handler mainGameHandler = new Handler();


  Realm realm;
  Game currentGame;
  LoadQuestions loadQuestions;
  Runnable postGameRunnable = new Runnable() {
    @Override
    public void run() {

      Toast.makeText(GameActivity.this, "The Game Has Completed", Toast.LENGTH_LONG).show();
      realm.executeTransaction(new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
          currentGame.setGameCompleted(true);
          realm.insertOrUpdate(currentGame);
        }
      });

      if (realm != null) {
        realm.close();
      }


    }

  };
  Runnable gameRunnable = new Runnable() {
    @Override
    public void run() {

      playGame();
      answerOneBtn.setClickable(true);
      answerTwoBtn.setClickable(true);
      answerThreeBtn.setClickable(true);
      answerFourBtn.setClickable(true);


    }
  };
  Runnable mainGameRunnable = new Runnable() {
    @Override
    public void run() {

      for (int i = 0; i <= 9; i++) {
        gameHandler.postDelayed(gameRunnable, 5000 * i);
      }
      gameHandler.postDelayed(postGameRunnable, 50000);
    }
  };
  private Users current;
  private loginPreferences session;
  private String username;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_game);

    //load realm
    loadRealm();

    //INSTANTIATE OBJECTS FOR USE
    gameHandler = new Handler();
    loadQuestions = new LoadQuestions();
    loadQuestions.questionList.AnswersJumbled();

    //INSTANTIATE BUTTONS AND TEXT VIEWS
    answerOneBtn = findViewById(R.id.AnswerOneButton);
    answerTwoBtn = findViewById(R.id.AnswerTwoButton);
    answerThreeBtn = findViewById(R.id.AnswerThreeButton);
    answerFourBtn = findViewById(R.id.AnswerFourButton);
    questionTextView = findViewById(R.id.questionText);
    playerScoreText = findViewById(R.id.playerScore);
    playerTwoText = findViewById(R.id.playerTwoScore);

    answerOneBtn.setOnClickListener(v -> {

      loadQuestions.playerOneSelection = answerOneBtn.getText().toString();
      playerScore += loadQuestions.checkPlayerAnswer(loadQuestions.playerOneSelection);

      answerOneBtn.setClickable(false);
      answerTwoBtn.setClickable(false);
      answerThreeBtn.setClickable(false);
      answerFourBtn.setClickable(false);


    });

    answerTwoBtn.setOnClickListener(v -> {

      loadQuestions.playerOneSelection = answerTwoBtn.getText().toString();

      playerScore += loadQuestions.checkPlayerAnswer(loadQuestions.playerOneSelection);

      answerOneBtn.setClickable(false);
      answerTwoBtn.setClickable(false);
      answerThreeBtn.setClickable(false);
      answerFourBtn.setClickable(false);


    });

    answerThreeBtn.setOnClickListener(v -> {

      loadQuestions.playerOneSelection = answerThreeBtn.getText().toString();
      playerScore += loadQuestions.checkPlayerAnswer(loadQuestions.playerOneSelection);
      answerOneBtn.setClickable(false);
      answerTwoBtn.setClickable(false);
      answerThreeBtn.setClickable(false);
      answerFourBtn.setClickable(false);


    });

    answerFourBtn.setOnClickListener(v -> {

      loadQuestions.playerOneSelection = answerFourBtn.getText().toString();
      playerScore += loadQuestions.checkPlayerAnswer(loadQuestions.playerOneSelection);

      answerOneBtn.setClickable(false);
      answerTwoBtn.setClickable(false);
      answerThreeBtn.setClickable(false);
      answerFourBtn.setClickable(false);

    });


  }

  @Override
  protected void onStart() {
    super.onStart();

    mainGameHandler.postDelayed(mainGameRunnable, 0);

  }

  private void loadRealm() {
    //open a realm and find logged in user
    session = new loginPreferences(getApplicationContext());
    username = session.getusername();
    if (realm == null) {
      realm = Realm.getDefaultInstance();
    }
    current = realm.where(Users.class).equalTo("_id", username).findFirst();

    //check for game or create game
    if (realm.where(Game.class).equalTo("playerCount", 1).findFirst() != null) {

      /*
       * On Game Load we need player one info, game id, question count
       * Must update player count to 2
       * Game should start only once player count == 2
       * */
      currentGame = realm.where(Game.class).equalTo("playerCount", 1).findFirst();
      realm.executeTransaction(new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
          currentGame.setPlayerTwo(username);
          questionCount = currentGame.getQuestionCount();
          currentGame.setPlayerCount(2);
          realm.copyToRealmOrUpdate(currentGame);
        }
      });


    } else {
      currentGame = new Game(username, _ID, 1);
    }


  }

  @Override
  protected void onPause() {
    super.onPause();
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        currentGame.setGameCompleted(true);
        realm.insertOrUpdate(currentGame);
      }
    });

    if (realm != null) {
      realm.close();
    }

    gameHandler.removeCallbacks(gameRunnable);
    gameHandler.removeCallbacks(postGameRunnable);


  }

  public void playGame() {

    answerOneBtn = findViewById(R.id.AnswerOneButton);
    answerTwoBtn = findViewById(R.id.AnswerTwoButton);
    answerThreeBtn = findViewById(R.id.AnswerThreeButton);
    answerFourBtn = findViewById(R.id.AnswerFourButton);
    questionTextView = findViewById(R.id.questionText);
    playerScoreText.setText(currentGame.getPlayerOne() + " " + currentGame.getPlayerOneScore());
    playerTwoText.setText(currentGame.getPlayerTwo() + " " + currentGame.getPlayerTwoScore());

    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        currentGame.setPlayerOneScore(playerScore);
        currentGame.setPlayerTwoScore(playerTwoScore);
        realm.insertOrUpdate(currentGame);
      }
    });

    loadQuestions.loadQuestion(questionCount);
    questionTextView.setText(loadQuestions.currentQuestion);
    answerOneBtn.setText(loadQuestions.firstAnswer);
    answerTwoBtn.setText(loadQuestions.secondAnswer);
    answerThreeBtn.setText(loadQuestions.thirdAnswer);
    answerFourBtn.setText(loadQuestions.fourthAnswer);

    questionCount++;


  }

}
