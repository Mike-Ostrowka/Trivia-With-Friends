package com.example.team8project;

public class Question {

  private String question;
  // Answer Choices
  private String answerOne;
  private String answerTwo;
  private String answerThree;
  private String answerFour;
  //Correct Answer
  private String correctAnswer;

  public Question(String question, String answerOne, String answerTwo, String answerThree,
      String answerFour, String correctAnswer) {
    this.question = question;
    this.answerOne = answerOne;
    this.answerTwo = answerTwo;
    this.answerThree = answerThree;
    this.answerFour = answerFour;
    this.correctAnswer = correctAnswer;
  }

  public String[] getWrongAnswers() {
    return new String[]{this.answerOne, this.answerTwo, this.answerThree,
        this.answerFour};
  }

  public String getAnswer() {
    return this.correctAnswer;
  }

  public String getQuestion() {
    return this.question;
  }
}
