package hu.uni.miskolc.iit.game.objects;

import hu.uni.miskolc.iit.config.AppConfig;
import hu.uni.miskolc.iit.engine.GameObject2D;
import hu.uni.miskolc.iit.engine.MusicManager;
import hu.uni.miskolc.iit.engine.math.Vector2D;
import hu.uni.miskolc.iit.engine.sound.AudioMaster;
import lombok.Getter;
import lombok.Setter;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Player extends GameObject2D{

    private final float leftSide=0f;
    private final float rightSide=AppConfig.appConfig().getWindow().getWidth()-64;
    private final float maxPlayerMoveSpeed=2f;
    private final float minPlayerMoveSpeed=0.5f;

    @Setter
    private int direction;

    @Getter
    private float baseMoveSpeed;
    @Getter
    private float actualMoveSpeed;

    private int baseMove;

    @Setter
    private boolean dying;
    @Setter
    private boolean poweredUp;
    @Setter
    private boolean canDoubleJump;

    @Getter
    private List<Booster> boosterList= new ArrayList<>();

    public Player() {
        super();
        dying = poweredUp = false;
        this.direction = 1;
        this.baseMoveSpeed =1.0f;
        this.actualMoveSpeed=1.0f;
        this.baseMove=5;
    }

    private void updateBoost()
    {
        actualMoveSpeed=baseMoveSpeed;
        for (Booster booster:boosterList.stream()
                .collect(Collectors.toList())) {
            if(booster.endUNIXTimeMillis>System.currentTimeMillis()) {
                actualMoveSpeed += booster.modifierValue/10;
            }
            else{
                boosterList.remove(booster);
            }
        }
    }


    public void addBooster(long durationSec, float modifierValue){
        boosterList.add(new Booster(durationSec,modifierValue){});
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
        updateBoost();
        if(this.dying==false){
        Vector2D pos = this.GetPosition();
        pos.x += direction*baseMove*this.actualMoveSpeed;
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
}
