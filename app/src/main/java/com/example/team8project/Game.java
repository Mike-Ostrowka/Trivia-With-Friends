package com.example.team8project;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class Game extends AppCompatActivity {

    public String playerOneSelection, /* playerTwoSelection, */ firstAnswer, secondAnswer, thirdAnswer, fourthAnswer, currentQuestion;
    QuestionList questionList = new QuestionList();
    String getCorrect;
    int questCount, playerScore;
    boolean readFlag;

    public Game(int questionCount, boolean readWrite, int playerScore){
        this.questCount = questionCount;
        this.readFlag = readWrite;
        this.playerScore = playerScore;

    }

    public void loadQuestion(int questionCount) {

        /*
        Pseudo-Code for loading questions
        -->load first question DONE
        -->load answers corresponding to question and randomize their order??(how to do this) DONE
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

    public int checkPlayerAnswer(String selection) {

                if (getCorrect == selection) {
                    return 5;

                } else {
                    return 0;
                }


    }


    public void startGame(int questionCount){

        //changes orders of answers so that correct answer is not always the same button
        loadQuestion(questionCount);
        Log.e("loading questions", "loaded");


    }

}


