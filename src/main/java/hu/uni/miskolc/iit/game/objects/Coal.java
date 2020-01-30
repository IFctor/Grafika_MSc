package hu.uni.miskolc.iit.game.objects;

import hu.uni.miskolc.iit.engine.BoundingBox2D;
import hu.uni.miskolc.iit.engine.GameObject2D;
import hu.uni.miskolc.iit.engine.MusicManager;
import hu.uni.miskolc.iit.engine.sound.AudioMaster;

public class Coal extends FallenObjectImpl{

    public Coal(int boosterDurationSec, float boosterModifierValue,int modifiervalue,int modellId){
        super(-5*modifiervalue,
                -10*modifiervalue,
                boosterDurationSec*modifiervalue,
                boosterModifierValue*modifiervalue,
                modellId);
    }

    @Override
    protected void playEffectSound() {
        MusicManager.hitSound.play();
    }
}
