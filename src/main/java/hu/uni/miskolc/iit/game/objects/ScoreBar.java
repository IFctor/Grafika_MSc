package hu.uni.miskolc.iit.game.objects;

import hu.uni.miskolc.iit.config.AppConfig;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ScoreBar {
    @Getter
    List<Score> scoreList = new ArrayList<>();
    private float startYPosition = 10;
    private float endXPosition = AppConfig.appConfig().getWindow().getWidth() - 30;
    private float shiftXPosition = 25;
    private final int MAX_DIGIT = AppConfig.appConfig().getPlayers().getScore().getMaxDigit();
    private final int MAX_SCORE = (int) Math.pow(10, MAX_DIGIT) - 1;

    @Getter
    private int actualScore = 0;


    public void addScoreObject(Score scoreObj) {
        scoreList.add(scoreObj);
    }

    public void init() {
        actualScore = 0;
        int index = 0;
        for (Score _score : scoreList) {
            _score.setActualScore(0);
            _score.SetPosition(endXPosition - (index * shiftXPosition), startYPosition);
            index++;
        }
        refreshScoreBar();
    }

    public void AddScorePoints(int addedScore) {
        this.actualScore += addedScore;

        if(actualScore>MAX_SCORE)
        {
            actualScore=MAX_SCORE;
        }else if(actualScore<0){
            actualScore=0;
        }
        refreshScoreBar();
    }

    private void refreshScoreBar() {

        String number = String.format("%0" + MAX_DIGIT + "d", actualScore);
        String reverseNumber = new StringBuilder(number).reverse().toString();
        for (int index = 0; index < reverseNumber.length(); index++) {
            int j = Character.digit(reverseNumber.charAt(index), 10);
            scoreList.get(index).setActualScore(j);
        }
    }
}
