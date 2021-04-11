package com.example.team8project;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class GameActivity extends AppCompatActivity {


    int questionCount = 0;
    int playerScore = 0;
    boolean playerAnswered = false;

    //declaring all of the layout objects
    Button answerOneBtn, answerTwoBtn, answerThreeBtn, answerFourBtn;
    TextView questionTextView, playerScoreText;

    //declaring current game, handler for rounds, and player one and two
    Dialogs dialog = new Dialogs();
    Handler handler = new Handler();
    Runnable gameRunnable = new Runnable() {
        @Override
        public void run() {

                playGame();
                System.out.println(questionCount);
                answerOneBtn.setClickable(true);
                answerTwoBtn.setClickable(true);
                answerThreeBtn.setClickable(true);
                answerFourBtn.setClickable(true);
            }

    };


    Game currentGame;
    Player playerOne;
    // Player playerTwo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        handler = new Handler();
        currentGame = new Game();


        playerOne = new Player(playerScore);
        currentGame.questionList.AnswersJumbled();

        answerOneBtn = findViewById(R.id.AnswerOneButton);
        answerTwoBtn = findViewById(R.id.AnswerTwoButton);
        answerThreeBtn = findViewById(R.id.AnswerThreeButton);
        answerFourBtn = findViewById(R.id.AnswerFourButton);
        questionTextView = findViewById(R.id.questionText);
        playerScoreText = findViewById(R.id.playerScore);




        answerOneBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerOneBtn.getText().toString();
            playerScore += currentGame.checkPlayerAnswer(currentGame.playerOneSelection);
            playerOne.setPlayerScore(playerScore);
            answerOneBtn.setClickable(false);
            answerTwoBtn.setClickable(false);
            answerThreeBtn.setClickable(false);
            answerFourBtn.setClickable(false);


        });


        answerTwoBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerTwoBtn.getText().toString();

            playerScore += currentGame.checkPlayerAnswer(currentGame.playerOneSelection);
            playerOne.setPlayerScore(playerScore);
            answerOneBtn.setClickable(false);
            answerTwoBtn.setClickable(false);
            answerThreeBtn.setClickable(false);
            answerFourBtn.setClickable(false);


        });


        answerThreeBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerThreeBtn.getText().toString();

            playerScore += currentGame.checkPlayerAnswer(currentGame.playerOneSelection);
            playerOne.setPlayerScore(playerScore);
            answerOneBtn.setClickable(false);
            answerTwoBtn.setClickable(false);
            answerThreeBtn.setClickable(false);
            answerFourBtn.setClickable(false);


        });


        answerFourBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerFourBtn.getText().toString();
            playerScore += currentGame.checkPlayerAnswer(currentGame.playerOneSelection);
            playerOne.setPlayerScore(playerScore);
            answerOneBtn.setClickable(false);
            answerTwoBtn.setClickable(false);
            answerThreeBtn.setClickable(false);
            answerFourBtn.setClickable(false);

        });

        for (int i = 0; i <= 9; i++) {

                handler.postDelayed(gameRunnable, 5000 * i);

        }


            //ask player to play a new game and pass current win information to database


    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(gameRunnable);

    }

    public void playGame() {

        answerOneBtn = findViewById(R.id.AnswerOneButton);
        answerTwoBtn = findViewById(R.id.AnswerTwoButton);
        answerThreeBtn = findViewById(R.id.AnswerThreeButton);
        answerFourBtn = findViewById(R.id.AnswerFourButton);
        questionTextView = findViewById(R.id.questionText);
        playerScoreText.setText(String.valueOf(playerOne.getPlayerScore()));

        currentGame.loadQuestion(questionCount);
        questionTextView.setText(currentGame.currentQuestion);
        answerOneBtn.setText(currentGame.firstAnswer);
        answerTwoBtn.setText(currentGame.secondAnswer);
        answerThreeBtn.setText(currentGame.thirdAnswer);
        answerFourBtn.setText(currentGame.fourthAnswer);

        questionCount++;


    }

}