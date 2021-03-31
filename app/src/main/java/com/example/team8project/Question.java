package com.example.team8project;

public class Question {

  private String question = null;
  // Answer Choices
  private String answerOne = null;
  private String answerTwo = null;
  private String answerThree = null;
  private String answerFour = null;
  //Correct Answer
  private String correctAnswer = null;

  public Question(String question, String answerOne, String answerTwo, String answerThree,
      String answerFour, String correctAnswer) {
    this.question = question;
    this.answerOne = answerOne;
    this.answerTwo = answerTwo;
    this.answerThree = answerThree;
    this.answerFour = answerFour;
    this.correctAnswer = correctAnswer;
  }

  public Question() {
  // make it all null
  }

  public String[] getWrongAnswers() {
    return new String[]{this.answerOne, this.answerTwo, this.answerThree};
  }

  public String getOptionalAnswer() {
    return this.answerFour;
  }

  public String getCorrectAnswer() {
    return this.correctAnswer;
  }

  public String getQuestion() {
    return this.question;
  }


}
