package hu.uni.miskolc.iit.game.objects;

public interface FallenObject {

    void cleanupObject();

    int getScore();

    boolean update();

    boolean collisionDetection(Player player);
}
