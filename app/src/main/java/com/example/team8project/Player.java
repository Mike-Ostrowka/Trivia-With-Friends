package com.example.team8project;

public class Player {


  private int playerID, playerScore, playerELO;
  private String playerName;

  public Player(int playerID, int playerScore, int playerELO, String playerName) {
    this.playerID = playerID;
    this.playerScore = playerScore;
    this.playerELO = playerELO;
    this.playerName = playerName;
  }

  public Player(int playerScore) {
    this.playerScore = playerScore;
  }

  public Player() {
  }

  public int getPlayerID() {
    return playerID;
  }

  public void setPlayerID(int playerID) {
    this.playerID = playerID;
  }

  public int getPlayerScore() {
    return playerScore;
  }

  public void setPlayerScore(int playerScore) {
    this.playerScore = playerScore;
  }

  public int getPlayerELO() {
    return playerELO;
  }

  public void setPlayerELO(int playerELO) {
    this.playerELO = playerELO;
  }

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }
}
