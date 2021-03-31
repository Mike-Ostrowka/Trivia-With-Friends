package com.example.team8project;


public class Game {

    public String playerOneSelection, playerTwoSelection, firstAnswer, secondAnswer, thirdAnswer, fourthAnswer, fifthAnswer;
    public int startingTimerSecondsAnswer = 10;
    public int startingTimerSecondsRead = 10;
    public int playerOneScore, playerTwoScore = 0;
    QuestionList questionList = new QuestionList();
    String getCurrentQuestion[];
    String getCorrect[];


    public String loadQuestion() {


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


