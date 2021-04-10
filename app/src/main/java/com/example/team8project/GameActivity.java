package com.example.team8project;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;


public class GameActivity extends AppCompatActivity {


    int questionCount = 0;
    int playerScore = 0;


    Button answerOneBtn, answerTwoBtn, answerThreeBtn, answerFourBtn;
    TextView questionTextView;
    Game currentGame = new Game(playerScore);
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        currentGame.questionList.AnswersJumbled();
        handler = new Handler();

        answerOneBtn = findViewById(R.id.AnswerOneButton);
        answerTwoBtn = findViewById(R.id.AnswerTwoButton);
        answerThreeBtn = findViewById(R.id.AnswerThreeButton);
        answerFourBtn = findViewById(R.id.AnswerFourButton);
        questionTextView = findViewById(R.id.questionText);


        answerOneBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerOneBtn.getText().toString();
            playerScore += currentGame.checkPlayerAnswer(currentGame.playerOneSelection);
            Log.e("Error", "Clicked");


        });


        answerTwoBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerTwoBtn.getText().toString();
            playerScore += currentGame.checkPlayerAnswer(currentGame.playerOneSelection);


        });


        answerThreeBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerThreeBtn.getText().toString();
            playerScore += currentGame.checkPlayerAnswer(currentGame.playerOneSelection);


        });


        answerFourBtn.setOnClickListener(v -> {

            currentGame.playerOneSelection = answerFourBtn.getText().toString();
            playerScore += currentGame.checkPlayerAnswer(currentGame.playerOneSelection);
            System.out.println("Player Score" + playerScore);

        });

        for (int i = 0; i < 10; i++) {

            handler.postDelayed(() -> playGame(), 10000 * i);
            // playerScore += currentGame.checkPlayerAnswer(currentGame.playerOneSelection);

        }



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