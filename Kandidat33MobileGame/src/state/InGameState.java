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
import com.jme3.effect.ParticleEmitter;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.post.Filter;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import control.PlayerControl;
import control.PlayerInteractorControl;
import control.SpotlightControl;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.PssmShadowRenderer;
import control.PlayerControl;
import control.PlayerInteractorControl;
import filters.AmbientOcclusionFilter;
import filters.BuiltInSSAO;
import filters.BuiltInSSAO_intervals;
import java.util.LinkedList;
import java.util.List;
import main.Main;
import spatial.Player;
import spatial.StandardParticleEmitter;
import spatial.Torch;
import spatial.hazard.LinearFireball;
import spatial.hazard.SingleShotWizard;
import variables.EffectSettings;
import variables.EffectSettings.AmbientOcclusion;
import variables.EffectSettings.Shadows;
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
 * Lights are added.
 * <br/><br/>
 * The camera is set to follow the player with a <code>ChaseCam</code>
 * @author forssenm, dagson
 */
public class InGameState extends AbstractAppState {

    public static final String GAME_NODE = "Game Node";

    private Main app;
    private Node gameNode;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private InputManager inputManager;
    private ViewPort viewPort;
    private BulletAppState physics;
    private Player player;
    private ChaseCamera chaseCam;
    private boolean gameOver = false;
    private LevelGeneratingState level;
    private DirectionalLight sun;
    private boolean startInvulnerable;
    private boolean stopInvulnerable;
    private AmbientOcclusionFilter aof;
    private BuiltInSSAO_intervals builtInSSAO;
    private DirectionalLightShadowRenderer shadows;
    
    /**
     * This method initializes the the InGameState and thus gets the game ready
     * for playing. That implies setting up the level, player, camera and music
     * by using a combination of
     * <code>Node</code>s and
     * <code>Control</code>s.
     *
     * @see Node
     * @param stateManager
     * @param app
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Main) app;
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
        //physics.setDebugEnabled(true); //
        this.stateManager.attach(new RunningState());

        initPlayer();
        initCollisions();
        initCamera();
        initInputs();

        sun = new DirectionalLight();
        sun.setColor(P.sunColor);
        //sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-.5f, -.5f, -.5f).normalizeLocal());
        gameNode.addLight(sun);

        initAudio();
        if (EffectSettings.shadows == EffectSettings.shadows.DIRECTIONAL) {
            this.refreshShadows();
        }
        
        if (EffectSettings.ambientOcclusion == AmbientOcclusion.FULL_POST_PROCESSING || EffectSettings.ambientOcclusion == AmbientOcclusion.INTERVAL_POST_PROCESSING) {
            initAO();
        }
                
        
    }
    /**
     * This method creates a node for the player. Also the default player model
     * is loaded and attached. A
     * <code>PlayerControl</code> is attached to control the physics of the
     * player.
     */
    private void initPlayer() {
        player = new Player(assetManager, this);
        gameNode.attachChild(player);

        this.physics.getPhysicsSpace().addAll(player);
        
        if (P.usePlayerSpot) {
        SpotLight playerSpot = new SpotLight();
        playerSpot.setColor(ColorRGBA.DarkGray);
        playerSpot.setSpotRange(100);
        playerSpot.setSpotOuterAngle(FastMath.DEG_TO_RAD * 25f);
        playerSpot.setSpotInnerAngle(FastMath.DEG_TO_RAD * 10f);
        playerSpot.setName("playerspot");
        gameNode.addLight(playerSpot);
        SpotlightControl playerSpotControl = new SpotlightControl(playerSpot);
        player.addControl(playerSpotControl);
        }
        
    }

