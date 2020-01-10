package hu.uni.miskolc.iit.game.managers;

import hu.uni.miskolc.iit.game.objects.FallenObject;
import hu.uni.miskolc.iit.game.objects.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FallenObjectManager {

    @Getter
    List<FallenObject> fallenObjectList = new ArrayList<>();

    int actualmaximumFallenObject=1;
    float oldSystemTime;

    public void update()
    {
        oldSystemTime=System.currentTimeMillis()/1000;

        //Csak azokat updateljük amik látszódnak
        int countVisible=0;
        for (FallenObject fallenObject:
        fallenObjectList) {
            if(fallenObject.isVisible())
            {
                fallenObject.update();
                countVisible++;
            }
        }
        System.out.println("countVisible: "+ countVisible);
        if(countVisible<actualmaximumFallenObject) {
            System.out.println("new init");
                newFallenObject();
            }
        incrementMaximumObject();

    }

    private void incrementMaximumObject(){

        if ((System.currentTimeMillis()/1000) % 30 == 0) {
            actualmaximumFallenObject++;
        }
    }
    public void newFallenObject()
    {
        if ((System.currentTimeMillis()/1000) % 10 == 0) {
            List<FallenObject> notVisible = fallenObjectList.stream()
                    .filter(f -> !f.isVisible())
                    .collect(Collectors.toList());

            if (notVisible.size() > 0) {
                Random rand = new Random();
                FallenObject randomElement = notVisible.get(rand.nextInt(notVisible.size()));
                randomElement.setVisible(true);

            }
        }
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
        for (FallenObject fallenObject:fallenObjectList.stream()
                .filter(f->f.isVisible())
                .collect(Collectors.toList())) {
            if(fallenObject.collisionDetection(player)==true)
            {
                gainedPoints+=fallenObject.getScore();
                fallenObject.setVisible(false);
            }
        }
        return gainedPoints;
    }

}
