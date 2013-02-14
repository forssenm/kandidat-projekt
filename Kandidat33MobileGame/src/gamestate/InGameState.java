package gamestate;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.util.LinkedList;
import variables.P;

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
    
    // Refactor platform. 
    // Platform controller code might be in the InGameState 
    // but should follow a protocol.
    private LinkedList<Geometry> platforms; 
    private Material platformMaterial;
    
    private Player player;
    // Refactor player
    //private Box playerModel;
    //private Material playerMaterial;
    //private Node playerNode;
    //private CharacterControl playerCharacter;

    

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
        
        generateLevel();
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

    private void generateLevel() {
        generateMaterials();
        generatePlatforms();
        
        // Create player and attach it to the Scene Graph and Physics Space
        this.player = Player.createPlayer(
                new PlayerSceneObjectDataSource(this.assetManager));
        this.player.addToNode(rootNode);
        this.player.addToPhysicsSpace(physics.getPhysicsSpace());
        
        initInputs();
        
    }

    /*
     private ChaseCamera chaseCam;
     
     private void initCamera() {

     // Enable a chase cam for this target (typically the player).
     Camera cam = new Camera();
     chaseCam = new ChaseCamera(cam, playerNode, inputManager);
     chaseCam.setSmoothMotion(true);
     chaseCam.setTrailingEnabled(false);
     chaseCam.setDefaultHorizontalRotation(-FastMath.DEG_TO_RAD * 270);
     chaseCam.setDefaultDistance(50);
     }
     */

    private void generateMaterials() {
        platformMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        platformMaterial.setColor("Color", ColorRGBA.Blue);
        //Material playerMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //playerMaterial.setColor("Color", ColorRGBA.Red);
        //this.player.setMaterial(playerMaterial);
    }

    private void generatePlatforms() {
        platforms = new LinkedList<Geometry>();
        Geometry firstPlatform = new Geometry("Platform", new Box(Vector3f.ZERO, P.platformLength / 2, P.platformHeight, P.platformWidth));
        firstPlatform.setMaterial(platformMaterial);
        firstPlatform.setLocalTranslation(0, 0 - 0.1f, 0);
        platforms.add(0, firstPlatform);
        rootNode.attachChild(firstPlatform);
        Geometry platformGeometry;
        RigidBodyControl platformRigidBodyControll;
        for (int i = 0; i < P.platformsPerLevel; i++) {
            platformGeometry = new Geometry("Platform", new Box(Vector3f.ZERO, P.platformLength / 2, P.platformHeight, P.platformWidth));
            platformGeometry.setMaterial(platformMaterial);
            System.out.println(platforms.getFirst().getLocalTranslation().x);
            platformGeometry.setLocalTranslation(platforms.getFirst().getLocalTranslation().x + 1 * P.platformLength,
                    platforms.getFirst().getLocalTranslation().y,
                    platforms.getFirst().getLocalTranslation().z);
            platforms.addFirst(platformGeometry);
            rootNode.attachChild(platforms.getFirst());

            // Make the platform physical
            platformRigidBodyControll = new RigidBodyControl(0.0f);
            //platformRigidBodyControll.setKinematic(true);
            platformGeometry.addControl(platformRigidBodyControll);
            physics.getPhysicsSpace().add(platformRigidBodyControll);

        }
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
                player.characterControl.jump();
            }
        }
    };
}
