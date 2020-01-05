package hu.uni.miskolc.iit.game.objects;

import hu.uni.miskolc.iit.engine.GameObject2D;
import hu.uni.miskolc.iit.engine.math.Vector2D;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Player extends GameObject2D{

    @Setter
    private int direction;

    @Getter
    private float moveSpeed;

    private int baseMove;

    @Setter
    private boolean dying;
    @Setter
    private boolean poweredUp;
    @Setter
    private boolean canDoubleJump;

    public Player() {
        super();
        dying = poweredUp = false;
        this.direction = 1;
        this.moveSpeed=1.0f;
        this.baseMove=5;
    }

    public void addSpeedBoost(){
        moveSpeed = (moveSpeed*1.05f > 2) ? 2 : moveSpeed*1.05f;
        System.out.println("moveSpeed:"+moveSpeed);
    }

    public void minusSpeedBoost(){
        moveSpeed = (moveSpeed/1.05f < 0.5f) ? 0.5f : moveSpeed/1.05f;
        System.out.println("moveSpeed:"+moveSpeed);
    }

    public void movePlayerLeft(){
        direction = -1;
        this.SetCurrentFrame(3);
        movePlayer();
    }

    public void movePlayerRight(){
        direction = 1;
        this.SetCurrentFrame(2);
        movePlayer();
    }

    private void movePlayer(){

        Vector2D pos = this.GetPosition();
        System.out.println("pos1:" +pos.getX());
        pos.x += direction*baseMove*this.moveSpeed;

        System.out.println("moveSpeed:" +this.moveSpeed);
        System.out.println("baseMove:" +baseMove);
        System.out.println("direction:" +direction);
        System.out.println("added:" +direction*baseMove*this.moveSpeed);

        System.out.println("pos2:" +pos.getX());
        this.SetPosition(pos);

        System.out.println("pos:" +GetPosition().getX());
    }

    public Player(int id) {
        this();
        this.SetID(id);
    }
}
