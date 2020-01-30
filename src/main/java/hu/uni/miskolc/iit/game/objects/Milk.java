package hu.uni.miskolc.iit.game.objects;

import hu.uni.miskolc.iit.engine.MusicManager;

public class Milk  extends FallenObjectImpl{
    public Milk(int lifeModifier,int boosterDurationSec, float boosterModifierValue,int modellId){
        super(10,lifeModifier,boosterDurationSec,boosterModifierValue,modellId);
    }
    @Override
    protected void playEffectSound() {
        MusicManager.drinkSound.play();
    }
}
