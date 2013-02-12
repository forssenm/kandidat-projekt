package gamestate;

import com.jme3.app.Application;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.InputManager;
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
    private LinkedList<Geometry> platforms;
    private Material platformMaterial;
    private Material playerMaterial;
    private Box playerModel;

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
        generateModels();
        generatePlatforms();
        generatePlayer();
    }

    private void generateMaterials() {
        platformMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        platformMaterial.setColor("Color", ColorRGBA.Blue);
        playerMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        playerMaterial.setColor("Color", ColorRGBA.Red);
    }
    
    private void generateModels() {
        playerModel = new Box(Vector3f.ZERO, 2f, 2f, 3f);
        playerModel.scaleTextureCoordinates(new Vector2f(1f, .5f));
    }

    private void generatePlatforms() {
        platforms = new LinkedList<Geometry>();
        Geometry firstPlatform = new Geometry("Platform", new Box(Vector3f.ZERO, P.platformLength / 2, P.platformHeight, P.platformWidth));
        firstPlatform.setMaterial(platformMaterial);
        firstPlatform.setLocalTranslation(0, 0 - 0.1f, 0);
        platforms.add(0, firstPlatform);
        rootNode.attachChild(firstPlatform);
        Geometry temp;
        RigidBodyControl tempControl;
        for (int i = 0; i < P.platformsPerLevel; i++) {
            temp = new Geometry("Platform", new Box(Vector3f.ZERO, P.platformLength / 2, P.platformHeight, P.platformWidth));
            temp.setMaterial(platformMaterial);
            System.out.println(platforms.getFirst().getLocalTranslation().x);
            temp.setLocalTranslation(platforms.getFirst().getLocalTranslation().x + 1 * P.platformLength,
                    platforms.getFirst().getLocalTranslation().y,
                    platforms.getFirst().getLocalTranslation().z);
            platforms.addFirst(temp);
            rootNode.attachChild(platforms.getFirst());

            // Make the platform physical
            tempControl = new RigidBodyControl(0.0f);
            temp.addControl(tempControl);
            physics.getPhysicsSpace().add(tempControl);

        }


        //Make the floor physical with mass 0.0f!

    }

    private void generatePlayer() {
         Geometry playerGeo = new Geometry("player", playerModel);
         playerGeo.setMaterial(playerMaterial);
         Node playerNode = new Node();
         playerNode.attachChild(playerGeo);
        
         /**
         * Create a CharacterControl object
         */
        CapsuleCollisionShape shape = new CapsuleCollisionShape(2f, 2f);
        CharacterControl playerCharacter = new CharacterControl(shape, 0.05f);
        playerCharacter.setJumpSpeed(P.jump_speed);

        /**
         * Position the player
         */
        Vector3f vt = new Vector3f(0, 10, 0);
        playerNode.setLocalTranslation(vt);

        rootNode.attachChild(playerNode);



        playerNode.addControl(playerCharacter);
        physics.getPhysicsSpace().add(playerCharacter);
        Vector3f walkDirection = Vector3f.UNIT_X.multLocal(P.run_speed);

        playerCharacter.setWalkDirection(walkDirection);
        
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
