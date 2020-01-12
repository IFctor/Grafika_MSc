package hu.uni.miskolc.iit.game.objects;

import hu.uni.miskolc.iit.engine.BoundingBox2D;
import hu.uni.miskolc.iit.engine.GameObject2D;
import hu.uni.miskolc.iit.engine.math.Vector2D;
import lombok.Getter;

import java.util.Random;

public class FallenObjectImpl  extends GameObject2D implements FallenObject {

    static private Random r = new Random();

    private float moveSpeed =1.0f;
    @Getter
    private int score=0;
    @Getter
    private int lifeModifier=0;
    @Getter
    private long boosterDurationSec=0;
    @Getter
    private float boosterModifierValue=0;

    @Override
    public void update() {
move();
    }

    public void move(){
        Vector2D pos = this.GetPosition();
        pos.y += 2*moveSpeed;
        this.SetPosition(pos);
        if(pos.y>720){
            init();
        }
    }

    FallenObjectImpl(int score,int lifeModifier, int boosterDurationSec, float boosterModifierValue){
        super();
        this.score=score;
        this.lifeModifier=lifeModifier;
        this.boosterDurationSec=boosterDurationSec;
        this.boosterModifierValue=boosterModifierValue;
        this.init();
    }

    @Override
    public boolean collisionDetection(Player player) {
        boolean result=false;
        BoundingBox2D boundingBox = this.GetCurrentFrame().getCurrentFrameTransformedBoundingBox();
        if (boundingBox.CheckOverlapping(player.GetCurrentFrame().getCurrentFrameTransformedBoundingBox())) {
            result=true;
            init();

        }
        return result;
    }
     void init(){
        int startPos = r.nextInt((20 - 0) + 1) + 0;
        this.SetPosition(50+startPos*50,-100);
        moveSpeed = (r.nextInt((3 - 1) + 1) + 1)/1.2f;
        this.setMVisible(false);
    }

    public void setVisible(boolean visible){
        this.setMVisible(visible);
    }

    public boolean isVisible()
    {
        return this.isMVisible();
    }


    @Override
    public void cleanupObject() {
        this.cleanUp();
    }
}
