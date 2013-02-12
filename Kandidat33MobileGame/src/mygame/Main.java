package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import java.util.List;

/**
 * test
 *
 * @author jonatankilhamn som testar git
 */
public class Main extends SimpleApplication implements ActionListener {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    /**
     * Prepare the Physics Application State (jBullet)
     */
    private BulletAppState bulletAppState;
    private CharacterControl playerCharacter;
    private static final Box player_model;
    private RigidBodyControl floor_phy;
    private static final Box platform_box;
    //private Geometry player;
    private Node player;
    private Node playerNode;
    private CameraNode camNode;
    private ChaseCamera chaseCam;
    private boolean light;
    private DirectionalLight sun;
    
    /**
     * dimensions used for bricks and wall
     */
    private static final float platform_length = 20f;
    private static final float platform_width = 5f;
    private static final float platform_height = 0.5f;
    private int level;
    private static final int platforms_per_level = 25;
    private static float level_length = platform_length * platforms_per_level;
    
    /**
     * Constants used for the player character
     */
    private float run_speed = 0.5f;
    private float jump_speed = 20f;
    
    /**
     * Prepare Materials
     */
    Material player_mat;
    Material stone_mat;
    Material floor_mat;
    private LevelSupply levelSupply;

    static {
        /**
         * Initialize the geometries;
         */
        player_model = new Box(Vector3f.ZERO, 2f, 2f, 3f);
        player_model.scaleTextureCoordinates(new Vector2f(1f, .5f));
        platform_box = new Box(Vector3f.ZERO, platform_length/2, platform_height, platform_width);
    }

    @Override
    public void simpleInitApp() {

        /**
         * Set up Physics Game
         */
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.getPhysicsSpace().enableDebug(assetManager);



        sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));

        rootNode.addLight(sun);
        light = true;


        /**
         * Initialize the scene, materials, inputs, and physics space
         */
        initInputs();
        initMaterials();
        player = initPlayer();

        levelSupply = new LevelSupply();

        initLevel();



        // Disable the default flyby cam
        flyCam.setEnabled(false);
        // Enable a chase cam for this target (typically the player).
        chaseCam = new ChaseCamera(cam, player, inputManager);
        chaseCam.setSmoothMotion(true);
        chaseCam.setTrailingEnabled(false);
        chaseCam.setDefaultHorizontalRotation(-FastMath.DEG_TO_RAD * 270);
        chaseCam.setDefaultDistance(50);
    }

    /**
     * Initialize the materials used in this scene.
     */
    public void initMaterials() {
        player_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        player_mat.setColor("Color", ColorRGBA.Red);

        stone_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        stone_mat.setColor("Color", ColorRGBA.Blue);

        floor_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex = assetManager.loadTexture("Textures/bluefire.jpg");

        floor_mat.setTexture("ColorMap", tex);
    }

    /**
     * Make a solid platform and add it to the scene.
     */
    private void makePlatform(float x, float y) {
        Geometry platform_geo = new Geometry("Platform", platform_box);
        platform_geo.setMaterial(floor_mat);
        platform_geo.setLocalTranslation(x, y - 0.1f, 0);
        rootNode.attachChild(platform_geo);
        /* Make the floor physical with mass 0.0f! */
        floor_phy = new RigidBodyControl(0.0f);
        platform_geo.addControl(floor_phy);
        bulletAppState.getPhysicsSpace().add(floor_phy);
    }

    /**
     * Creates a platform from a PlatformPosition.
     * @param p The PlatformPosition containing the position of the new
     * platform, relative to the start of the level.
     */
    private void makePlatform(PlatformPosition p) {
        float offset = level_length * p.getLevel();
        float x = offset + platform_length * p.getX();
        float y = platform_length * p.getY();

        makePlatform(x, y);
    }

    /**
     * Creates the first level.
     */
    public void initLevel() {
        List<PlatformPosition> platforms = levelSupply.getLevel(0);
        for (PlatformPosition p : platforms) {
            makePlatform(p);
        }
    }

    /**
     * This method creates the player.
     */
    public Node initPlayer() {

        Vector3f vt =
                new Vector3f(0, 10, 0);

        
         Geometry playerGeo = new Geometry("player", player_model);
         playerGeo.setMaterial(player_mat);
         Node playerNode = new Node();
         playerNode.attachChild(playerGeo);
        
         /**
         * Create a CharacterControl object
         */
        CapsuleCollisionShape shape = new CapsuleCollisionShape(2f, 3f);
        playerCharacter = new CharacterControl(shape, 0.05f);
        playerCharacter.setJumpSpeed(jump_speed);



        //Node playerNode = (Node) assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");


        /**
         * Position the player
         */
        playerNode.setLocalTranslation(vt);
        //playerNode.setLocalScale(0.05f);
        //playerGeo.rotate(0f, 1f, 0f);

        rootNode.attachChild(playerNode);



        playerNode.addControl(playerCharacter);
        bulletAppState.getPhysicsSpace().add(playerCharacter);
        Vector3f walkDirection = Vector3f.UNIT_X.multLocal(run_speed);

        playerCharacter.setWalkDirection(walkDirection);
        return playerNode;

    }

    @Override
    public void simpleUpdate(float tpf) {
        if (player.getWorldTranslation().getX()
                > 2 * level * platforms_per_level * platform_length) {
            level++;

        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    /**
     * Setup the input. Spacebar jumps the character.
     */
    private void initInputs() {
        /*
         inputManager.addMapping("left",
         new KeyTrigger(KeyInput.KEY_A));
         inputManager.addMapping("right",
         new KeyTrigger(KeyInput.KEY_D));
         */
                 inputManager.addMapping("light",
         new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping("jump",
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, /*"right", "left",*/ "jump", "light");
    }

    public void onAction(String binding, boolean value, float tpf) {

        if (binding.equals("jump")) {
            playerCharacter.jump();
        }
        if (binding.equals("light")) {
            if (light) {
            rootNode.removeLight(sun);
            
            } else {
                rootNode.addLight(sun);
            }
            light = !light;
        }
    }
}
