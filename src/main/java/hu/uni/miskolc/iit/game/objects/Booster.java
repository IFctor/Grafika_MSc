package hu.uni.miskolc.iit.game.objects;

import lombok.Getter;

public class Booster {

    @Getter
    float modifierValue;

    @Getter
    long durationSec;

    @Getter
    long endUNIXTimeMillis;

    Booster(long durationSec, float modifierValue){
        this.durationSec=durationSec;
        this.modifierValue=modifierValue;
        endUNIXTimeMillis=System.currentTimeMillis()+(durationSec*1000);
    }

}
