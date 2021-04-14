package com.example.team8project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;
import com.huhx0015.hxaudio.audio.HXMusic;
import com.huhx0015.hxaudio.audio.HXSound;
import io.realm.Realm;
import java.util.UUID;


public class GameActivity extends AppCompatActivity {

  private final boolean gameFinished = false;
  int questionCount = 0;
  int playerScore = 0, playerTwoScore = 0;
  String playerOne, playerTwo;
  long _ID = UUID.randomUUID().getMostSignificantBits();
  //player flag, if true player one, if two player two
  boolean setPlayer;
  //declaring all of the layout objects
  Button answerOneBtn, answerTwoBtn, answerThreeBtn, answerFourBtn;
  TextView questionTextView, playerScoreText, playerTwoText;
  Handler gameHandler = new Handler();
  Handler mainGameHandler = new Handler();
  Realm realm;
  Game currentGame;
  LoadQuestions loadQuestions;

  private int correctSound;
  private int wrongSound;
  private int green;
  private int red;
  private int gray;
  private int blue;

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
    correctSound = R.raw.correct;
    wrongSound = R.raw.wrong;
    green = getResources().getColor(R.color.green);
    red = getResources().getColor(R.color.red);
    gray = getResources().getColor(R.color.gray);
    blue = getResources().getColor(R.color.blue);
    int clickSound = R.raw.click;

    answerOneBtn.setOnClickListener(v -> {

      loadQuestions.playerOneSelection = answerOneBtn.getText().toString();
      if (setPlayer == true) {
        playerScore += loadQuestions.checkPlayerAnswer(loadQuestions.playerOneSelection);
      } else if (setPlayer == false) {
        playerTwoScore += loadQuestions.checkPlayerAnswer(loadQuestions.playerOneSelection);
      }
      System.out.println(setPlayer);

      //correct chosen
      if (loadQuestions.checkPlayerAnswer(loadQuestions.playerOneSelection) == 5) {
        answerOneBtn.setBackgroundColor(green);
        HXSound.sound().load(correctSound).play(this);
      } else { //wrong answer
        HXSound.sound().load(wrongSound).play(this);
        setCorrectColor();
        answerOneBtn.setBackgroundColor(red);
      }


    });

    answerTwoBtn.setOnClickListener(v -> {

      loadQuestions.playerOneSelection = answerTwoBtn.getText().toString();
      if (setPlayer == true) {
        playerScore += loadQuestions.checkPlayerAnswer(loadQuestions.playerOneSelection);
      } else if (setPlayer == false) {
        playerTwoScore += loadQuestions.checkPlayerAnswer(loadQuestions.playerOneSelection);
      }
      System.out.println(setPlayer);

      //correct chosen
      if (loadQuestions.checkPlayerAnswer(loadQuestions.playerOneSelection) == 5) {
        answerTwoBtn.setBackgroundColor(green);
        HXSound.sound().load(correctSound).play(this);
      } else { //wrong answer
        HXSound.sound().load(wrongSound).play(this);
        setCorrectColor();
        answerTwoBtn.setBackgroundColor(red);
      }


    });

    answerThreeBtn.setOnClickListener(v -> {

      loadQuestions.playerOneSelection = answerThreeBtn.getText().toString();
      if (setPlayer == true) {
        playerScore += loadQuestions.checkPlayerAnswer(loadQuestions.playerOneSelection);
      } else if (setPlayer == false) {
        playerTwoScore += loadQuestions.checkPlayerAnswer(loadQuestions.playerOneSelection);
      }
      System.out.println(setPlayer);

      //correct chosen
      if (loadQuestions.checkPlayerAnswer(loadQuestions.playerOneSelection) == 5) {
        answerThreeBtn.setBackgroundColor(green);
        HXSound.sound().load(correctSound).play(this);
      } else { //wrong answer
        HXSound.sound().load(wrongSound).play(this);
        setCorrectColor();
        answerThreeBtn.setBackgroundColor(red);
      }


    });

