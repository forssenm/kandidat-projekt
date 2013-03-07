/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestate;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.io.IOException;
import java.util.LinkedList;
import variables.P;

/**
 * A class controlling the entire level – the background and the platforms.
 * Usage: attach to a Node called level and leave it running while the game
 * is running.
 * @author jonatankilhamn
 */
public class LevelControl implements Control {

    
    private Spatial levelNode;
    private LinkedList<Node> chunks;
    private AssetManager am;
    private PhysicsSpace pSpace;
    
    /**
     * Creates a new LevelControl.
     * 
     */
    public LevelControl(AssetManager am, PhysicsSpace pSpace) {
        this.am = am;
        this.pSpace = pSpace;
        
    }


    /**
     * Sets the spatial of this control.
     * This spatial is used for reference position and should not be moved.
     * @param spatial 
     */
    public void setSpatial(Spatial spatial) {
        this.levelNode = spatial;
    }

    /**
     * Checks if any chunk of the level is outside the view and needs moving.
     * If so, performs that move.
     * @param tpf 
     */
    public void update(float tpf) {
    }
    
    /**
     * Generate a new chunk of the level, placing it directly after the
     * last chunk.
     * @return 
     */
    private Node generateNextChunk() {
        float xPos = this.chunks.getLast().getLocalTranslation().getX() + P.chunkLength;
        Platform platform = new Platform(this.am);
        Node chunk = new Node();
        chunk.attachChild(platform);
        platform.setLocalTranslation(xPos+P.platformDistance, 0f, 0f);
        
        addChunkToPhysicsSpace(chunk);
        return chunk;
    }
    
    /**
     * Adds all objects with a PhysicsControl in a chunk to the PhysicsSpace.
     * "In a chunk" is the same as "attached to a Node".
     * @param chunk 
     */
    private void addChunkToPhysicsSpace(Node chunk) {
        // traverse the scenegraph starting from the chunk node
        chunk.depthFirstTraversal(new SceneGraphVisitor() {
            public void visit(Spatial spatial) {
                pSpace.addAll(spatial);
            }
        });
    }    

    /**
     * Disables the physics of all objects in a chunk.
     * "In a chunk" is the same as "attached to a Node".
     * @param chunk 
     */
    private void disablePhysicsOfChunk(Node chunk) {
        // traverse the scenegraph starting from the chunk node
        chunk.depthFirstTraversal(new SceneGraphVisitor() {
            public void visit(Spatial spatial) {
                // get the PhysicsControl if there is any
                PhysicsControl physicsControl = spatial.getControl(PhysicsControl.class);
                if (physicsControl != null) {
                    physicsControl.setEnabled(false);
                }
            }
        });
    }

    /**
     * Enables the physics of all objects in a chunk.
     * "In a chunk" is the same as "attached to a Node".
     * @param chunk 
     */
    private void enablePhysicsOfChunk(Node chunk) {
        // traverse the scenegraph starting from the chunk node
        chunk.depthFirstTraversal(new SceneGraphVisitor() {
            public void visit(Spatial spatial) {
                // get the PhysicsControl if there is any
                PhysicsControl physicsControl = spatial.getControl(PhysicsControl.class);
                if (physicsControl != null) {
                    physicsControl.setEnabled(true);
                }
            }
        });
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
