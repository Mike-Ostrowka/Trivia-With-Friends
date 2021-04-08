package com.example.team8project;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    Game currentGame = new Game();
    Button answerOneBtn = findViewById(R.id.AnswerOneButton);
    Button answerTwoBtn = findViewById(R.id.AnswerTwoButton);
    Button answerThreeBtn = findViewById(R.id.AnswerThreeButton);
    Button answerFourBtn = findViewById(R.id.AnswerFourButton);
    Button answerFiveBtn = findViewById(R.id.AnswerFiveButton);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        currentGame.startGame();

    }


    public void playGame() {


        answerOneBtn.setText(currentGame.firstAnswer);
        answerTwoBtn.setText(currentGame.secondAnswer);
        answerThreeBtn.setText(currentGame.thirdAnswer);
        answerFourBtn.setText(currentGame.fourthAnswer);
        answerFiveBtn.setText(currentGame.fifthAnswer);

        TextView questionTextView = findViewById(R.id.questionText);
        questionTextView.setText(currentGame.currentQuestion);


        answerOneBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerOneBtn.getText().toString();
            currentGame.checkPlayerAnswer(currentGame.playerOneSelection);
            answerOneBtn.setClickable(false);
            answerTwoBtn.setClickable(false);
            answerThreeBtn.setClickable(false);
            answerFourBtn.setClickable(false);
            answerFiveBtn.setClickable(false);


        });


        answerOneBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerTwoBtn.getText().toString();
            currentGame.checkPlayerAnswer(currentGame.playerOneSelection);
            answerOneBtn.setClickable(false);
            answerTwoBtn.setClickable(false);
            answerThreeBtn.setClickable(false);
            answerFourBtn.setClickable(false);
            answerFiveBtn.setClickable(false);



        });


        answerOneBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerThreeBtn.getText().toString();
            currentGame.checkPlayerAnswer(currentGame.playerOneSelection);
            answerOneBtn.setClickable(false);
            answerTwoBtn.setClickable(false);
            answerThreeBtn.setClickable(false);
            answerFourBtn.setClickable(false);
            answerFiveBtn.setClickable(false);

        });


        answerOneBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerFourBtn.getText().toString();
            currentGame.checkPlayerAnswer(currentGame.playerOneSelection);
            answerOneBtn.setClickable(false);
            answerTwoBtn.setClickable(false);
            answerThreeBtn.setClickable(false);
            answerFourBtn.setClickable(false);
            answerFiveBtn.setClickable(false);


        });


        answerOneBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerFiveBtn.getText().toString();
            currentGame.checkPlayerAnswer(currentGame.playerOneSelection);
            answerOneBtn.setClickable(false);
            answerTwoBtn.setClickable(false);
            answerThreeBtn.setClickable(false);
            answerFourBtn.setClickable(false);
            answerFiveBtn.setClickable(false);

        });

    }

}