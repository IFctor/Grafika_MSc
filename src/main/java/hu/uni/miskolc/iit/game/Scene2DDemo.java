package hu.uni.miskolc.iit.game;


import hu.uni.miskolc.iit.config.AppConfig;
import hu.uni.miskolc.iit.engine.GameEngine;
import hu.uni.miskolc.iit.engine.IGameLogic;
import hu.uni.miskolc.iit.game.managers.GameManager;

/**
 * Simple tutorial for 2D texture loading and rendering
 *
 * @author Mileff Peter
 * <p>
 * University of Miskolc
 */
public class Scene2DDemo {

    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new GameManager();
            GameEngine gameEng = new GameEngine(AppConfig.appConfig().getApplication().getName(), AppConfig.appConfig().getWindow().getWidth(), AppConfig.appConfig().getWindow().getHeight(), AppConfig.appConfig().getWindow().isVSync(), gameLogic);
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}