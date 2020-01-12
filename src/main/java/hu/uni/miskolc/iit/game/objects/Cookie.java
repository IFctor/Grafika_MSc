package hu.uni.miskolc.iit.game.objects;

public class Cookie extends FallenObjectImpl{
    public Cookie(int lifeModifier,int boosterDurationSec, float boosterModifierValue){
        super(0,lifeModifier,boosterDurationSec,boosterModifierValue);
    }
}
