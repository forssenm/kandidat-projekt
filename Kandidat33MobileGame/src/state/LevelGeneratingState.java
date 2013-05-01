package state;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import leveldata.ChunkFactory;
import leveldata.LevelContentGenerator;
import spatial.Platform;
import spatial.Player;
import spatial.hazard.AbstractFireball;
import spatial.hazard.AbstractWizard;
import util.RomanNumber;
import variables.P;

/**
 * A class controlling the entire level – the background and the platforms.
 * Usage: attach to a Node called level and leave it running while the game is
 * running.
 *
 * @author jonatankilhamn
 */
public class LevelGeneratingState extends AbstractAppState {

    private Node levelNode;
    private Node platformNode = new Node("platforms");
    private Node wizardNode = new Node("wizards");
    private Node fireballNode = new Node("fireballs");
    private Node miscNode = new Node("misc");
    private float nextChunkX;
    private Player player;
    private ChunkFactory chunkFactory;
    private int chunkNumber;
    private int gameProgress;
    
    private SimpleApplication app;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private Node gameNode;
    private PhysicsSpace physicsSpace;
    
    public static final String LEVEL_NODE = "Level Node";
    private BitmapFont guiFont;

    /**
     * This method initializes the the InGameState and thus gets the game 
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
        this.gameNode = (Node)this.app.getRootNode().getChild(InGameState.GAME_NODE);
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.physicsSpace = this.stateManager.getState(
                BulletAppState.class).getPhysicsSpace();
        
        this.levelNode = new Node(LEVEL_NODE);
        
        levelNode.attachChild(wizardNode);
        levelNode.attachChild(fireballNode);
        levelNode.attachChild(platformNode);
        levelNode.attachChild(miscNode);
        
        this.player = (Player)gameNode.getChild("player");
        gameNode.attachChild(levelNode);
        
        chunkFactory = new ChunkFactory(assetManager);
        
        initiateLevel();
    }

    private float leftBound;
    private float rightBound;
    private float lowerBound;
    private Vector3f tempVec;
    
    /**
     * Checks if any chunk of the level is outside the view and needs moving. If
     * so, performs that move.
     *
     * @param tpf
     */
    @Override
    public void update(float tpf) {
        
        tempVec = this.player.getLocalTranslation();
        leftBound = tempVec.getX() - P.minLeftDistance;
        rightBound = tempVec.getX() + P.minRightDistance;
        lowerBound = tempVec.getY() - P.minDownDistance;
        
        
        for (Spatial spatial : getAllLevelObjects()) {
            if (isOutsideLevelBounds(spatial.getLocalTranslation())) {
                removeFromLevel(spatial);
                /* the background node (wall and window) acts as a checkpoint –
                 * when it is removed, we know that we need more level
                 */
                if (spatial.getName().equals("background")) {
                    generateNextChunk();
                }
            }
        }
        for (Light light : this.gameNode.getLocalLightList()) {
            if (light instanceof PointLight) {
                if (isOutsideLevelBounds(((PointLight)light).getPosition())) {
                    this.gameNode.removeLight(light);
                }
            } else if (light instanceof SpotLight) {
                if (isOutsideLevelBounds(((SpotLight)light).getPosition()) && !P.playerSpot.equals(light.getName())) {
                    this.gameNode.removeLight(light);
                }
            }
        }
    }
    
    Collection tempColl = new LinkedList<Spatial>();
    
    public Collection<Spatial> getAllLevelObjects() {
        tempColl.clear();
        tempColl.addAll(wizardNode.getChildren());
        tempColl.addAll(fireballNode.getChildren());
        tempColl.addAll(platformNode.getChildren());
        tempColl.addAll(miscNode.getChildren());
        return tempColl;
    }

    private boolean isOutsideLevelBounds(Vector3f position) {
        return (position.getX() < leftBound || position.getX() > rightBound
                || position.getY() < lowerBound);
    }

    public void initiateLevel() {
        this.nextChunkX = -P.minLeftDistance+P.chunkLength;
        chunkNumber = 0;
        // generate starting chunks
        for (int i = 0; i < P.noOfStartingChunks; i++) {
            generateNextChunk();
        }
        gameProgress = -1;
    }

