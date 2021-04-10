package com.example.team8project;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class Game extends AppCompatActivity {

    public String playerOneSelection, /* playerTwoSelection, */
            firstAnswer, secondAnswer, thirdAnswer, fourthAnswer, currentQuestion;
    QuestionList questionList = new QuestionList();
    String getCorrect;

    public Game() {
    }



    public void loadQuestion(int questionCount) {

        // Changed the questionlist class, jumbled holds 3 correct and 1 wrong, fifth answer is optional
        // but correct one is within one of the jumbled answers

        currentQuestion = questionList.getQuestion(questionCount);
        firstAnswer = questionList.getJumbledAnswer(questionCount, 0);
        secondAnswer = questionList.getJumbledAnswer(questionCount, 1);
        thirdAnswer = questionList.getJumbledAnswer(questionCount, 2);
        fourthAnswer = questionList.getJumbledAnswer(questionCount, 3);
        getCorrect = questionList.getCorrectAnswer(questionCount);

    }

    public int checkPlayerAnswer(String selection) {

        if (getCorrect.equals(selection)) {
            return 5;

        } else {
            return 0;
        }


    }



}


