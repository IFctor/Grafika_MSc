package hu.uni.miskolc.iit.game.objects;

import hu.uni.miskolc.iit.engine.GameObject2D;
import lombok.Getter;
import lombok.Setter;

public class Score{
    @Getter
    private int actualScore = 0;

    public void addToActualScore(int addedScore){
        actualScore+=addedScore;
    }
}
