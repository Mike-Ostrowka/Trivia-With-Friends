package com.example.team8project;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {


    int questionCount;
    Boolean readFlag = true;
    Button answerOneBtn, answerTwoBtn, answerThreeBtn, answerFourBtn;
    TextView answerFiveBtn;
    int playerScore = 0;
    Game currentGame = new Game(questionCount, readFlag, playerScore);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        answerOneBtn = findViewById(R.id.AnswerOneButton);
        answerTwoBtn = findViewById(R.id.AnswerTwoButton);
        answerThreeBtn = findViewById(R.id.AnswerThreeButton);
        answerFourBtn = findViewById(R.id.AnswerFourButton);
        answerFiveBtn = findViewById(R.id.AnswerFiveButton);

        answerOneBtn.setText(currentGame.firstAnswer);
        answerTwoBtn.setText(currentGame.secondAnswer);
        answerThreeBtn.setText(currentGame.thirdAnswer);
        answerFourBtn.setText(currentGame.fourthAnswer);

        TextView questionTextView = findViewById(R.id.questionText);
        questionTextView.setText(currentGame.currentQuestion);


        answerOneBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerOneBtn.getText().toString();
            playerScore += currentGame.checkPlayerAnswer(currentGame.playerOneSelection);
            Log.e("error", "button one clicked");




        });


        answerTwoBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerTwoBtn.getText().toString();
            playerScore += currentGame.checkPlayerAnswer(currentGame.playerOneSelection);
            Log.e("error", "button two clicked");


        });


        answerThreeBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerThreeBtn.getText().toString();
            playerScore += currentGame.checkPlayerAnswer(currentGame.playerOneSelection);
            Log.e("error", "button 3 clicked");


        });


        answerFourBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerFourBtn.getText().toString();
            playerScore += currentGame.checkPlayerAnswer(currentGame.playerOneSelection);
            Log.e("error", "button 4 clicked");


        });




    }



    public void playGame() {

        if(playerScore < 25){
            if (readFlag = true){
                currentGame.startGame();
            }
            else{

            }
        }
        else {
           //user wins
        }




    }

}