package hu.uni.miskolc.iit.game.objects;

import hu.uni.miskolc.iit.engine.MusicManager;

public class Cookie extends FallenObjectImpl{
    public Cookie(int lifeModifier,int boosterDurationSec, float boosterModifierValue, int modellId){
        super(5,lifeModifier,boosterDurationSec,boosterModifierValue,modellId);
    }
    @Override
    protected void playEffectSound() {
        MusicManager.eatSound.play();
    }
}
