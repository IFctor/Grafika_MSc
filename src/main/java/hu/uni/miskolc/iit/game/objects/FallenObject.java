package hu.uni.miskolc.iit.game.objects;

public interface FallenObject {

    void cleanupObject();

    int getScore();

    void update();

    void setVisible(boolean visible);

    boolean isVisible();

    boolean collisionDetection(Player player);

    int getLifeModifier();

    long getBoosterDurationSec();

    float getBoosterModifierValue();
}
