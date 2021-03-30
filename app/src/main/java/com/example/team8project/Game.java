package com.example.team8project;

public class Game {

    public int playerOneSelection, playerTwoSelection;
    public int startingTimerSecondsAnswer = 10;
    public int startingTimerSecondsRead = 10;
    QuestionXMLParser getQBank = new QuestionXMLParser();
    //Question getQuestion = new Question();


    public QuestionXMLParser loadQuestion(){

        QuestionXMLParser currentQuestion = getQBank;
        //TODO add logic for getting questions, questionbank must be completed first

        return currentQuestion;
    }

    public void getPlayerAnswer(int selection){

        int playerSelection = selection;



    }



}
