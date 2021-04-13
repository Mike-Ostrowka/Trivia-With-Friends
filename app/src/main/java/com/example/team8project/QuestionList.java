package com.example.team8project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuestionList {

  private final QuestionXMLParser allQuestions;
  private final Question[] list;
  private final String[] questions;
  private final String[][] wrongAnswers;
  private final String[] optionalAnswers;
  private final String[] correctAnswers;
  private final String[][] jumbledAnswers;


  public QuestionList() {
    this.allQuestions = new QuestionXMLParser();
    this.list = new Question[10];
    this.questions = new String[10];
    this.wrongAnswers = new String[10][3];
    this.optionalAnswers = new String[10];
    this.correctAnswers = new String[10];

    // holds 4 answers, 3 wrong and 1 correct
    this.jumbledAnswers = new String[10][4];
  }

  public Question[] getAllQuestions() {
    this.allQuestions.parser();
    Question[] bank = new Question[this.allQuestions.sizeOfBank];
    return this.allQuestions.getQuestionBank(bank);
  }

  // 10 questions total, get random ints and pick, remove that option for being repeat question
  // store the 10 questions in new array and return new array

  public void getQuestionList() {
    Question[] bank = this.getAllQuestions();

    List<Integer> range = new ArrayList<>();
    //randomize an array from 0 to 99
    for (int i = 0; i < 80; i++) {
      range.add(i);
    }
    Collections.shuffle(range, new Random(System.currentTimeMillis()));

    //take first 10 randomized numbers and get the corresponding questions associated with those indices
    for (int j = 0; j < 10; j++) {

      this.list[j] = bank[range.get(j)];
    }
  }

  // Seperates the Question object into an array of questions, array within array of 3 wrong answers,
  // array of optional answers, and an array of correct answers.
  public void separateQuestions() {

    this.getQuestionList();
    for (int i = 0; i < 10; i++) {

      this.questions[i] = this.list[i].getQuestion();
      this.wrongAnswers[i] = this.list[i].getWrongAnswers();
      this.optionalAnswers[i] = this.list[i].getOptionalAnswer();
      this.correctAnswers[i] = this.list[i].getCorrectAnswer();
    }
  }

  // jumble order of wrong and correct answers
  public void AnswersJumbled() {

    this.separateQuestions();
    for (int i = 0; i < 10; i++) {

      List<String> list = new ArrayList<>();
      list.add(this.wrongAnswers[i][0]);
      list.add(this.wrongAnswers[i][1]);
      list.add(this.wrongAnswers[i][2]);
      list.add(this.correctAnswers[i]);

      Collections.shuffle(list, new Random());

      list.toArray(this.jumbledAnswers[i]);

      System.out.println(Arrays.toString(this.jumbledAnswers[i]));
      System.out.println(this.correctAnswers[i]);
    }
  }

  // returns answer text of indexed question and answer
  public String getJumbledAnswer(int i, int j) {
    System.out.println(this.jumbledAnswers[i][j]);
    return this.jumbledAnswers[i][j];
  }

  // return question text of indexed question
  public String getQuestion(int i) {
    System.out.println(this.questions[i]);
    return this.questions[i];
  }

  // to be used for lifeline system, to add an extra answer to opponents next question.
  public String getOptionalAnswer(int i) {
    System.out.println(this.optionalAnswers[i]);
    return this.optionalAnswers[i];
  }

  // returns answer text of the correct answer for the indexed question
  public String getCorrectAnswer(int i) {
    System.out.println(this.correctAnswers[i]);
    return this.correctAnswers[i];
  }
}
