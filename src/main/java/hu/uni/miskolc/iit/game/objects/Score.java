package hu.uni.miskolc.iit.game.objects;

import hu.uni.miskolc.iit.engine.GameObject2D;
import lombok.Getter;
import lombok.Setter;

public class Score extends GameObject2D{
    @Getter
    private int actualScore = 0;

    public void setActualScore(int actualScore) {
        if(actualScore>=0 && actualScore<=9) {
            this.actualScore = actualScore;
            this.SetCurrentFrame(actualScore);
        }
    }
}