    /**
     * This method creates a
     * <code>MusicNode</code> for the background music, loads the default music
     * file and starts playing.
     */
    private void initAudio() {
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
    
    private void initAO() {
        //aof = new AmbientOcclusionFilter();
        Filter testFilter = null;
        if (EffectSettings.ambientOcclusion == AmbientOcclusion.INTERVAL_POST_PROCESSING) {
            builtInSSAO = new BuiltInSSAO_intervals(2, 5, 0.4f, 0.02f);
            testFilter = builtInSSAO;
        } else if (EffectSettings.ambientOcclusion == AmbientOcclusion.FULL_POST_PROCESSING) {
            testFilter = new BuiltInSSAO(2, 5, 0.4f, 0.02f);
        }
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(testFilter);
        viewPort.addProcessor(fpp);
        //Filter testFilter = new SSAOFilter(4, 3, 0.2f, 0.1f);
        //Filter testFilter = new SSAOFilter(2, 5, 0.4f, 0.02f);
        //Filter testFilter = new BuiltInSSAO(2, 5, 0.4f, 0.02f);
        //Filter testFilter = aof;
        //Filter testFilter = builtInSSAO;
        //fpp.addFilter(testFilter);
        //viewPort.addProcessor(fpp);
    }

    /*
     * this implementation does no longer match the structure of the scene graph
     * see LevelControl for current structure
     */
    private void updateAOIntervals() {
        Vector3f playerCenter = viewPort.getCamera().getScreenCoordinates(player.getWorldTranslation());
        List<Spatial> movingObjects = new LinkedList<Spatial>();
        int margin = 50;
        
        try {
            Node levelNode = (Node)gameNode.getChild("Level Node");
            movingObjects = ((Node)levelNode.getChild("wizards")).getChildren();
        } catch (NullPointerException e) {
            System.out.println(e);
        }
        
        Vector4f[] values = new Vector4f[1 + movingObjects.size()];
        values[0] = new Vector4f(playerCenter.x - margin, playerCenter.x + margin, playerCenter.y - margin / 2, playerCenter.y + margin * 1.5f);

        for (int i = 1; i < values.length; i++) {
            Vector3f center = viewPort.getCamera().getScreenCoordinates(movingObjects.get(i - 1).getWorldTranslation());
            values[i] = new Vector4f(center.x - margin, center.x + margin, center.y - margin, center.y + margin);
        }
        
        //aof.updateIntervals(values);
        builtInSSAO.updateIntervals(values);
        
    }
    
    private float gameTime;
    private int difficultyLevel;
    private float lightShiftTime;
    
    /**
     * {inheritDoc}
     */
    @Override
    public void update(float tpf) {
        if (EffectSettings.ambientOcclusion == AmbientOcclusion.INTERVAL_POST_PROCESSING) {
            this.updateAOIntervals();
        }
        
        if (startInvulnerable || stopInvulnerable) {
            if (startInvulnerable) {
                lightShiftTime += tpf;
                if (lightShiftTime > 0.5f) {
                    startInvulnerable = false;
                }
            } else {
                lightShiftTime -= tpf;
                if (lightShiftTime < 0f) {
                    stopInvulnerable = false;
                }
            }
            sun.setColor(P.sunColor.mult(1 + (lightShiftTime / 0.5f)));
        }
        
        if (!gameOver) {
            // check for game over
            if (player.getWorldTranslation().getY() < P.deathTreshold) {
                this.gameOver();// = true;
            }
            // check for difficulty increase
            gameTime += tpf;
            if (gameTime > difficultyLevel*3f) {
                difficultyLevel++;
                if (P.speedFactor < P.maxSpeedFactor) {
                    P.speedFactor += 0.05f;
                    player.getControl(PlayerControl.class).setSpeedFactor(P.speedFactor);
                }
            }
            
        } else { // gameOver
            // wait until player has fallen down
            if (player.getWorldTranslation().getY() < P.deathTreshold - 30) {
                this.setEnabled(false);
            }
        }
    }
    
    private void gameOver() {
        gameOver = true;
        this.app.gameOver();
        this.chaseCam.setEnabled(false);
    }
        
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        player.setEnabled(enabled);
        physics.setEnabled(enabled);
        gameOver = false;
    }


