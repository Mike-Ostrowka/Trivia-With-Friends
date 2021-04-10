package com.example.team8project;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;


public class GameActivity extends AppCompatActivity {


    int questionCount = 0;
    int playerScore = 0;
    boolean playerAnswered = false;

    //declaring all of the layout objects
    Button answerOneBtn, answerTwoBtn, answerThreeBtn, answerFourBtn;
    TextView questionTextView, playerScoreText;

    //declaring current game, handler for rounds, and player one and two
    Handler handler = new Handler();
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
            playerAnswered = true;


        });


        answerTwoBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerTwoBtn.getText().toString();

            playerScore += currentGame.checkPlayerAnswer(currentGame.playerOneSelection);
            playerOne.setPlayerScore(playerScore);
            playerAnswered = true;


        });


        answerThreeBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerThreeBtn.getText().toString();

            playerScore += currentGame.checkPlayerAnswer(currentGame.playerOneSelection);
            playerOne.setPlayerScore(playerScore);
            playerAnswered = true;


        });


        answerFourBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerFourBtn.getText().toString();
            playerScore += currentGame.checkPlayerAnswer(currentGame.playerOneSelection);
            playerOne.setPlayerScore(playerScore);
            playerAnswered = true;

        });

        for (int i = 0; i < 10; i++) {

            handler.postDelayed(() -> playGame(), 10000 * i);
            playerAnswered = false;
//            playerScore += currentGame.checkPlayerAnswer(currentGame.playerOneSelection);

        }



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