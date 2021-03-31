package com.example.team8project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.*;

public class QuestionList {
  private QuestionXMLParser allQuestions;
  private Question[] list;
  private String[] questions;
  private String[][] wrongAnswers;
  private String[] optionalAnswers;
  private String[] correctAnswers;
  private String[][] jumbledAnswers;


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

  public Question[] getQuestionBank() {
    this.allQuestions.parse();
    Question[] bank = new Question[this.allQuestions.sizeOfBank];
    return this.allQuestions.getQuestionBank(bank);
  }

  // 10 questions total, get random ints and pick, remove that option for being repeat question
  // store the 10 questions in new array and return new array

  public void getQuestionList() {
    Question[] bank = this.getQuestionBank();

    List<Integer> range = new ArrayList<>();
    //randomize an array from 0 to 99
    for(int i = 0; i < 100; i++) {
      range.add(i);
    }
    Collections.shuffle(range, new Random(System.currentTimeMillis()));

    //take first 10 randomized numbers and get the corresponding questions associated with those indices
    for(int j = 0; j < 10; j++) {
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
  public void AnswersJumbled(){
    this.separateQuestions();
    List<String> list = new ArrayList<>();
    for(int i = 0; i < 10; i++){
      list.add(this.wrongAnswers[i][1]);
      list.add(this.wrongAnswers[i][2]);
      list.add(this.wrongAnswers[i][3]);
      list.add(this.correctAnswers[i]);
      Collections.shuffle(list, new Random(System.currentTimeMillis()));
      list.toArray(this.jumbledAnswers[i]);
    }
  }

  // returns answer text of indexed question and answer
  public String getJumbledAnswer(int i, int j){
    return this.jumbledAnswers[i][j];
  }

  // return question text of indexed question
  public String getQuestion(int i){
    return this.questions[i];
  }

  // to be used for lifeline system, to add an extra answer to opponents next question.
  public String getOptionalAnswer(int i){
    return this.optionalAnswers[i];
  }

  // returns answer text of the correct answer for the indexed question
  public String getCorrectAnswer(int i) {
    return this.correctAnswers[i];
  }

}
