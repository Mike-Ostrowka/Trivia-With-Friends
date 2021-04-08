package com.example.team8project;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.example.team8project.Timer;


public class Game extends AppCompatActivity {

    public String playerOneSelection, playerTwoSelection, firstAnswer, secondAnswer, thirdAnswer, fourthAnswer, fifthAnswer, currentQuestion;
    public int questionCount = 0;
    public int playerOneScore, playerTwoScore = 0;
    QuestionList questionList = new QuestionList();
    String[] getCurrentQuestion;
    String getCorrect;
    //variables for timers
    private boolean running;
    public int startingTimerSecondsAnswer = 0;
    public int startingTimerSecondsRead = 0;
    public int endTime = 10;
//    Button answerOneBtn = findViewById(R.id.AnswerOneButton);
//    Button answerTwoBtn = findViewById(R.id.AnswerTwoButton);
//    Button answerThreeBtn = findViewById(R.id.AnswerThreeButton);
//    Button answerFourBtn = findViewById(R.id.AnswerFourButton);
//    Button answerFiveBtn = findViewById(R.id.AnswerFiveButton);


    public void loadQuestion(int questionCount) {

        /*
        Pseudo-Code for loading questions
        -->load first question
        -->load answers corresponding to question and randomize their order??(how to do this)
        -->timer starts counting down from 10
        -->when timer reaches 0, player has 10 seconds to answer
        -->take player input and use checkPlayerAnswer() to compare to current questions correct answer
        -->if correct player gains 5 points
        -->refresh GameActivity with next question(increment questionCount 1)-->most likely to be done using new activity(must research this)
        -->game runs until player has 25 points(this is more in relation to multiplayer.
        -->return to welcomeActivity




         */

        // Changed the questionlist class, jumbled holds 3 correct and 1 wrong, fifth answer is optional
        // but correct one is within one of the jumbled answers

        currentQuestion = questionList.getQuestion(questionCount);
        firstAnswer = questionList.getJumbledAnswer(questionCount,0);
        secondAnswer = questionList.getJumbledAnswer(questionCount,1);
        thirdAnswer = questionList.getJumbledAnswer(questionCount,2);
        fourthAnswer = questionList.getJumbledAnswer(questionCount,3);
        getCorrect = questionList.getCorrectAnswer(questionCount);

    }

    public void checkPlayerAnswer(String selection) {

       // getCorrect[1] = questionList.correctAnswers[1];
        do {
            for (int i = 0; i <= getCurrentQuestion.length; i++) {
                if (getCorrect == selection) {
                    playerOneScore += 5;
                } else {
                    Toast.makeText(this, "You chose the wrong answer sorry!", Toast.LENGTH_SHORT).show();
                }
            }
        } while (playerOneScore < 25);
    }


    public void startGame(){

        questionList.AnswersJumbled();
        for(int i = 0; i < 10; i++) {
            loadQuestion(i);
            Thread threadRead = new Thread(new Timer());
//            answerOneBtn.setClickable(true);
//            answerTwoBtn.setClickable(true);
//            answerThreeBtn.setClickable(true);
//            answerFourBtn.setClickable(true);
//            answerFiveBtn.setClickable(true);
            Thread threadAnswer = new Thread(new Timer());
        }
    }

}


