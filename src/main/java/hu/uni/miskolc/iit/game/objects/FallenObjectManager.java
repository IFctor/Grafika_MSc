package hu.uni.miskolc.iit.game.objects;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class FallenObjectManager {
    @Getter
    List<FallenObject> fallenObjectList = new ArrayList<>();

    public void update()
    {
        for (FallenObject fallenObject:
        fallenObjectList) {
            if(fallenObject.update()==true)
            {
                clean(fallenObject);
            }
        }
    }

    public void clean(FallenObject fallenObject)
    {
        fallenObject.cleanupObject();
        fallenObjectList.remove(fallenObject);
    }

    public void clean()
    {
        fallenObjectList.forEach(object -> object.cleanupObject());
    }

    public void addNewFallenObject(FallenObject fallenObject){
        fallenObjectList.add(fallenObject);
    }

    public int collisionDetection(Player player){
        int gainedPoints=0;
        for (FallenObject fallenObject:fallenObjectList) {
            if(fallenObject.collisionDetection(player)==true)
            {
                gainedPoints+=fallenObject.getScore();
                clean(fallenObject);
            }
        }
        return gainedPoints;
    }

}
