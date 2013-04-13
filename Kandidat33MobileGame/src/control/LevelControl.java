package control;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
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
public class LevelControl implements Control {

    private Node levelNode;
    private float nextChunkX;
    private AssetManager assetManager;
    private PhysicsSpace physicsSpace;
    private Player player;
    private ChunkFactory chunkFactory;
    private int chunkNumber;

    /**
     * Creates a new LevelControl.
     *
     */
    public LevelControl(AssetManager assetManager, PhysicsSpace physicsSpace,
            Player player) {
        this.assetManager = assetManager;
        this.physicsSpace = physicsSpace;
        this.player = player;
        this.chunkFactory = new ChunkFactory(assetManager);
        this.nextChunkX = -P.minLeftDistance;
    }

    /**
     * Sets the spatial of this control. This spatial is used for reference
     * position and should not be moved.
     *
     * @param spatial The spatial to attach the entire level to - must be a
     * Node!
     */
    public void setSpatial(Spatial spatial) {
        assert (spatial instanceof Node);
        if (levelNode != null) {
            cleanup();
        }
        this.levelNode = (Node) spatial;
        initiateLevel();
    }

    /**
     * Checks if any chunk of the level is outside the view and needs moving. If
     * so, performs that move.
     *
     * @param tpf
     */
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

        // generate a chunk filled the chunk with content
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
         * Give references to the LevelControl to anyone who wants to
         * bring friends (other spatials) to the level
         */
        spatial.depthFirstTraversal(
                new SceneGraphVisitor() {
                    public void visit(Spatial spatial) {
                        int nbrOfCtrls = spatial.getNumControls();
                        for (int i = 0; i < nbrOfCtrls; i++) {
                            Control control = spatial.getControl(i);
                            if (control instanceof LevelContentGenerator) {
                                ((LevelContentGenerator) control).setLevelControl(LevelControl.this);
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

    public void cleanup() {
        for (Spatial spatial : levelNode.getChildren()) {
            removeFromLevel(spatial);
        }
        this.nextChunkX = -P.minLeftDistance;

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
