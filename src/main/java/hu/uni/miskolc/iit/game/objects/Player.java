package hu.uni.miskolc.iit.game.objects;

import hu.uni.miskolc.iit.config.AppConfig;
import hu.uni.miskolc.iit.engine.GameObject2D;
import hu.uni.miskolc.iit.engine.MusicManager;
import hu.uni.miskolc.iit.engine.math.Vector2D;
import hu.uni.miskolc.iit.engine.sound.AudioMaster;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Player extends GameObject2D{

    private final float leftSide=0f;
    private final float rightSide=AppConfig.appConfig().getWindow().getWidth()-64;
    private final float maxPlayerMoveSpeed=2f;
    private final float minPlayerMoveSpeed=0.5f;

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
        moveSpeed = (moveSpeed*1.05f >= maxPlayerMoveSpeed) ? maxPlayerMoveSpeed : moveSpeed*1.05f;
        System.out.println("moveSpeed:"+moveSpeed);
    }

    public void minusSpeedBoost(){
        moveSpeed = (moveSpeed/1.05f <= minPlayerMoveSpeed) ? minPlayerMoveSpeed : moveSpeed/1.05f;
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
        if(this.dying==false){
        Vector2D pos = this.GetPosition();
        pos.x += direction*baseMove*this.moveSpeed;
        if (pos.x> rightSide)
        {pos.x=rightSide;}
        else if (pos.x<leftSide){pos.x=leftSide;}
        this.SetPosition(pos);
        }
    }

    public void die() {
        MusicManager.deathSound.play(AudioMaster.DEATH_SOUND);
        this.dying=true;
    }

    public Player(int id) {
        this();
        this.SetID(id);
    }
}
