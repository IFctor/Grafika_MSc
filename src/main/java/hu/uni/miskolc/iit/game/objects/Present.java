package hu.uni.miskolc.iit.game.objects;

import hu.uni.miskolc.iit.engine.GameObject2D;
import hu.uni.miskolc.iit.engine.Window;
import hu.uni.miskolc.iit.engine.math.Vector2D;

import java.util.Random;

public class Present extends GameObject2D {

    static Random r = new Random();

    boolean collected=false;
    int startPos;
    float moveSpeed =1.0f;

    public void movePresent(){
        Vector2D pos = this.GetPosition();
        pos.y += 2*moveSpeed;
        this.SetPosition(pos);
        if(pos.y>720 || collected==true){
            this.init();
        }
    }

    public Present(){
        super();
        init();
    }

    private void init(){
        startPos = r.nextInt((20 - 0) + 1) + 0;
        this.SetPosition(50+startPos*50,-100);
        moveSpeed = (r.nextInt((3 - 1) + 1) + 1)/1.2f;
        collected=false;
    }


}
