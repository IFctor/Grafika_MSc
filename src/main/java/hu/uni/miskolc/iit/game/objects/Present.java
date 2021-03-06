package hu.uni.miskolc.iit.game.objects;

import hu.uni.miskolc.iit.engine.BoundingBox2D;
import hu.uni.miskolc.iit.engine.GameObject2D;
import hu.uni.miskolc.iit.engine.MusicManager;
import hu.uni.miskolc.iit.engine.Window;
import hu.uni.miskolc.iit.engine.math.Vector2D;
import lombok.Getter;

import java.util.Random;

public class Present extends FallenObjectImpl {

    public Present(int score, int modellId) {
        super(score, 0, 0, 0, modellId);
    }
    @Override
    protected void playEffectSound() {
        MusicManager.hohohoSound.play();
    }
}
