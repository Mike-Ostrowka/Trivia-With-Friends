package com.example.team8project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.*;

public class QuestionList {
  public QuestionXMLParser allQuestions;
  private Question[] list;
  private String[] questions;
  private String[][] wrongAnswers;
  private String[] optionalAnswers;
  private String[] correctAnswers;


  public QuestionList() {
    this.allQuestions = new QuestionXMLParser();
    this.list = new Question[10];
    this.questions = new String[10];
    this.wrongAnswers = new String[10][3];
    this.optionalAnswers = new String[10];
    this.correctAnswers = new String[10];
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

    List<Integer> range = new ArrayList<Integer>();
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
    this.getQuestionBank();
    for (int i = 0; i < 10; i++) {
      this.questions[1] = this.list[i].getQuestion();
      this.wrongAnswers[1] = this.list[i].getWrongAnswers();
      this.optionalAnswers[1] = this.list[i].getOptionalAnswer();
      this.correctAnswers[1] = this.list[i].getCorrectAnswer();
    }
  }

}
