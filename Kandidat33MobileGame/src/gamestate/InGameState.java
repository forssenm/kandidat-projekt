package gamestate;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.LightControl;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.shadow.PssmShadowRenderer;
import java.util.LinkedList;
import java.util.Random;
import variables.P;

/**
 * This class handles all the in-game things
 *
 * @author forssenm
 */
public class InGameState extends AbstractAppState {

    private SimpleApplication app;
    private Node inGameRootNode;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private InputManager inputManager;
    private ViewPort viewPort;
    private BulletAppState physics;
    
    private PlatformController platformController;
    
    private Player player;
    
    
    private AudioNode audioBackground; 
    
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
        
        setup();
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

    @Override
    public void update(float tpf) {
        //directionToPlayer = player.getSpatial().getLocalTranslation().
        //        subtract(playerLightPosition);
        //playerSpot.setDirection(directionToPlayer);
        
        //Check if the first platform can be moved
        if(player.getSpatial().getLocalTranslation().x - platformController.platforms.getFirst().getPlatformX() > 10) {
            Random random = new Random();
            int randomNumber = (random.nextInt(9)-4);
            Platform newPlatform = platformController.platforms.getFirst();
            Platform previousPlatform = platformController.platforms.getLast();
            Vector3f newPosition = previousPlatform.getPlatformPosition().add(
                    new Vector3f(2*P.platformLength + P.platformDistance,randomNumber,0));
            newPlatform.setPlatformPosition(newPosition);
            platformController.platforms.addLast(newPlatform);
            platformController.platforms.removeFirst();
        }
    }

    private void setup() {
        inGameRootNode.setShadowMode(ShadowMode.Off);

        PlatformFactory platformFactory = new PlatformFactory(
                this.assetManager,
                this.inGameRootNode,
                this.physics.getPhysicsSpace());
        this.platformController = new PlatformController(platformFactory);


        // Create player and attach it to the Scene Graph and Physics Space
        this.player = new Player(new PlayerSceneObjectDataSource(this.assetManager));
        this.player.addToNode(inGameRootNode);
        this.player.addToPhysicsSpace(physics.getPhysicsSpace());
        initCamera();
        initInputs();
        initLights();
        initAudio();

    }
    
    
     private ChaseCamera chaseCam;
     
     private void initCamera() {
        this.app.getFlyByCamera().setEnabled(false);
        this.chaseCam = new ChaseCamera(this.app.getCamera(), this.player.spatial, this.inputManager);
        //this.chaseCam.setSmoothMotion(true);
        this.chaseCam.setTrailingEnabled(false);
        this.chaseCam.setDefaultHorizontalRotation(-FastMath.DEG_TO_RAD * 270);
        this.chaseCam.setDefaultDistance(50);
     }
     


    private void initLights() {
        
        /*
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.Green);
        sun.setDirection(new Vector3f(-.5f, -.5f, -.5f).normalizeLocal());
        
        inGameRootNode.addLight(sun);
        */ 
        
        SpotLight playerSpot = new SpotLight();
        playerSpot.setSpotRange(1000f);                           // distance
        playerSpot.setSpotInnerAngle(4f * FastMath.DEG_TO_RAD); // inner light cone (central beam)
        playerSpot.setSpotOuterAngle(20f * FastMath.DEG_TO_RAD); // outer light cone (edge of the light)
        playerSpot.setColor(ColorRGBA.White.mult(1.3f));         // light color

        ShadowedSpotlightControl ssc = new ShadowedSpotlightControl(playerSpot);
        player.getSpatial().addControl(ssc);
        ssc.setDefaultPositionOffset(new Vector3f(-10,50,0));

        inGameRootNode.addLight(playerSpot);

        PssmShadowRenderer shadowRenderer = new PssmShadowRenderer(assetManager, 512,3);
        shadowRenderer.setDirection(new Vector3f(0, -1, 0).normalizeLocal());
        shadowRenderer.setLambda(0.55f);
        shadowRenderer.setShadowIntensity(0.6f);
        shadowRenderer.setCompareMode(PssmShadowRenderer.CompareMode.Software);
        shadowRenderer.setFilterMode(PssmShadowRenderer.FilterMode.Dither);

        viewPort.addProcessor(shadowRenderer);
        ssc.setShadowRenderer(shadowRenderer);

    }


    /**
     * Sets up the input. Mouseclick jumps the character.
     */
    private void initInputs() {
        inputManager.addMapping("jump",
                new KeyTrigger(KeyInput.KEY_SPACE));
        
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
    
    private void initAudio(){
        this.audioBackground = new AudioNode(assetManager, "Sound/Music/SpookyMusicForRunning.ogg", false);
        this.audioBackground.setLooping(true);  // activate continuous playing
        this.audioBackground.setPositional(false);
        this.audioBackground.setLocalTranslation(Vector3f.ZERO.clone());
        this.audioBackground.setVolume(3);
        this.inGameRootNode.attachChild(this.audioBackground);
        this.audioBackground.play(); // play continuously!
    }
}