    /**
     * Restarts the level after a game over. Respawns the player, resets the
     * level and difficulty.
     */
    public void restartLevel() {
        gameOver = false;
        gameTime = 0;
        difficultyLevel = 0;
        stopInvulnerable = false;
        startInvulnerable = false;
        lightShiftTime = 0;
        sun.setColor(P.sunColor);
        P.speedFactor = P.minSpeedFactor;
        
        Vector3f spawnPosition = player.getLocalTranslation();
        spawnPosition.x = 0.0f;
        spawnPosition.y = 20.0f;
        PlayerControl pc = player.getControl(PlayerControl.class);
        pc.respawn(spawnPosition);
        
        level.cleanup();
        // Try again
        level.resetLevel();

        this.chaseCam.setEnabled(true);
    }

    /**
     * Initializes the camera. After this, the camera follows the player,
     * looking at them from the right angle.
     */
    private void initCamera() {
        this.chaseCam = new ChaseCamera(this.app.getCamera(), this.player, this.inputManager);
        //this.chaseCam.setSmoothMotion(true);
        this.chaseCam.setTrailingEnabled(false);
        this.chaseCam.setDefaultHorizontalRotation(-FastMath.DEG_TO_RAD * 270);
        this.chaseCam.setDefaultVerticalRotation(FastMath.DEG_TO_RAD * 30);
        //Depth (z) distance from camera to target (player)
        this.chaseCam.setDefaultDistance(50);
        //Offset in x direction so that the target (player) is on the left half 
        //of the screen instead of the center of the screen.
        this.chaseCam.setLookAtOffset(new Vector3f(6f, 8f, 0f));
    }

