package hu.uni.miskolc.iit.game.managers;

import hu.uni.miskolc.iit.game.objects.FallenObject;
import hu.uni.miskolc.iit.game.objects.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FallenObjectManager {

    @Getter
    List<FallenObject> fallenObjectList = new ArrayList<>();

    int actualmaximumFallenObject = 1;
    int oldSeconds;

    public void update() {

        //Csak azokat updateljük amik látszódnak
        int countVisible = 0;
        for (FallenObject fallenObject :
                fallenObjectList) {
            if (fallenObject.isVisible()) {
                fallenObject.update();
                countVisible++;
            }
        }

        int newSeconds = new Date().getSeconds();

        if (Integer.compare(oldSeconds, newSeconds) != 0) {
            oldSeconds = newSeconds;
            if (countVisible < actualmaximumFallenObject) {
                newFallenObject();
            }
            incrementMaximumObject();
        }
    }

    private void incrementMaximumObject() {
        if (oldSeconds % 20 == 0) {
            actualmaximumFallenObject++;
        }
    }

    private void newFallenObject() {
        if (oldSeconds % 5 == 0) {
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

    public void clean() {
        fallenObjectList.forEach(object -> object.cleanupObject());
    }

    public void addNewFallenObject(FallenObject fallenObject) {
        fallenObjectList.add(fallenObject);
    }

    public void collisionDetection(Player player) {
        for (FallenObject fallenObject : fallenObjectList.stream()
                .filter(f -> f.isVisible())
                .collect(Collectors.toList())) {
            if (fallenObject.collisionDetection(player) == true) {
                player.addScore(fallenObject.getScore());
                player.addLife(fallenObject.getLifeModifier());
                player.addBooster(fallenObject.getBoosterDurationSec(), fallenObject.getBoosterModifierValue());
            }
        }
    }

    public void initFalleObjectManager(){
        actualmaximumFallenObject = 1;
        for (FallenObject fallenObject : fallenObjectList.stream()
                .filter(f -> f.isVisible())
                .collect(Collectors.toList()))  {
            fallenObject.setVisible(false);
        }
    }
}
