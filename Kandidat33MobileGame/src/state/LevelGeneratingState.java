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
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import leveldata.ChunkFactory;
import leveldata.LevelContentGenerator;
import spatial.Player;
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
    private float nextChunkX;
    private Player player;
    private ChunkFactory chunkFactory;
    private int chunkNumber;
    private SimpleApplication app;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private Node gameNode;
    private PhysicsSpace physicsSpace;
    
    public static final String LEVEL_NODE = "Level Node";

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
        this.player = (Player)gameNode.getChild("player");
        gameNode.attachChild(levelNode);
        
        chunkFactory = new ChunkFactory(assetManager);
        
        initiateLevel();
    }

    /**
     * Checks if any chunk of the level is outside the view and needs moving. If
     * so, performs that move.
     *
     * @param tpf
     */
    @Override
    public void update(float tpf) {
        for (Spatial spatial : levelNode.getChildren()) {
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
    }

    private boolean isOutsideLevelBounds(Vector3f position) {
        Vector3f playerPosition = this.player.getLocalTranslation();
        final float leftBound = playerPosition.getX() - P.minLeftDistance;
        final float rightBound = playerPosition.getX() + P.minRightDistance;
        final float lowerBound = playerPosition.getY() - P.minDownDistance;

        return (position.getX() < leftBound || position.getX() > rightBound
                || position.getY() < lowerBound);
    }

    public void initiateLevel() {
        this.nextChunkX = -P.minLeftDistance;
        chunkNumber = 0;
        // generate starting chunks
        int nbrOfChunks = (int) Math.round((P.minRightDistance + P.minLeftDistance) / P.chunkLength) - 3;
        for (int i = 0; i < nbrOfChunks; i++) {
            generateNextChunk();
        }
    }

    /**
     * Generate a new chunk of the level, placing it directly after the last
     * chunk.
     *
     * @return
     */
    private void generateNextChunk() {
        
        chunkNumber++;

        // generate a chunk filled with content
        List<Spatial> list = chunkFactory.generateChunk(chunkNumber);
        Vector3f newChunkPosition =
                new Vector3f(nextChunkX, 0f, 0f);
        nextChunkX += P.chunkLength;

        for (Spatial spatial : list) {
            addToLevel(spatial, spatial.getLocalTranslation().add(newChunkPosition));
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
        
        levelNode.attachChild(spatial);
    }

    public void removeFromLevel(Spatial spatial) {
        physicsSpace.removeAll(spatial);
        levelNode.detachChild(spatial);
    }

    public Player getPlayer() {
        return player;
    }
 
    public void resetLevel() {
        for (Spatial spatial : levelNode.getChildren()) {
            removeFromLevel(spatial);
        }
        initiateLevel();
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
}