    /**
     * Sets up the user inputs. Jump is triggered by spacebar and mouseclick; on
     * the phone all touch events are translated to clicks.
     */
    private void initInputs() {
        inputManager.addListener(player.getControl(PlayerControl.class), "jump");
        inputManager.addListener(player.getControl(PlayerControl.class), "pause");
        inputManager.addListener(player.getControl(PlayerControl.class), "pauseAnim");
        
        if (EffectSettings.shadows == Shadows.DIRECTIONAL) {
        inputManager.addMapping("shadows",
                new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("shadowSplitsDown",
                new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("shadowSplitsUp",
                new KeyTrigger(KeyInput.KEY_W));
        
        inputManager.addMapping("shadowResDown",
                new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("shadowResUp",
                new KeyTrigger(KeyInput.KEY_S));

        ActionListener shadowController = new ActionListener() {
            //private InGameState state = InGameState.this;
            public void onAction(String name, boolean isPressed, float tpf) {
                if (!isPressed) {
                    return;
                }
                if (name.equals("shadows")) {
                    InGameState.this.toggleShadows();
                    return;
                }
                if (name.equals("shadowSplitsDown")) {
                    if (InGameState.this.shadowSplits > 1) {
                        InGameState.this.shadowSplits -= 1;
                    }
                }
                if (name.equals("shadowSplitsUp")) {
                    if (InGameState.this.shadowSplits < 4) {
                        InGameState.this.shadowSplits += 1;
                    }
                }
                if (name.equals("shadowResUp")) {
                    InGameState.this.shadowRes *= 2;
                }
                if (name.equals("shadowResDown")) {
                    InGameState.this.shadowRes /= 2;
                }
                InGameState.this.refreshShadows();
            }
        };
        inputManager.addListener(shadowController, "shadows","shadowSplitsUp","shadowSplitsDown","shadowResUp","shadowResDown");
        }
        
    }

    private void initCollisions() {
        PhysicsCollisionListener physicsCollisionListener = new PhysicsCollisionListener() {
            public void collision(PhysicsCollisionEvent event) {
                if (event.getNodeA().getName().equals("player")
                        && event.getNodeB().getName().equals("playerinteractor")) {
                    event.getNodeB().getControl(PlayerInteractorControl.class).collideWithPlayer((Player) event.getNodeA());
                } else if (event.getNodeB().getName().equals("playerinteractor")
                        && event.getNodeA().getName().equals("platform")) {
                    event.getNodeB().getControl(PlayerInteractorControl.class).collideWithStatic();
                }

            }
        };
        this.physics.getPhysicsSpace().addCollisionListener(physicsCollisionListener);
    }

    public void setInvulnerable(boolean setting) {
        startInvulnerable = setting;
        stopInvulnerable = !setting;
    }

    private void toggleShadows() {
        if (viewPort.getProcessors().contains(shadows)) {
            viewPort.removeProcessor(shadows);
        } else {
            viewPort.addProcessor(shadows);
        }
    }

    private int shadowRes = 128;
    private int shadowSplits = 1;

    private void refreshShadows() {
        if (shadows != null) {
            viewPort.removeProcessor(shadows);
        }
                shadows = new DirectionalLightShadowRenderer(assetManager,shadowRes,shadowSplits);
        shadows.setShadowIntensity(0.5f);
        shadows.setLight(sun);
        viewPort.addProcessor(shadows);
        System.out.println(shadowRes + " " + shadowSplits);
    }
/* Methods below are used for screenshots, probably won't have to be used again
    private void initParticles() {
        
        ParticleEmitter glow = StandardParticleEmitter.standard(assetManager);
           
        glow.setNumParticles(5);
        glow.setStartColor(ColorRGBA.Red);
        glow.setEndColor(ColorRGBA.White);
        glow.getParticleInfluencer().setInitialVelocity(Vector3f.ZERO);
        glow.setStartSize(3.5f);
        glow.setEndSize(0.1f);
        glow.setGravity(0, 0, 2);
        glow.setLowLife(1f);
        glow.setHighLife(1f);
        glow.getParticleInfluencer().setVelocityVariation(0.3f);
        glow.setLocalTranslation(-10f, 20f, 0f);
        this.gameNode.attachChild(glow);




        ParticleEmitter fire = StandardParticleEmitter.standard(assetManager);
        fire.setStartColor(new ColorRGBA(0, 1f, 0.3f, 1.0f));
        fire.setEndColor(new ColorRGBA(0.45f, 0.4f, 0f, 0.5f));
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 5, 5));
        fire.setStartSize(1.7f);
        fire.setEndSize(0.1f);
        fire.setGravity(new Vector3f(0, 30f, 0));
        fire.setLowLife(0.3f);
        fire.setHighLife(1f);
        fire.getParticleInfluencer().setVelocityVariation(0.3f);
        fire.setLocalTranslation(-5f, 20f, 0f);
        this.gameNode.attachChild(fire);
        

        ParticleEmitter sparkle = StandardParticleEmitter.standard(assetManager);
        sparkle.setName("spark");
        sparkle.setNumParticles(5);
        sparkle.getMaterial().setTexture("Texture", assetManager.loadTexture(
                "Textures/Explosion/flash.png"));

        sparkle.setStartColor(new ColorRGBA(.10f, 0.40f, 0.90f, 1f));   // bright cyan
        sparkle.setEndColor(new ColorRGBA(0f, 0.1f, 0.25f, 0.5f)); // dark blue
        sparkle.getParticleInfluencer().setInitialVelocity(new Vector3f(0, -5, 0));
        sparkle.setStartSize(1.5f);
        sparkle.setEndSize(0.5f);
        sparkle.setGravity(0, 0f, 0);
        sparkle.setLowLife(0.2f);
        sparkle.setHighLife(0.6f);
        sparkle.getParticleInfluencer().setVelocityVariation(0.3f);
        sparkle.setLocalTranslation(0f,20f,0f);
        this.gameNode.attachChild(sparkle);
        
        ParticleEmitter forcefield = StandardParticleEmitter.forcefield(assetManager);
        forcefield.setLocalTranslation(5f,20f,0f);
        this.gameNode.attachChild(forcefield);
    }*/
    
    /*public void initScene() {
        SpotLight spotlight = new SpotLight();
        SingleShotWizard wizard = new SingleShotWizard(assetManager, spotlight);
        this.level.addToLevel(wizard, new Vector3f(20f, 15f, 0f));
        this.level.addToLevel(spotlight, new Vector3f(20f, 15f, 0f));
        
        Torch torch = new Torch(assetManager, new Vector3f(0f,13f,0f));
        gameNode.attachChild(torch);
        Torch torch2 = new Torch(assetManager, new Vector3f(-10f,17f,0f));
        gameNode.attachChild(torch2);        
    }*/
}
