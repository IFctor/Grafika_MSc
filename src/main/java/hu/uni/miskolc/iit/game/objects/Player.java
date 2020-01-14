package hu.uni.miskolc.iit.game.objects;

import hu.uni.miskolc.iit.config.AppConfig;
import hu.uni.miskolc.iit.engine.GameObject2D;
import hu.uni.miskolc.iit.engine.MusicManager;
import hu.uni.miskolc.iit.engine.math.Vector2D;
import hu.uni.miskolc.iit.engine.sound.AudioMaster;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Player extends GameObject2D{

    private final float leftSide=0f;
    private final float rightSide=AppConfig.appConfig().getWindow().getWidth()-64;
    private final float maxPlayerMoveSpeed=2f;
    private final float minPlayerMoveSpeed=0.5f;
    private final int maximumLife=5;
    private final int minimumLife=0;

    private int actualScore;

    @Setter
    private int direction;

    @Getter
    private float baseMoveSpeed;
    @Getter
    private float actualMoveSpeed;

    private int baseMove;

    @Setter
    private boolean dying;

    private int actualLife;

    @Getter
    private LifeBar lifeBar;

    @Getter
    private List<Booster> boosterList= new ArrayList<>();

    public Player(LifeBar lifeBar) {
        super();
        dying = false;
        this.direction = 1;
        this.baseMoveSpeed =1.0f;
        this.actualMoveSpeed=1.0f;
        this.baseMove=5;
        actualLife=5;
        this.lifeBar=lifeBar;
        this.lifeBar.SetCurrentFrame(actualLife);
        this.actualScore=0;

    }

    public void addScore(int score){
        this.actualScore+=score;
    }
    public void setLife(int life){
        if(life>=minimumLife && life <=maximumLife)
        {actualLife=life;}
        else {
            actualLife=0;
        }
        setLifeBarToCurrentLife();
    }

    public void addLife(int life){
        if(this.dying==true)
        {
            this.actualLife=0;
        }
        else {
            this.actualLife += life;
        }
        setLifeBarToCurrentLife();
    }

    private void setLifeBarToCurrentLife(){

        if(actualLife<minimumLife){actualLife=minimumLife;}
        if(actualLife>maximumLife){actualLife=maximumLife;}
        if(actualLife==0){this.die();}
        this.lifeBar.SetCurrentFrame(actualLife);
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

    @Override
    public void cleanUp(){
        lifeBar.cleanUp();
        super.cleanUp();
    }
}
