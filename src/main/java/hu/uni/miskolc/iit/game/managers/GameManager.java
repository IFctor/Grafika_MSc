package hu.uni.miskolc.iit.game.managers;


import hu.uni.miskolc.iit.config.AppConfig;
import hu.uni.miskolc.iit.engine.*;
import hu.uni.miskolc.iit.engine.sound.AudioMaster;
import hu.uni.miskolc.iit.game.Renderer;
import hu.uni.miskolc.iit.game.objects.*;

import java.io.File;

import static org.lwjgl.glfw.GLFW.*;

public class GameManager implements IGameLogic {
    private static final String SYS_EXTENSION_SEPARATOR = ".";
    private static final String TEXTURES_LOCATION = AppConfig.appConfig().getTextures().getLocation() + File.separator;
    private static final String BACKGROUND_LOCATION = AppConfig.appConfig().getScenes().getBackground().getLocation() + File.separator;
    private static final String TEXTURES_EXTENSION = SYS_EXTENSION_SEPARATOR + AppConfig.appConfig().getTextures().getExtension();

    // Global Scene manager
    public static C2DSceneManager sceneManager;
    private final Renderer renderer;
    private C2DScene scene;

    // 2D GameObject items
    private Player playerObject;
    private Score scoreObject= new Score();

    private FallenObjectManager fallenObjectManager=new FallenObjectManager();

    private boolean gameRuning=false;


    public GameManager() {
        renderer = new Renderer();
        AudioMaster.init();
        AudioMaster.setListenerData();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        createNewGame();
    }

    private void createNewGame()
    {
        createPlayer();
        createFallenObjects();
        initScenes();
        initSound();
    }

    private void createFallenObjects(){
        fallenObjectManager.clean();
        fallenObjectManager=new FallenObjectManager();
        createNewGifts();
        createNewCoals();
        createNewCookies();
        createNewMilks();
    }

    private void initScenes() {
        sceneManager = new C2DSceneManager();
        scene = new C2DScene();

        // Create a background texture
        Texture2D background = new Texture2D();
        background.CreateTexture(TEXTURES_LOCATION + BACKGROUND_LOCATION + "layer_sd_01" + TEXTURES_EXTENSION);

        // Create a mountain layer
        Texture2D mountains = new Texture2D();
        mountains.CreateTexture(TEXTURES_LOCATION + BACKGROUND_LOCATION + "layer_sd_07" + TEXTURES_EXTENSION);

        // Create a cloud layer
        /*Texture2D cloud = new Texture2D();
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
        playerLayer.AddGameObject(playerObject.getLifeBar());
        for (FallenObject giftObject:fallenObjectManager.getFallenObjectList()) {
            playerLayer.AddGameObject((GameObject2D) giftObject);
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

    private void createNewGifts()
    {
        final String santaDirectory = "Present/";
        for (int i=1; i<=17;i++){
            CSprite presentTexture = new CSprite(TEXTURES_LOCATION + santaDirectory+"Present_"+i, 1, 200, 200);
            int plusScore=0;
            if(i<15) {
                plusScore=50;
            }
            Present presentObject = new Present(i * 10+plusScore);

            presentObject.AddFrame(presentTexture);
            fallenObjectManager.addNewFallenObject(presentObject);
        }
    }
    private void createNewCoals()
    {
        final String santaDirectory = "Coal/";
        for (int i=1; i<=11;i++){
            CSprite coalTexture = new CSprite(TEXTURES_LOCATION + santaDirectory+"Coal_"+i, 1, 200, 200);

            int modifierMult=1;
            if(i<9) {
                modifierMult=2;
            }
                Coal coalObject = new Coal(10*modifierMult,-2f*modifierMult);

            coalObject.AddFrame(coalTexture);
            fallenObjectManager.addNewFallenObject(coalObject);
        }
    }
    private void createNewCookies()
    {
        final String santaDirectory = "Boost/";
        for (int i=1; i<=6;i++){
            CSprite cookieTexture = new CSprite(TEXTURES_LOCATION + santaDirectory+"Cookie_"+i, 1, 200, 200);

            int addLife=0;
            if(i<5) {
                addLife=1;
            }
            Cookie coalObject = new Cookie(addLife,10,+2f);

            coalObject.AddFrame(cookieTexture);
            fallenObjectManager.addNewFallenObject(coalObject);
        }
    }

    private void createNewMilks()
    {
        final String santaDirectory = "Boost/";
        for (int i=1; i<=2;i++){
            CSprite milkTexture = new CSprite(TEXTURES_LOCATION + santaDirectory+"Milk_"+i, 1, 200, 200);

            Milk milkObject = new Milk(1,20,+2f);

            milkObject.AddFrame(milkTexture);
            fallenObjectManager.addNewFallenObject(milkObject);
        }
    }

    private void createPlayer(){

LifeBar lifeBar = createLifeBar();

        playerObject = new Player(lifeBar);
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

    private LifeBar createLifeBar(){
        LifeBar lifeBar=new LifeBar();
        for (int i=0; i<=5;i++) {
            CSprite life = new CSprite(TEXTURES_LOCATION + "Heart/" + "lifeBar_" + i, 1, 200, 200);
            lifeBar.AddFrame(life);
        }
        lifeBar.SetPosition(0,0);
        return lifeBar;
    }

    private void initSound(){
        MusicManager.gameMusic.setVolume(0.15f);
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
        if(gameRuning==true) {
            if (playerObject.getDirection() == 1) {
                playerObject.SetCurrentFrame(0);
            } else {
                playerObject.SetCurrentFrame(1);
            }

            if (window.isKeyPressed(GLFW_KEY_LEFT)) {
                playerObject.movePlayerLeft();
            } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
                playerObject.movePlayerRight();
            }
        }

        if(window.isKeyPressed(GLFW_KEY_F1)){
            //Pause/Continue game
            gameRuning=!gameRuning;
        }else if(window.isKeyPressed(GLFW_KEY_F2)){
            //New game
            createNewGame();
        }
    }

    @Override
    public void update(float interval) {
        if(gameRuning==true){
        fallenObjectManager.update();
        fallenObjectManager.collisionDetection(playerObject);
        gameRuning=!playerObject.isDying();
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
        fallenObjectManager.clean();

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
