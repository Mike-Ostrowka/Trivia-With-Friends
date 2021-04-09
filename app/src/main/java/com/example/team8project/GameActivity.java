package com.example.team8project;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {


    int questionCount = 0;
    int textUpdate = 0;

    Boolean readFlag = true;
    Button answerOneBtn, answerTwoBtn, answerThreeBtn, answerFourBtn;
    TextView counterTxt;
    int playerScore = 0;
    Game currentGame = new Game(questionCount, readFlag, playerScore);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        playGame();

        answerOneBtn = findViewById(R.id.AnswerOneButton);
        answerTwoBtn = findViewById(R.id.AnswerTwoButton);
        answerThreeBtn = findViewById(R.id.AnswerThreeButton);
        answerFourBtn = findViewById(R.id.AnswerFourButton);
        counterTxt = findViewById(R.id.CounterText);

//        answerOneBtn.setText(currentGame.firstAnswer);
//        answerTwoBtn.setText(currentGame.secondAnswer);
//        answerThreeBtn.setText(currentGame.thirdAnswer);
//        answerFourBtn.setText(currentGame.fourthAnswer);

        TextView questionTextView = findViewById(R.id.questionText);
        questionTextView.setText(currentGame.currentQuestion);


        answerOneBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerOneBtn.getText().toString();
            Log.e("error", currentGame.playerOneSelection);




        });


        answerTwoBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerTwoBtn.getText().toString();
            Log.e("error", currentGame.playerOneSelection);


        });


        answerThreeBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerThreeBtn.getText().toString();
            Log.e("error", currentGame.playerOneSelection);


        });


        answerFourBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerFourBtn.getText().toString();
            Log.e("error", currentGame.playerOneSelection);


        });




    }



    public void playGame() {
        currentGame.questionList.AnswersJumbled();

        answerOneBtn = findViewById(R.id.AnswerOneButton);
        answerTwoBtn = findViewById(R.id.AnswerTwoButton);
        answerThreeBtn = findViewById(R.id.AnswerThreeButton);
        answerFourBtn = findViewById(R.id.AnswerFourButton);
        counterTxt = findViewById(R.id.CounterText);

//        for (int i = 0; i < 10; i++) {
//
//            currentGame.loadQuestion(i);
//            answerOneBtn.setText(currentGame.firstAnswer);
//            answerTwoBtn.setText(currentGame.secondAnswer);
//            answerThreeBtn.setText(currentGame.thirdAnswer);
//            answerFourBtn.setText(currentGame.fourthAnswer);
//
//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//
//                    Log.e("Running", currentGame.firstAnswer);
//                }
//            },0,1000000);
//
//
//        }

        do{


            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    currentGame.loadQuestion(questionCount);
                    Log.e("Running", currentGame.firstAnswer);
                                answerOneBtn.setText(currentGame.firstAnswer);
            answerTwoBtn.setText(currentGame.secondAnswer);
            answerThreeBtn.setText(currentGame.thirdAnswer);
            answerFourBtn.setText(currentGame.fourthAnswer);

                }
            },0,1000);
            questionCount++;

        }while (questionCount < 9);




    }

}