package gamestate;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

/**
 * This class handles all the in-game things
 *
 * @author forssenm
 */
public class InGameState extends AbstractAppState {

    private SimpleApplication app;
    private Node rootNode;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private InputManager inputManager;
    private ViewPort viewPort;
    private BulletAppState physics;
    
    private PlatformController platformController;
    
    private Player player;
    

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
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort = this.app.getViewPort();
        this.physics = new BulletAppState();
        this.stateManager.attach(physics);
        
        setup();
    }

    @Override
    public void cleanup() {
        super.cleanup();
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

    @Override
    public void update(float tpf) {
    }

    private void setup() {
        PlatformFactory platformFactory = new PlatformFactory(
                this.assetManager,
                this.rootNode,
                this.physics.getPhysicsSpace());
        this.platformController = new PlatformController(platformFactory);
        
        
        // Create player and attach it to the Scene Graph and Physics Space
        this.player = new Player(new PlayerSceneObjectDataSource(this.assetManager));
        this.player.addToNode(rootNode);
        this.player.addToPhysicsSpace(physics.getPhysicsSpace());
        initCamera();
        initInputs();
    }
    
    
     private ChaseCamera chaseCam;
     
     private void initCamera() {
        this.app.getFlyByCamera().setEnabled(false);
        this.chaseCam = new ChaseCamera(this.app.getCamera(), this.player.spatial, this.inputManager);
        //this.chaseCam.setSmoothMotion(true);
        this.chaseCam.setTrailingEnabled(false);
        this.chaseCam.setDefaultHorizontalRotation(-FastMath.DEG_TO_RAD * 270);
        this.chaseCam.setDefaultDistance(25);
     }
     
    
    /**
     * Sets up the input. Spacebar jumps the character.
     */
    private void initInputs() {

        inputManager.addMapping("light",
                new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping("jump",
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "jump");
    }
    
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String binding, boolean value, float tpf) {
            if (binding.equals("jump")) {
                player.jump();
            }
        }
    };
}