    answerFourBtn.setOnClickListener(v -> {

      loadQuestions.playerOneSelection = answerFourBtn.getText().toString();
      if (setPlayer == true) {
        playerScore += loadQuestions.checkPlayerAnswer(loadQuestions.playerOneSelection);
      } else if (setPlayer == false) {
        playerTwoScore += loadQuestions.checkPlayerAnswer(loadQuestions.playerOneSelection);
      }
      System.out.println(setPlayer);
      //correct chosen
      if (loadQuestions.checkPlayerAnswer(loadQuestions.playerOneSelection) == 5) {
        answerFourBtn.setBackgroundColor(green);
        HXSound.sound().load(correctSound).play(this);
      } else {
        HXSound.sound().load(wrongSound).play(this);
        setCorrectColor();
        answerFourBtn.setBackgroundColor(red);
      }


    });


  }

  @Override
  public void onResume() {
    super.onResume();

    //load preferences
    PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences, false);
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    boolean soundSwitch = sharedPref.getBoolean(SettingsActivity.KEY_PREF_SOUND_SWITCH, false);
    //if switch value is false, disable music
    if (soundSwitch) {
      playMusic();
    }

  }

  @Override
  protected void onStart() {
    super.onStart();
    playerOne = currentGame.getPlayerOne();
    playerTwo = currentGame.getPlayerTwo();
    mainGameHandler.postDelayed(mainGameRunnable, 0);


  }

  Runnable postGameRunnable = new Runnable() {
    @Override
    public void run() {
      HXSound.sound().load(R.raw.time_over).play(GameActivity.this);
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
      Dialogs.intentDialog("The game has ended", GameActivity.this, WelcomeActivity.class);

    }

  };

  //declaring current game, handler for rounds, and player one and two
  Runnable mainGameRunnable = new Runnable() {
    @Override
    public void run() {

      for (int i = 0; i <= 9; i++) {
        gameHandler.postDelayed(gameRunnable, 5000 * i);
      }
      gameHandler.postDelayed(postGameRunnable, 50000);
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
      answerOneBtn.setBackgroundColor(blue);
      answerTwoBtn.setBackgroundColor(blue);
      answerThreeBtn.setBackgroundColor(blue);
      answerFourBtn.setBackgroundColor(blue);


    }
  };

  private void loadRealm() {
    //open a realm and find logged in user
    session = new loginPreferences(getApplicationContext());
    username = session.getusername();
    if (realm == null) {
      realm = Realm.getDefaultInstance();
    }

    //check for game or create game
    if (realm.where(Game.class).equalTo("playerCount", 1).equalTo("gameCompleted", false).findFirst() != null) {

      //if game with single player and not finished(this means player is in lobby waiting for second player
      //set this current player to false which flags it as second player
      //get current game that matches open game criteria
      //
      setPlayer = false;
      currentGame = realm.where(Game.class).equalTo("playerCount", 1).equalTo("gameCompleted", false).findFirst();
      realm.executeTransaction(new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
          currentGame.setPlayerTwo(username);
          currentGame.setPlayerCount(2);
          playerTwo = username;
          playerOne = currentGame.getPlayerOne();
          realm.copyToRealmOrUpdate(currentGame);
        }
      });
      _ID = currentGame.get_id();
    } else {
      setPlayer = true;
      currentGame = new Game(username, _ID, 1);
      currentGame.setPlayerOne(username);
    }

    System.out.println(currentGame.getPlayerOneScore());
    System.out.println(currentGame.getPlayerTwoScore());
    System.out.println(currentGame.getPlayerOne());
    System.out.println(currentGame.getPlayerTwo());

  }

  @Override
  protected void onPause() {
    super.onPause();
    HXMusic.stop();
    HXMusic.clear();
    if (realm == null) {
      realm = Realm.getDefaultInstance();
    }
    realm.executeTransaction(realm -> {
      currentGame.setGameCompleted(true);
      realm.insertOrUpdate(currentGame);
    });

    if (realm != null) {
      realm.close();
    }
    System.out.println(currentGame.get_id());
    gameHandler.removeCallbacks(gameRunnable);
    gameHandler.removeCallbacks(postGameRunnable);


  }

  public void playGame() {

    answerOneBtn = findViewById(R.id.AnswerOneButton);
    answerTwoBtn = findViewById(R.id.AnswerTwoButton);
    answerThreeBtn = findViewById(R.id.AnswerThreeButton);
    answerFourBtn = findViewById(R.id.AnswerFourButton);
    questionTextView = findViewById(R.id.questionText);


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

    playerScoreText.setText(playerOne + " " + currentGame.getPlayerOneScore());
    playerTwoText.setText(playerTwo + " " + currentGame.getPlayerTwoScore());

    questionCount++;

    System.out.println(currentGame.getPlayerOneScore());
    System.out.println(currentGame.getPlayerTwoScore());
    System.out.println(currentGame.getPlayerOne());
    System.out.println(currentGame.getPlayerTwo());

  }

  private void playMusic() {
    int song = R.raw.game_music;
    HXMusic.music().load(song).gapless(true).looped(true).play(this);
  }

  public int correctAnswer() {
    String answerOne = answerOneBtn.getText().toString();
    String answerTwo = answerTwoBtn.getText().toString();
    String answerThree = answerThreeBtn.getText().toString();
    String answerFour = answerFourBtn.getText().toString();
    if (loadQuestions.checkPlayerAnswer(answerOne) == 5) {
      return 1;
    }
    if (loadQuestions.checkPlayerAnswer(answerTwo) == 5) {
      return 2;
    }
    if (loadQuestions.checkPlayerAnswer(answerThree) == 5) {
      return 3;
    }
    if (loadQuestions.checkPlayerAnswer(answerFour) == 5) {
      return 4;
    }
    return 0;
  }

  public void setCorrectColor() {
    answerOneBtn.setClickable(false);
    answerTwoBtn.setClickable(false);
    answerThreeBtn.setClickable(false);
    answerFourBtn.setClickable(false);

    answerOneBtn.setBackgroundColor(gray);
    answerTwoBtn.setBackgroundColor(gray);
    answerThreeBtn.setBackgroundColor(gray);
    answerFourBtn.setBackgroundColor(gray);

    switch (correctAnswer()) {
      case 1:
        answerOneBtn.setBackgroundColor(green);
        break;
      case 2:
        answerTwoBtn.setBackgroundColor(green);
        break;
      case 3:
        answerThreeBtn.setBackgroundColor(green);
        break;
      case 4:
        answerFourBtn.setBackgroundColor(green);
        break;
    }
  }

}
