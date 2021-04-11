package com.example.team8project;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class Game extends AppCompatActivity {

    public String playerOneSelection, playerTwoSelection,
            firstAnswer, secondAnswer, thirdAnswer, fourthAnswer, currentQuestion;
    QuestionList questionList = new QuestionList();
    String getCorrect;

    private String playerOne, playerTwo;
    private String playerOneScore, playerTwoScore;
    private String[] players;
    private boolean gameCompleted;

    public Game(String playerOne, String playerTwo, String[] players, boolean gameCompleted) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.players = players;
        this.gameCompleted = gameCompleted;
    }

    public Game() {
        //empty constructor
    }

    public String getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(String playerOne) {
        this.playerOne = playerOne;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(String playerTwo) {
        this.playerTwo = playerTwo;
    }

    public String[] getPlayers() {
        return players;
    }

    public void setPlayers(String[] players) {
        this.players = players;
    }

    public boolean isGameCompleted() {
        return gameCompleted;
    }

    public void setGameCompleted(boolean gameCompleted) {
        this.gameCompleted = gameCompleted;
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


