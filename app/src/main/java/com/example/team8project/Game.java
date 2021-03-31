package com.example.team8project;


public class Game {

    public String playerOneSelection, playerTwoSelection, firstAnswer, secondAnswer, thirdAnswer, fourthAnswer, fifthAnswer;
    public int questionCount = 0;
    public int startingTimerSecondsAnswer = 10;
    public int startingTimerSecondsRead = 10;
    public int playerOneScore, playerTwoScore = 0;
    QuestionList questionList = new QuestionList();
    String getCurrentQuestion[];
    String getCorrect[];


    public String loadQuestion() {

        /*
        Pseudo-Code for loading questions
        -->load first question
        -->load answers corresponding to question and randomize their order??(how to do this)
        -->timer starts counting down from 10
        -->when timer reaches 0, player has 10 seconds to answer
        -->take player input and use checkPlayerAnswer() to compare to curent questions correct answer
        -->if correct player gains 5 points
        -->refresh GameActivity with next question(increment questionCount 1)-->most likely to be done using new activity(must research this)
        -->game runs until player has 25 points(this is more in relation to multiplayer.
        -->return to welcomeActivity




         */
        questionList.separateQuestions();
        getCurrentQuestion[1] = questionList.correctAnswers[1];
        return getCurrentQuestion[1];
    }

    public void checkPlayerAnswer(String selection) {

        getCorrect[1] = questionList.correctAnswers[1];
        do {
            for (int i = 0; i <= getCurrentQuestion.length; i++)
                if (getCorrect[1] == selection) {
                    playerOneScore += 5;
                } else {
                    //logic for incorrect question
                }
        } while (playerOneScore < 25);
    }

}