    /**
     * Generate a new chunk of the level, placing it directly after the last
     * chunk.
     *
     * @return
     */
    private void generateNextChunk() {
        
        chunkNumber++;
        gameProgress++;
        
        // generate a chunk filled with content
        List<Object> list = chunkFactory.generateChunk(chunkNumber);
        Vector3f newChunkPosition =
                new Vector3f(nextChunkX, 0f, 0f);
        nextChunkX += P.chunkLength;

        for (Object object : list) {
            if (object instanceof Spatial) {
                Spatial spatial = (Spatial) object;
                addToLevel(spatial, spatial.getLocalTranslation().add(newChunkPosition));
            } else if (object instanceof SpotLight) {
                SpotLight light = (SpotLight) object;
                addToLevel(light, light.getPosition().add(newChunkPosition));
            } else if (object instanceof PointLight) {
                PointLight light = (PointLight) object;
                addToLevel(light, light.getPosition().add(newChunkPosition));   
            }
        }

    }

    /**
     * Take level content and add it to the level. LevelChunks with several
     * static objects are not added this way, but everything else is. TODO:
     * process all level content through this method
     *
     * @param spatial The spatial to add. Should be a single object, not a
     * sub-tree of several game objects.
     * @param position The position to place the object in.
     */
    public void addToLevel(Spatial spatial, final Vector3f position) {

        physicsSpace.addAll(spatial);

        // physics-secure movement to the position where it's added
        PhysicsControl physicsControl = spatial.getControl(PhysicsControl.class);
        if (physicsControl != null) {
            physicsControl.setEnabled(false);
            spatial.setLocalTranslation(position);
            physicsControl.setEnabled(true);
        } else {
            spatial.setLocalTranslation(position);
        }


        /*
         * Give references to the LevelGeneratingState to anyone who wants to
         * bring friends (other spatials) to the level
         */
        spatial.depthFirstTraversal(
                new SceneGraphVisitor() {
                    public void visit(Spatial spatial) {
                        int nbrOfCtrls = spatial.getNumControls();
                        for (int i = 0; i < nbrOfCtrls; i++) {
                            Control control = spatial.getControl(i);
                            if (control instanceof LevelContentGenerator) {
                                ((LevelContentGenerator) control).setLevelControl(LevelGeneratingState.this);
                            }
                        }
                    }
                });
        
        if (spatial instanceof AbstractFireball) {
            fireballNode.attachChild(spatial);
        } else if (spatial instanceof AbstractWizard) {
            wizardNode.attachChild(spatial);
        } else if (spatial instanceof Platform) {
            platformNode.attachChild(spatial);
        } else {
            miscNode.attachChild(spatial);
        }
        
        //levelNode.attachChild(spatial);
    }

    public void removeFromLevel(Spatial spatial) {
        physicsSpace.removeAll(spatial);
        if (spatial instanceof AbstractFireball) {
            fireballNode.detachChild(spatial);
        } else if (spatial instanceof AbstractWizard) {
            wizardNode.detachChild(spatial);
        } else if (spatial instanceof Platform) {
            platformNode.detachChild(spatial);
        } else {
            miscNode.detachChild(spatial);
        }
    }
    
    private void addToLevel(SpotLight light, Vector3f position) {
        light.setPosition(position);
        this.gameNode.addLight(light);
    }
    
    private void addToLevel(PointLight light, Vector3f position) {
        light.setPosition(position);
        this.gameNode.addLight(light);
    }
    
    private void removeFromLevel(Light light) {
        this.gameNode.removeLight(light);
    }

    public Player getPlayer() {
        return player;
    }

    public void resetLevel() {
        removeAllFromLevel();
        initiateLevel();
    }
    
    public void removeAllFromLevel() {
        for (Spatial spatial : getAllLevelObjects()) {
            removeFromLevel(spatial);
        }
    }
    
    @Override
    public void cleanup() {
        removeAllFromLevel();
        super.cleanup();
        
    }

    public void render(RenderManager rm, ViewPort vp) {
    }

    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void write(JmeExporter ex) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void read(JmeImporter im) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Deprecated
    private void showLevelProgress() {
        Node guiNode = app.getGuiNode();
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        String romanNumber = RomanNumber.romanNumberString(gameProgress);
        helloText.setText(romanNumber);
        float xPos = P.screenWidth/2 - helloText.getLineWidth()/2;
        helloText.setLocalTranslation(xPos, helloText.getLineHeight(), 0);
        guiNode.attachChild(helloText);
    }
}
