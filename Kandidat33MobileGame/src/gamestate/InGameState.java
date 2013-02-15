package gamestate;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
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
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.LightControl;
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
    private Node inGameRootNode;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private InputManager inputManager;
    private ViewPort viewPort;
    private BulletAppState physics;
    private LinkedList<Geometry> platforms;
    private Material platformMaterial;
    private Material playerMaterial;
    private Box playerModel;
    private Box playerDeathModel;
    private Node playerNode;
    private CharacterControl playerCharacter;
    private Vector3f playerLightPosition;
    private SpotLight playerSpot;
    private Vector3f directionToPlayer;

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
        generateLevel();
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
        directionToPlayer = playerNode.getLocalTranslation().
                subtract(playerLightPosition);
        playerSpot.setDirection(directionToPlayer);


        if (playerNode.getLocalTranslation().y < -1) {
            playerDeath();
        }
    }
    
    private void playerDeath() {
        playerNode.getControl(CharacterControl.class).setEnabled(false);
        System.out.println("Game Over!");
        Spatial pG = playerNode.getChild("PlayerModel");

        playerNode.detachChild(pG);

                //detachChildNamed("PlayerModel");
        Geometry playerGeo = new Geometry("PlayerDeathModel", playerDeathModel);
        
        //playerGeo.setLocalTranslation(pG.getLocalTranslation());
        playerGeo.setMaterial(playerMaterial);
        playerNode.attachChild(playerGeo);
        

        setEnabled(false);
        //stateManager.detach(this);
    }

    private void generateLevel() {
        generateMaterials();
        generateModels();
        generatePlatforms();
        generatePlayer();
        initInputs();
        initCamera();
        initLights();


    }

    /**
     * Sets up the camera to follow the player.
     */
    private void initCamera() {

        /*
         * Set up a node a bit ahead of the player, to keep the the player
         * to the left of the screen.
         * Comment out this section and change camFocusNode to playerNode
         * to get player centered.
         */
        Node camFocusNode = new Node();
        camFocusNode.setLocalTranslation(playerNode.getLocalTranslation());
        playerNode.attachChild(camFocusNode);
        camFocusNode.move(15f, 0f, 0f);

        /*
         * Disable the default flyby camera. Hopefully this work even if some
         * other state has already disabled it.
         */
        this.app.getFlyByCamera().setEnabled(false);
        Camera cam = this.app.getCamera();

        ChaseCamera chaseCam;
        // Change camFocusNode to playerNode to center player
        chaseCam = new ChaseCamera(cam, camFocusNode, inputManager);

        // Set the style of camera chasing right
        chaseCam.setSmoothMotion(true);
        chaseCam.setTrailingEnabled(true);
        chaseCam.setDefaultDistance(50);
        // Set the camera to stay facing the side of the player
        chaseCam.setDefaultHorizontalRotation(-FastMath.DEG_TO_RAD * 265);
        chaseCam.setDefaultVerticalRotation(FastMath.DEG_TO_RAD * 10);
    }

    private void initLights() {
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-.5f, -.5f, -.5f).normalizeLocal());
        inGameRootNode.addLight(sun);

        playerSpot = new SpotLight();
        SpotLight backwardSpot = new SpotLight();
        playerSpot.setSpotRange(1000f);                           // distance
        playerSpot.setSpotInnerAngle(15f * FastMath.DEG_TO_RAD); // inner light cone (central beam)
        playerSpot.setSpotOuterAngle(35f * FastMath.DEG_TO_RAD); // outer light cone (edge of the light)
        playerSpot.setColor(ColorRGBA.White.mult(1.3f));         // light color

                playerLightPosition = new Vector3f(100f, 50f, 0f);
        playerSpot.setPosition(playerLightPosition);
        
        // find the direction to shine in
        directionToPlayer = playerNode.getLocalTranslation().
                subtract(playerLightPosition);
        playerSpot.setDirection(directionToPlayer);
                
        inGameRootNode.addLight(playerSpot);

        backwardSpot.setSpotRange(100f);                           // distance
        backwardSpot.setSpotOuterAngle(35f * FastMath.DEG_TO_RAD); // outer light cone (edge of the light)
        backwardSpot.setSpotInnerAngle(15f * FastMath.DEG_TO_RAD); // inner light cone (central beam)
        backwardSpot.setColor(ColorRGBA.White.mult(1.3f));         // light color
        backwardSpot.setPosition(new Vector3f(200f, 30f, 0f));    // shine from above end
        backwardSpot.setDirection(new Vector3f(-200f, -30f, 0f));             // shine along path


        //inGameRootNode.addLight(backwardSpot);
    }

    
    
    private void generateMaterials() {
        platformMaterial = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        platformMaterial.setBoolean("UseMaterialColors", true);
        platformMaterial.setColor("Ambient", ColorRGBA.Blue);
        platformMaterial.setColor("Diffuse", ColorRGBA.Blue);
        playerMaterial = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        playerMaterial.setBoolean("UseMaterialColors", true);
        playerMaterial.setColor("Ambient", ColorRGBA.Red);
        playerMaterial.setColor("Diffuse", ColorRGBA.Red);
    }

    private void generateModels() {
        playerModel = new Box(Vector3f.ZERO, 2f, 3f, 2f);
        playerModel.scaleTextureCoordinates(new Vector2f(1f, .5f));
        playerDeathModel = new Box(Vector3f.ZERO,1f, 1f, 1f);
        playerDeathModel.scaleTextureCoordinates(new Vector2f(1f, .5f));
    }

    private void generatePlatforms() {
        platforms = new LinkedList<Geometry>();
        // Create the first platform
        Geometry firstPlatform = new Geometry("Platform", new Box(Vector3f.ZERO, P.platformLength / 2, P.platformHeight, P.platformWidth));
        firstPlatform.setMaterial(platformMaterial);
        firstPlatform.setLocalTranslation(0, 0 - 0.1f, 0);
        platforms.add(0, firstPlatform);
        inGameRootNode.attachChild(firstPlatform);
        // Make the first platform physical
        RigidBodyControl tempControl = new RigidBodyControl(0.0f);
        firstPlatform.addControl(tempControl);
        physics.getPhysicsSpace().add(tempControl);
        // Generate the rest of the platforms
        Geometry temp;
        for (int i = 0; i < P.platformsPerLevel; i++) {
            temp = new Geometry("Platform", new Box(Vector3f.ZERO, P.platformLength / 2, P.platformHeight, P.platformWidth));
            temp.setMaterial(platformMaterial);
            System.out.println(platforms.getFirst().getLocalTranslation().x);
            temp.setLocalTranslation(platforms.getFirst().getLocalTranslation().x + 1.4f * P.platformLength,
                    platforms.getFirst().getLocalTranslation().y,
                    platforms.getFirst().getLocalTranslation().z);
            platforms.addFirst(temp);
            inGameRootNode.attachChild(platforms.getFirst());

            // Make the platforms physical
            tempControl = new RigidBodyControl(0.0f);
            temp.addControl(tempControl);
            physics.getPhysicsSpace().add(tempControl);
        }
    }

    private void generatePlayer() {
        Geometry playerGeo = new Geometry("PlayerModel", playerModel);
        playerGeo.setMaterial(playerMaterial);
        playerNode = new Node("PlayerNode");
        playerNode.attachChild(playerGeo);

        /**
         * Create a CharacterControl object
         */
        CapsuleCollisionShape shape = new CapsuleCollisionShape(2f, 2f);
        playerCharacter = new CharacterControl(shape, 0.05f);
        playerCharacter.setJumpSpeed(P.jump_speed);

        /**
         * Position the player
         */
        Vector3f vt = new Vector3f(-7, 2, 0);
        playerNode.setLocalTranslation(vt);

        inGameRootNode.attachChild(playerNode);



        playerNode.addControl(playerCharacter);
        physics.getPhysicsSpace().add(playerCharacter);
        Vector3f walkDirection = Vector3f.UNIT_X.multLocal(P.run_speed);

        playerCharacter.setWalkDirection(walkDirection);

    }

    /**
     * Sets up the input. Mouseclick jumps the character.
     */
    private void initInputs() {

        inputManager.addMapping("jump",
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "jump");
    }
    
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String binding, boolean value, float tpf) {
            if (binding.equals("jump")) {
                playerCharacter.jump();
            }
        }
    };
}
