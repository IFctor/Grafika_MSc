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
    private static final Integer WINDOW_WIDTH = AppConfig.appConfig().getWindow().getWidth();
    private static final Integer WINDOW_HEIGHT = AppConfig.appConfig().getWindow().getHeight();
    private static final Integer SCORE_MAX_DIGIT = AppConfig.appConfig().getPlayers().getScore().getMaxDigit();
    private static final Integer COALSCOUNT = AppConfig.appConfig().getPlayers().getFallenObject().getCoal().getCount();
    private static final Integer COOKIESCOUNT = AppConfig.appConfig().getPlayers().getFallenObject().getCookie().getCount();
    private static final Integer MILKSCOUNT = AppConfig.appConfig().getPlayers().getFallenObject().getMilk().getCount();
    private static final Integer PRESENTSSCOUNT = AppConfig.appConfig().getPlayers().getFallenObject().getPresent().getCount();


    final String COAL_DIRECTORY = AppConfig.appConfig().getPlayers().getFallenObject().getCoal().getLocation()+File.separator;
    final String PRESENT_DIRECTORY = AppConfig.appConfig().getPlayers().getFallenObject().getPresent().getLocation()+File.separator;
    final String COOKIE_DIRECTORY = AppConfig.appConfig().getPlayers().getFallenObject().getCookie().getLocation()+File.separator;
    final String MILK_DIRECTORY = AppConfig.appConfig().getPlayers().getFallenObject().getMilk().getLocation()+File.separator;
    final String SANTA_DIRECTORY = AppConfig.appConfig().getPlayers().getSanta().getLocation()+File.separator;
    final String SCORE_DIRECTORY = AppConfig.appConfig().getPlayers().getScore().getLocation()+File.separator;
    final String lifeBarDirectory = AppConfig.appConfig().getPlayers().getLifeBar().getLocation()+File.separator;

    // Global Scene manager
    public static C2DSceneManager sceneManager;
    private final Renderer renderer;
    private C2DScene scene;

    // 2D GameObject items
    private Player playerObject;

    private FallenObjectManager fallenObjectManager = new FallenObjectManager();

    private GameObject2D gameStatus;
    private boolean gameRuning = false;


    public GameManager() {
        renderer = new Renderer();
        AudioMaster.init();
        AudioMaster.setListenerData();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        createGameStatus();
        createNewGame();
    }

    private void createGameStatus(){
        gameStatus=new GameObject2D();
        CSprite startGame = new CSprite(TEXTURES_LOCATION +  "start_game", 1, 200, 200);
        CSprite pauseGame = new CSprite(TEXTURES_LOCATION +  "pause_game", 1, 200, 200);
        CSprite endGame = new CSprite(TEXTURES_LOCATION +  "game_over", 1, 200, 200);
        gameStatus.AddFrame(startGame);
        gameStatus.AddFrame(pauseGame);
        gameStatus.AddFrame(endGame);

        GameStatusHandler(0);
    }

    private void restartGame(){
        playerObject.initPlayer();
        fallenObjectManager.initFalleObjectManager();
    }

    private void createNewGame() {
        createPlayer();
        createFallenObjects();
        initScenes();
        initSound();
    }

    private ScoreBar createScoreBar() {
        ScoreBar result = new ScoreBar();
        for (int i = 0; i < SCORE_MAX_DIGIT; i++) {
            result.addScoreObject(createScore());
        }
        return result;
    }

    private Score createScore() {
        Score result = new Score();
        for (int i = 0; i <= 9; i++) {
            CSprite number = new CSprite(TEXTURES_LOCATION + SCORE_DIRECTORY + "num_" + i, 1, 200, 200);
            result.AddFrame(number);
        }
        return result;
    }

    private void createFallenObjects() {
        fallenObjectManager.clean();
        fallenObjectManager = new FallenObjectManager();
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
        mountains.CreateTexture(TEXTURES_LOCATION + BACKGROUND_LOCATION + "layer_sd_02" + TEXTURES_EXTENSION);

        // Create graphics layer
        C2DGraphicsLayer layer0 = new C2DGraphicsLayer();
        layer0.AddTexture(background);

        // Create graphics layer
        C2DGraphicsLayer layer1 = new C2DGraphicsLayer();
        layer1.AddTexture(mountains);

        C2DGraphicsLayer playerLayer = new C2DGraphicsLayer();
        playerLayer.AddGameObject(playerObject);
        playerLayer.AddGameObject(playerObject.getLifeBar());
        for (FallenObject fallenObject : fallenObjectManager.getFallenObjectList()) {
            playerLayer.AddGameObject((GameObject2D) fallenObject);
        }
        for (Score score : playerObject.getScoreBar().getScoreList()) {
            playerLayer.AddGameObject(score);
        }
        playerLayer.AddGameObject(gameStatus);



        // register layer at the scene
        scene.RegisterLayer(layer0);
        scene.RegisterLayer(layer1);
        scene.RegisterLayer(playerLayer);

        // Register scene at the manager
        sceneManager.RegisterScene(scene);
    }

    private void createNewGifts() {
        for (int i = 1; i <= PRESENTSSCOUNT; i++) {
            CSprite presentTexture = new CSprite(TEXTURES_LOCATION + PRESENT_DIRECTORY + "Present_" + i, 1, 200, 200);
            int plusScore = 0;
            if (i >= 15) {
                plusScore = 10;
            }
            Present presentObject = new Present(i + plusScore, i);

            presentObject.AddFrame(presentTexture);
            fallenObjectManager.addNewFallenObject(presentObject);
        }
    }

    private void createNewCoals() {
        for (int i = 1; i <= COALSCOUNT; i++) {
            CSprite coalTexture = new CSprite(TEXTURES_LOCATION + COAL_DIRECTORY + "Coal_" + i, 1, 200, 200);

            int modifierMult = 1;
            if (i > 7) {
                modifierMult = 2;
            }
            Coal coalObject = new Coal(10, -2f,modifierMult, i);

            coalObject.AddFrame(coalTexture);
            fallenObjectManager.addNewFallenObject(coalObject);
        }
    }

    private void createNewCookies() {
        for (int i = 1; i <= COOKIESCOUNT; i++) {
            CSprite cookieTexture = new CSprite(TEXTURES_LOCATION + COOKIE_DIRECTORY + "Cookie_" + i, 1, 200, 200);

            int addLife = 0;
            if (i < 5) {
                addLife = 1;
            }
            Cookie cookieObject = new Cookie(addLife, 10, +2f, i);

            cookieObject.AddFrame(cookieTexture);
            fallenObjectManager.addNewFallenObject(cookieObject);
        }
    }

    private void createNewMilks() {
        for (int i = 1; i <= MILKSCOUNT; i++) {
            CSprite milkTexture = new CSprite(TEXTURES_LOCATION + MILK_DIRECTORY + "Milk_" + i, 1, 200, 200);

            Milk milkObject = new Milk(1, 20, +2f, i);

            milkObject.AddFrame(milkTexture);
            fallenObjectManager.addNewFallenObject(milkObject);
        }
    }

    private void createPlayer() {

        LifeBar lifeBar = createLifeBar();

        playerObject = new Player(lifeBar,
                createScoreBar());

        CSprite frameRunRight = new CSprite(TEXTURES_LOCATION + SANTA_DIRECTORY + "Santa_Run_", 8, 200, 200);
        CSprite frameRunLeft = new CSprite(TEXTURES_LOCATION + SANTA_DIRECTORY + "Santa_Run_Left_", 8, 200, 200);
        CSprite idleRight = new CSprite(TEXTURES_LOCATION + SANTA_DIRECTORY + "Santa_Idle_", 10, 200, 200);
        CSprite idleLeft = new CSprite(TEXTURES_LOCATION + SANTA_DIRECTORY + "Santa_Idle_Left_", 10, 200, 200);
        CSprite deadRight = new CSprite(TEXTURES_LOCATION + SANTA_DIRECTORY + "Santa_Dead_", 17, 200, 200);
        deadRight.SetAnimationSpeed(5);
        CSprite deadLeft = new CSprite(TEXTURES_LOCATION + SANTA_DIRECTORY + "Santa_Dead_Left_", 17, 200, 200);
        deadLeft.SetAnimationSpeed(5);

        playerObject.AddFrame(idleRight);
        playerObject.AddFrame(idleLeft);
        playerObject.AddFrame(frameRunRight);
        playerObject.AddFrame(frameRunLeft);
        playerObject.AddFrame(deadRight);
        playerObject.AddFrame(deadLeft);

        playerObject.SetPosition(200, 600);
    }

    private LifeBar createLifeBar() {
        LifeBar lifeBar = new LifeBar();
        for (int i = 0; i <= 5; i++) {
            CSprite life = new CSprite(TEXTURES_LOCATION + lifeBarDirectory + "lifeBar_" + i, 1, 200, 200);
            lifeBar.AddFrame(life);
        }
        lifeBar.SetPosition(0, 0);
        return lifeBar;
    }

    private void initSound() {
        MusicManager.gameMusic.setVolume(0.15f);
        MusicManager.gameMusic.setLooping(true);
        MusicManager.gameMusic.init(AudioMaster.GAME_MUSIC);
        MusicManager.gameMusic.play();

        MusicManager.hohohoSound.setVolume(1f);
        MusicManager.hohohoSound.setLooping(false);
        MusicManager.hohohoSound.init(AudioMaster.HOHOHO_SOUND);

        MusicManager.deathSound.setVolume(1f);
        MusicManager.deathSound.setLooping(false);
        MusicManager.deathSound.init(AudioMaster.DEATH_SOUND);

        MusicManager.drinkSound.setVolume(1f);
        MusicManager.drinkSound.setLooping(false);
        MusicManager.drinkSound.init(AudioMaster.DRINK_SOUND);

        MusicManager.hitSound.setVolume(1f);
        MusicManager.hitSound.setLooping(false);
        MusicManager.hitSound.init(AudioMaster.HIT_SOUND);

        MusicManager.eatSound.setVolume(1f);
        MusicManager.eatSound.setLooping(false);
        MusicManager.eatSound.init(AudioMaster.EAT_SOUND);
    }

    @Override
    public void input(Window window) {
        if (gameRuning == true) {
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

        if (window.isKeyPressed(GLFW_KEY_F1)) {
            //Pause game
            GamePauseHandler(false);
        } else if (window.isKeyPressed(GLFW_KEY_F2)) {
            //Continue game
            GamePauseHandler(true);
        } else if (window.isKeyPressed(GLFW_KEY_F3)) {
            //New game
            NewGameHandler();
        }
    }

    private void GameStatusHandler(int frame)
    {
        gameStatus.SetCurrentFrame(frame);
        float x=(WINDOW_WIDTH-gameStatus.GetCurrentFrame().GetCurrentFrameTexture().GetWidth())/2;
        float y=(WINDOW_HEIGHT-gameStatus.GetCurrentFrame().GetCurrentFrameTexture().GetHeight())/2;
        gameStatus.SetPosition(x,y);
        gameStatus.setMVisible(true);
    }

    private void NewGameHandler()
    {
        GameStatusHandler(0);
        gameRuning=false;
        restartGame();
    }

    private void GameOverHandler()
    {
        gameRuning=false;
        GameStatusHandler(2);
    }

    private void GamePauseHandler(boolean continueGame)
    {
        if(continueGame){
            gameStatus.setMVisible(false);
        }else{
            GameStatusHandler(1);
        }
        gameRuning=continueGame;
    }

    @Override
    public void update(float interval) {
        if (gameRuning == true) {
            fallenObjectManager.update();
            fallenObjectManager.collisionDetection(playerObject);
            if(playerObject.isDying()==true)
            {
                GameOverHandler();
            }
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
        MusicManager.hohohoSound.delete();
        MusicManager.deathSound.delete();
        MusicManager.drinkSound.delete();
        MusicManager.hitSound.delete();

        AudioMaster.cleanUp();
    }
}
