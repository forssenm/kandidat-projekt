package state;

import audio.MusicNode;
import control.LevelControl;
import state.RunningState;
import control.RunningControl;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

/**
 * This class handles setting up the game. 
 *
 * @author forssenm
 */
public class InGameState extends AbstractAppState{
    public static final String GAME_NODE = "Game Node";
    public static final String LEVEL_NODE = "Level Node";
    
    private SimpleApplication app;
    private Node inGameRootNode;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private InputManager inputManager;
    private ViewPort viewPort;
    private BulletAppState physics;
    
    private Node player;
    
    private ChaseCamera chaseCam;
     
    
    /**
     * This method initializes the the InGameState
     *
     * @param stateManager
     * @param app
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.inGameRootNode = new Node();
        this.app.getRootNode().attachChild(this.inGameRootNode);
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort = this.app.getViewPort();
        this.physics = new BulletAppState();
        
        this.stateManager.attach(physics);
        this.stateManager.attach(new RunningState());

        
        
        initPlayer();
        initLevel();
        initCamera();
        initInputs();

        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.Green);
        sun.setDirection(new Vector3f(-.5f, -.5f, -.5f).normalizeLocal());
        inGameRootNode.addLight(sun);
        
        initAudio();
    }
   
    public void initLevel() {
        LevelControl levelControl = new LevelControl(
                assetManager, physics.getPhysicsSpace(), player);
        Node level = new Node();
        level.addControl(levelControl);
        inGameRootNode.attachChild(level);
    }
    
    public void initPlayer() {
        player = (Node)assetManager.loadModel("Models/ghost6anim/ghost6animgroups.j3o");
        inGameRootNode.attachChild(player);
        player.setLocalTranslation(0.0f, 3.0f, 0.0f);
        player.addControl(new RunningControl());
        this.physics.getPhysicsSpace().addAll(player);
    }

    private void initAudio(){
        MusicNode musicNode = new MusicNode(assetManager, "Sound/Music/SpookyMusicForRunning.ogg");
        inGameRootNode.attachChild(musicNode);
        musicNode.play();
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        this.app.getRootNode().detachChild(this.inGameRootNode);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            //Initiate the things that are needed when the state is active
            System.out.println("InGameState is now active");
        } else {
            //Remove the things not needed when the state is inactive
            System.out.println("InGameState is now inactive");
        }
    }
    
    /**{inheritDoc}*/
    @Override
    public void update(float tpf) {
        /* Does nothing */
    }

     
     /**
      * Initializes the camera.
      * After this, the camera follows the player, looking at them from
      * the right angle.
      */
     private void initCamera() {
        //this.app.getFlyByCamera().setEnabled(false);
        this.chaseCam = new ChaseCamera(this.app.getCamera(), this.player, this.inputManager);
        //this.chaseCam.setSmoothMotion(true);
        this.chaseCam.setTrailingEnabled(false);
        this.chaseCam.setDefaultHorizontalRotation(-FastMath.DEG_TO_RAD * 270);
        this.chaseCam.setDefaultDistance(50);
     }

    /**
     * Sets up the user inputs. Jump is triggered by
     */
    private void initInputs() {
        inputManager.addListener(player.getControl(RunningControl.class), "jump");
    }
}
