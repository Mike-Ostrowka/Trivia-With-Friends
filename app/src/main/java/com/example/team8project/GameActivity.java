package com.example.team8project;

import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity {


    int questionCount = 0;
    int textUpdate = 0;

    Boolean readFlag = true;
    Button answerOneBtn, answerTwoBtn, answerThreeBtn, answerFourBtn;
    TextView questionTextView;

    int playerScore = 0;
    Game currentGame = new Game(questionCount, readFlag, playerScore);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        currentGame.questionList.AnswersJumbled();

        for(int i = 0; i < 10; i++) {
            Log.e("Error", "For Loop is Running");

            ScheduledExecutorService scheduler =
                    Executors.newSingleThreadScheduledExecutor();

            scheduler.scheduleAtFixedRate
                    (new Runnable() {
                        public void run() {
                            // call service
                            playGame();
                        }
                    }, 0, 10, TimeUnit.SECONDS);

        }

        answerOneBtn = findViewById(R.id.AnswerOneButton);
        answerTwoBtn = findViewById(R.id.AnswerTwoButton);
        answerThreeBtn = findViewById(R.id.AnswerThreeButton);
        answerFourBtn = findViewById(R.id.AnswerFourButton);
        questionTextView = findViewById(R.id.questionText);


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

        answerOneBtn = findViewById(R.id.AnswerOneButton);
        answerTwoBtn = findViewById(R.id.AnswerTwoButton);
        answerThreeBtn = findViewById(R.id.AnswerThreeButton);
        answerFourBtn = findViewById(R.id.AnswerFourButton);
        questionTextView = findViewById(R.id.questionText);



        currentGame.loadQuestion(questionCount);
        questionTextView.setText(currentGame.currentQuestion);
        answerOneBtn.setText(currentGame.firstAnswer);
        answerTwoBtn.setText(currentGame.secondAnswer);
        answerThreeBtn.setText(currentGame.thirdAnswer);
        answerFourBtn.setText(currentGame.fourthAnswer);

        questionCount++;


    }

}