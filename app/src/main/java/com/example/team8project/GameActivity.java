package com.example.team8project;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.util.UUID;

import io.realm.Realm;


public class GameActivity extends AppCompatActivity {

    int questionCount = 0;
    int playerScore = 0;
    int count = 0;
    boolean playerAnswered = false;
    long _ID = UUID.randomUUID().getMostSignificantBits();

    //declaring all of the layout objects
    Button answerOneBtn, answerTwoBtn, answerThreeBtn, answerFourBtn;
    TextView questionTextView, playerScoreText;

    //declaring current game, handler for rounds, and player one and two
    Dialogs dialog = new Dialogs();
    Handler mainHandler = new Handler();
    Handler gameHandler = new Handler();
    Handler postGameHandler = new Handler();
    Handler mainGameHandler = new Handler();



    Realm realm;
    Game currentGame;
    LoadQuestions loadQuestions;
    Player playerOne;
    private Users current;
    private loginPreferences session;
    private String username;

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

        Runnable postGameRunnable = new Runnable() {
            @Override
            public void run() {

                System.out.println("Gets here");

            }

    };

    Runnable mainGameRunnable = new Runnable() {
        @Override
        public void run() {

                for (int i = 0; i <= 9; i++) {
                    gameHandler.postDelayed(gameRunnable, 5000 * i);
                }

            }
    };

    private boolean gameFinished = false;
    // Player playerTwo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //load realm
        loadRealm();

        //INSTANTIATE OBJECTS FOR USE
        gameHandler = new Handler();

        if (realm.where(Game.class).equalTo("playerCount", 1).findFirst() != null) {
            currentGame = realm.where(Game.class).equalTo("playerCount", 1).findFirst();
            currentGame.setPlayerCount(2);
        }
        else{
            currentGame = new Game(username, _ID, 1);
        }
        currentGame = new Game(username, playerScore, _ID);
        loadQuestions = new LoadQuestions();

        loadQuestions.questionList.AnswersJumbled();

        //INSTANTIATE BUTTONS AND TEXT VIEWS
        answerOneBtn = findViewById(R.id.AnswerOneButton);
        answerTwoBtn = findViewById(R.id.AnswerTwoButton);
        answerThreeBtn = findViewById(R.id.AnswerThreeButton);
        answerFourBtn = findViewById(R.id.AnswerFourButton);
        questionTextView = findViewById(R.id.questionText);
        playerScoreText = findViewById(R.id.playerScore);


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


        //ask player to play a new game and pass current win information to database
        mainGameHandler.postDelayed(mainGameRunnable, 0);
        gameHandler.postDelayed(postGameRunnable, 0);

    }

    private void loadRealm() {
        //open a realm and find logged in user
        session = new loginPreferences(getApplicationContext());
        username = session.getusername();
        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }

        current = realm.where(Users.class).equalTo("_id", username).findFirst();

    }

    @Override
    protected void onPause() {
        super.onPause();
        realm.executeTransaction(transactionRealm -> transactionRealm.insert(currentGame));

        if (realm != null) {
            realm.close();
        }

        gameHandler.removeCallbacks(gameRunnable);
        postGameHandler.removeCallbacks(postGameRunnable);


    }

    public void playGame() {

        answerOneBtn = findViewById(R.id.AnswerOneButton);
        answerTwoBtn = findViewById(R.id.AnswerTwoButton);
        answerThreeBtn = findViewById(R.id.AnswerThreeButton);
        answerFourBtn = findViewById(R.id.AnswerFourButton);
        questionTextView = findViewById(R.id.questionText);
        playerScoreText.setText(username + " " + String.valueOf(playerScore));
        currentGame.setPlayerOneScore(playerScore);

        loadQuestions.loadQuestion(questionCount);
        questionTextView.setText(loadQuestions.currentQuestion);
        answerOneBtn.setText(loadQuestions.firstAnswer);
        answerTwoBtn.setText(loadQuestions.secondAnswer);
        answerThreeBtn.setText(loadQuestions.thirdAnswer);
        answerFourBtn.setText(loadQuestions.fourthAnswer);

        questionCount++;


    }

}