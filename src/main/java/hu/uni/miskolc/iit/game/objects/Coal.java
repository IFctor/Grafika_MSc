package hu.uni.miskolc.iit.game.objects;

import hu.uni.miskolc.iit.engine.BoundingBox2D;
import hu.uni.miskolc.iit.engine.GameObject2D;

public class Coal extends FallenObjectImpl{

    public Coal(int boosterDurationSec, float boosterModifierValue){
        super(0,-1,boosterDurationSec,boosterModifierValue);
    }
}
