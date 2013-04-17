package state;

import audio.MusicNode;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.shadow.PssmShadowRenderer;
import com.jme3.system.Timer;
import control.HazardControl;
import control.PlayerControl;
import spatial.Player;
import variables.P;

/**
 * This state is activated to start the game. The class sets up  
 * the different <code>Node</code>s and attaches relevant controls to them.
 * 
 * The level/scene is setup using a <code>LevelGeneratingState</code> to continously 
 * generate scene-chunks whene the player moves. 
 * <br/><br/>
 * The player is setup with a
 * <code>PlayerControl</code> which keeps the player moving to the right and 
 * handles jump-events.
 * <br/><br/>
 * Background music is set to play.
 * <br/><br/>
 * Lights are added
 * <br/><br/>
 * The camera is set to follow the player with a <code>ChaseCam</code>
 * @author forssenm, dagson
 */
public class InGameState extends AbstractAppState{
    public static final String GAME_NODE = "Game Node";
    
    private SimpleApplication app;
    private Node gameNode;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private InputManager inputManager;
    private ViewPort viewPort;
    private BulletAppState physics;
    
    private Player player;
    
    private ChaseCamera chaseCam;
    
    private boolean gameOver = false;
    private float respawnDelay = 1.0f; // seconds
    private float respawnTimer = 0.0f; // seconds
    private LevelGeneratingState level;
    
    /**
     * This method initializes the the InGameState and thus getts the game 
     * ready for playing. That implies setting up the level, player, camera and 
     * music by using a combination of <code>Node</code>s and 
     * <code>Control</code>s.
     *
     * @see Node
     * @param stateManager
     * @param app
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.gameNode = new Node(GAME_NODE);
        this.app.getRootNode().attachChild(this.gameNode);
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort = this.app.getViewPort();
        this.physics = new BulletAppState();
        
        this.level = new LevelGeneratingState();
        
        this.stateManager.attach(level);
        this.stateManager.attach(physics);
        this.stateManager.attach(new RunningState());
        
        initPlayer();
        initCollisions();
        initCamera();
        initInputs();

        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-.5f, -.5f, -.5f).normalizeLocal());
        gameNode.addLight(sun);
        
        initAudio();
    }
    
    /**
     * This method creates a node for the player. Also the default player model 
     * is loaded and attached. A <code>PlayerControl</code> is attached to 
     * control the physics of the player. 
     */
    private void initPlayer() {
        player = new Player(assetManager);
        gameNode.attachChild(player);

        this.physics.getPhysicsSpace().addAll(player);
    }

    /**
     * This method creates a <code>MusicNode</code> for the background music,
     * loads the default music file and starts playing.
     */
    private void initAudio(){
        MusicNode musicNode = new MusicNode(assetManager, "Sound/Music/SpookyMusicForRunning.ogg");
        gameNode.attachChild(musicNode);
        musicNode.play();
    }
    
    /**
     * This method is for cleaning up everything created by this 
     * <code>GameState</code>.
     */
    @Override
    public void cleanup() {
        /* This implementation is incomplete 
         * If this class is keept as reference then things under the gameNode 
         * will not be garbarge collected. That might be intended or it might 
         * not be.
         */
        super.cleanup();
        this.app.getRootNode().detachChild(this.gameNode);
    }
    
         
    /**{inheritDoc}*/
    @Override
    public void update(float tpf) {
        if(!gameOver){
            if( player.getWorldTranslation().getY() < P.deathTreshold){
                this.chaseCam.setEnabled(false);
                this.gameOver = true;
            }
        }else{ // gameOver
            respawnTimer += tpf;
            if(respawnTimer > respawnDelay){
                Vector3f spawnPosition = player.getLocalTranslation();
                spawnPosition.x = 0.0f;
                spawnPosition.y = 20.0f;
                PlayerControl pc = player.getControl(PlayerControl.class); 
                pc.respawn(spawnPosition);
                
                level.cleanup();
                
                // Try again
                level.resetLevel();
                
                this.chaseCam.setEnabled(true);
                gameOver = false;
                respawnTimer = 0.0f;
            }
        }
        
        
    }

     
     /**
      * Initializes the camera.
      * After this, the camera follows the player, looking at them from
      * the right angle.
      */
     private void initCamera() {
        this.chaseCam = new ChaseCamera(this.app.getCamera(), this.player, this.inputManager);
        //this.chaseCam.setSmoothMotion(true);
        this.chaseCam.setTrailingEnabled(false);
        this.chaseCam.setDefaultHorizontalRotation(-FastMath.DEG_TO_RAD * 270);
        this.chaseCam.setDefaultVerticalRotation(FastMath.DEG_TO_RAD * 20);
        //Depth (z) distance from camera to target (player)
        this.chaseCam.setDefaultDistance(50);
        //Offset in x direction so that the target (player) is on the left half 
        //of the screen instead of the center of the screen.
        this.chaseCam.setLookAtOffset(new Vector3f(6f, 8f, 0f));
     }

    /**
     * Sets up the user inputs. Jump is triggered by spacebar and mouseclick;
     * on the phone all touch events are translated to clicks.
     */
    private void initInputs() {
        inputManager.addListener(player.getControl(PlayerControl.class), "jump");
    }

    private void initCollisions() {
        PhysicsCollisionListener physicsCollisionListener = new PhysicsCollisionListener() {
            public void collision(PhysicsCollisionEvent event) {
                if (event.getNodeA().getName().equals("player")
                        && event.getNodeB().getName().equals("hazard")) {
                    event.getNodeB().getControl(HazardControl.class).collideWithPlayer((Player)event.getNodeA());
                } else if (event.getNodeB().getName().equals("player")
                        && event.getNodeA().getName().equals("hazard")) {
                    event.getNodeA().getControl(HazardControl.class).collideWithPlayer((Player)event.getNodeB());
                }
                // should we ever want to detect collisions between other
                // combinations than player and hazard, use this:
                /*
                 if (event.getNodeA().getName().equals("player")
                 && event.getNodeB().getName().equals("nothazard")) {
                 final Spatial spatial = event.getNodeA();
                 System.out.println("foo");
                 } else if (event.getNodeB().getName().equals("player")
                 && event.getNodeA().getName().equals("nothazard")) {
                 final Spatial spatial = event.getNodeB();
                 System.out.println("bar");
                 }
                 */
            }
        };
        this.physics.getPhysicsSpace().addCollisionListener(physicsCollisionListener);     
    }
    
   
}
