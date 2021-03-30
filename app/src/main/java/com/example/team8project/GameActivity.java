package com.example.team8project;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class GameActivity extends AppCompatActivity {

    Game currentGame = new Game();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

    }


    public void playGame() {

        Button answerOneBtn = (Button) findViewById(R.id.AnswerOneButton);
        answerOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentGame.playerOneSelection = 1;
                currentGame.getPlayerAnswer(currentGame.playerOneSelection);

            }
        });

        Button answerTwoBtn = (Button) findViewById(R.id.AnswerTwoButton);
        answerOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentGame.playerOneSelection = 2;
                currentGame.getPlayerAnswer(currentGame.playerOneSelection);

            }
        });

        Button answerThreeBtn = (Button) findViewById(R.id.AnswerThreeButton);
        answerOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentGame.playerOneSelection = 3;
                currentGame.getPlayerAnswer(currentGame.playerOneSelection);

            }
        });

        Button answerFourBtn = (Button) findViewById(R.id.AnswerFourButton);
        answerOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentGame.playerOneSelection = 4;
                currentGame.getPlayerAnswer(currentGame.playerOneSelection);

            }
        });

        Button answerFiveBtn = (Button) findViewById(R.id.AnswerFiveButton);
        answerOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentGame.playerOneSelection = 5;
                currentGame.getPlayerAnswer(currentGame.playerOneSelection);

            }
        });

    }

}