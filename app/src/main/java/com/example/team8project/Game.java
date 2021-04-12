package com.example.team8project;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


public class Game extends RealmObject {


    @Required
    private String playerOne;
    private String  playerTwo;
    private int playerOneScore, playerTwoScore;
    private boolean gameCompleted;
    private int playerCount;

    public Game(String playerOne, long _id, int playerCount) {
        this.playerOne = playerOne;
        this.playerCount = playerCount;
        this._id = _id;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    @PrimaryKey
    private long _id;


    public Game(String playerOne, long _id) {
        this.playerOne = playerOne;
        this._id = _id;
    }

    public Game(String playerOne, int playerOneScore, long _id) {
        this.playerOne = playerOne;
        this.playerOneScore = playerOneScore;
        this._id = _id;
    }

    public int getPlayerOneScore() {
        return playerOneScore;
    }

    public void setPlayerOneScore(int playerOneScore) {
        this.playerOneScore = playerOneScore;
    }

    public int getPlayerTwoScore() {
        return playerTwoScore;
    }

    public void setPlayerTwoScore(int playerTwoScore) {
        this.playerTwoScore = playerTwoScore;
    }

    public Game(String playerOne, String playerTwo, int playerOneScore, int playerTwoScore, long _id) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.playerOneScore = playerOneScore;
        this.playerTwoScore = playerTwoScore;
        this._id = _id;
    }


    public Game(String playerOne, String playerTwo, long _id, boolean gameCompleted) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this._id = _id;
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


    public boolean isGameCompleted() {
        return gameCompleted;
    }

    public void setGameCompleted(boolean gameCompleted) {
        this.gameCompleted = gameCompleted;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }


}


