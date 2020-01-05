package hu.uni.miskolc.iit.game;


import hu.uni.miskolc.iit.config.AppConfig;
import hu.uni.miskolc.iit.engine.*;
import hu.uni.miskolc.iit.engine.math.Vector2D;
import hu.uni.miskolc.iit.engine.sound.AudioMaster;
import hu.uni.miskolc.iit.game.objects.Player;
import hu.uni.miskolc.iit.game.objects.Present;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class DummyGame implements IGameLogic {
    private static final String SYS_EXTENSION_SEPARATOR = ".";
    private static final String TEXTURES_LOCATION = AppConfig.appConfig().getTextures().getLocation() + File.separator;
    private static final String BACKGROUND_LOCATION = AppConfig.appConfig().getScenes().getBackground().getLocation() + File.separator;
    private static final String TEXTURES_EXTENSION = SYS_EXTENSION_SEPARATOR + AppConfig.appConfig().getTextures().getExtension();


    // Global Scene manager
    public static C2DSceneManager sceneManager;
    private final Renderer renderer;
    private int direction = 0;
    // 2D GameObject items
    private Player playerObject;
    private List<Present> giftObjectList = new ArrayList<>();
    private C2DScene scene;

    public DummyGame() {
        renderer = new Renderer();
        AudioMaster.init();
        AudioMaster.setListenerData();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        createPlayer(window);
        createNewGift();
        initScenes();
        initSound();
    }
    private void initScenes() {
        sceneManager = new C2DSceneManager();
        scene = new C2DScene();

        // Create a background texture
        Texture2D background = new Texture2D();
        background.CreateTexture(TEXTURES_LOCATION + BACKGROUND_LOCATION + "layer_sd_01" + TEXTURES_EXTENSION);

        // Create a cloud layer
        Texture2D mountains = new Texture2D();
        mountains.CreateTexture(TEXTURES_LOCATION + BACKGROUND_LOCATION + "layer_sd_07" + TEXTURES_EXTENSION);

        // Create a mountain layer
        /*Texture2D mountains = new Texture2D();
        mountains.CreateTexture(TEXTURES_LOCATION + BACKGROUND_LOCATION + "layer_sd_03" + TEXTURES_EXTENSION);*/

        // Create a tree layer
        Texture2D trees = new Texture2D();
        trees.CreateTexture(TEXTURES_LOCATION + BACKGROUND_LOCATION + "layer_sd_04" + TEXTURES_EXTENSION);

        // Create a ground layer
        Texture2D ground = new Texture2D();
        ground.CreateTexture(TEXTURES_LOCATION + BACKGROUND_LOCATION + "layer_sd_05" + TEXTURES_EXTENSION);

        // Create graphics layer
        C2DGraphicsLayer layer0 = new C2DGraphicsLayer();
        layer0.AddTexture(background);

        // Create graphics layer
        /*C2DGraphicsLayer layer1 = new C2DGraphicsLayer();
        layer1.AddTexture(clouds);*/

        // Create graphics layer
        C2DGraphicsLayer layer2 = new C2DGraphicsLayer();
        layer2.AddTexture(mountains);

        /*C2DGraphicsLayer layer3 = new C2DGraphicsLayer();
        layer3.AddTexture(trees);*/

        /*C2DGraphicsLayer layer4 = new C2DGraphicsLayer();
        layer4.AddTexture(ground);*/

        C2DGraphicsLayer playerLayer = new C2DGraphicsLayer();
        playerLayer.AddGameObject(playerObject);
        for (GameObject2D giftObject:giftObjectList) {
            playerLayer.AddGameObject(giftObject);
        }

        // register layer at the scene
        scene.RegisterLayer(layer0);
        //scene.RegisterLayer(layer1);
        scene.RegisterLayer(layer2);
        //scene.RegisterLayer(layer3);
        //scene.RegisterLayer(layer4);
        scene.RegisterLayer(playerLayer);

        // Register scene at the manager
        sceneManager.RegisterScene(scene);
    }

    private void createNewGift()
    {
        final String santaDirectory = "Present/";


        for (int i=1; i<=8;i++){

        CSprite presentTexture = new CSprite(TEXTURES_LOCATION + santaDirectory+"Present_"+i, 1, 200, 200);

            Present presentObject = new Present();
        presentObject.AddFrame(presentTexture);
        giftObjectList.add(presentObject);
        }
    }
    private void createPlayer(Window window){
        playerObject = new Player();
        final String santaDirectory = "Santa_sprites/";

        CSprite frameRunRight = new CSprite(TEXTURES_LOCATION + santaDirectory+"Santa_Run_", 8, 200, 200);
        CSprite frameRunLeft = new CSprite(TEXTURES_LOCATION + santaDirectory+"Santa_Run_Left_", 8, 200, 200);
        CSprite idleRight = new CSprite(TEXTURES_LOCATION + santaDirectory+"Santa_Idle_", 10, 200, 200);
        CSprite idleLeft = new CSprite(TEXTURES_LOCATION + santaDirectory+"Santa_Idle_Left_", 10, 200, 200);

        playerObject.AddFrame(idleRight);
        playerObject.AddFrame(idleLeft);
        playerObject.AddFrame(frameRunRight);
        playerObject.AddFrame(frameRunLeft);

        playerObject.SetPosition(200, 600);
    }

    private void initSound(){
        MusicManager.gameMusic.setVolume(0.64f);
        MusicManager.gameMusic.setLooping(true);
        MusicManager.gameMusic.play(AudioMaster.GAME_MUSIC);

        MusicManager.winMusic.setVolume(0.64f);
        MusicManager.winMusic.setLooping(false);
        MusicManager.jumpSound.setVolume(1f);
        MusicManager.jumpSound.setLooping(false);
        MusicManager.deathSound.setVolume(1f);
        MusicManager.deathSound.setLooping(false);
        MusicManager.fireSound.setVolume(1f);
        MusicManager.fireSound.setLooping(false);
        MusicManager.effectsSound.setVolume(1f);
        MusicManager.effectsSound.setLooping(false);
        MusicManager.hitSound.setVolume(1f);
        MusicManager.hitSound.setLooping(false);
    }
    @Override
    public void input(Window window) {
        if (direction == 1) {
            playerObject.SetCurrentFrame(0);
        } else {
            playerObject.SetCurrentFrame(1);
        }

        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            playerObject.movePlayerLeft();
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            playerObject.movePlayerRight();
        }


        if (window.isKeyPressed(GLFW_KEY_Q)) {
            playerObject.addSpeedBoost();
        } else if (window.isKeyPressed(GLFW_KEY_W)) {
            playerObject.minusSpeedBoost();
        }
    }

    @Override
    public void update(float interval) {
        for (Present actualGift:
             giftObjectList) {
            actualGift.movePresent();
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        playerObject.cleanUp();

        MusicManager.gameMusic.delete();
        MusicManager.winMusic.delete();
        MusicManager.jumpSound.delete();
        MusicManager.deathSound.delete();
        MusicManager.fireSound.delete();
        MusicManager.effectsSound.delete();
        MusicManager.hitSound.delete();

        AudioMaster.cleanUp();
    }
}
