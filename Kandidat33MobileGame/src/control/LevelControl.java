/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import spatial.Platform;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import spatial.LevelChunk;
import spatial.Wall;
import spatial.WindowFrame;
import variables.P;

/**
 * A class controlling the entire level – the background and the platforms.
 * Usage: attach to a Node called level and leave it running while the game
 * is running.
 * @author jonatankilhamn
 */
public class LevelControl implements Control {

    
    private Node gameNode;
    private LinkedList<LevelChunk> chunks;
    private AssetManager assetManager;
    private PhysicsSpace physicsSpace;
    private Spatial player;
    
    /**
     * Creates a new LevelControl.
     * 
     */
    public LevelControl(AssetManager assetManager, PhysicsSpace physicsSpace,
            Spatial player) {
        this.assetManager = assetManager;
        this.physicsSpace = physicsSpace;
        this.player = player;
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
        if (gameNode != null) {
            gameNode.detachAllChildren();
        }
        this.gameNode = (Node) spatial;
        generateStartingChunks();
    }

    /**
     * Checks if any chunk of the level is outside the view and needs moving.
     * If so, performs that move.
     * @param tpf 
     */
    public void update(float tpf) {
        if (this.player.getLocalTranslation().getX() >
                chunks.getFirst().getLocalTranslation().getX() + P.chunkLength + 60) {
            deleteChunk(chunks.removeFirst());
            generateNextChunk();
        }
    }
    
    private void deleteChunk(LevelChunk chunk) {
        chunk.remove();
    }
    
    private void generateStartingChunks() {
        chunks = new LinkedList<LevelChunk>();
        // generate 5 chunks
        for (int i = 0; i<10; i++){
            generateNextChunk();
        }
    }
    
    /**
     * Generate a new chunk of the level, placing it directly after the
     * last chunk.
     * In the current implementation, a chunk is simply one platform.
     * @pre generateStartingChunks has been run once.
     * @return 
     */
    private Node generateNextChunk() {

        // generate the node to attach everything to
        LevelChunk chunk = new LevelChunk(this.physicsSpace,gameNode);

        // find the x position to place the new chunk in
        float xPos;
        if (chunks.isEmpty())  {
            xPos = -3;
        } else {
            xPos = this.chunks.getLast().getLocalTranslation().getX() + P.chunkLength;
        }
        // generate a new chunk position
        Vector3f newChunkPosition =
                new Vector3f(xPos, 0f, 0f);

        // generate platform positions
        Random random = new Random();
        int rand1 = random.nextInt(6) - 3;
        int rand2 = rand1 + random.nextInt(6) - 3;
        
        // generate two platforms
        Platform platform1 = new Platform(this.assetManager, new Vector3f(0f, rand1, 0f), P.platformLength, P.platformHeight, P.platformWidth);
        Platform platform2 = new Platform(this.assetManager, new Vector3f(P.platformLength+P.platformDistance, rand2, 0), P.platformLength, P.platformHeight, P.platformWidth);
        
        
        // generate the background wall
        Wall wall = new Wall(this.assetManager);
        
        // generate a windowframe
        Vector3f windowPos = new Vector3f(5f,5f,0f);
        WindowFrame window = new WindowFrame(this.assetManager, windowPos);
        
        // generate a light shining out the window
        SpotLight windowLight = new SpotLight();
        windowLight.setSpotOuterAngle(15f * FastMath.DEG_TO_RAD);
        windowLight.setSpotInnerAngle(13f * FastMath.DEG_TO_RAD);
        Vector3f windowLightDir = new Vector3f(0f, -10f, 20f);
        windowLight.setPosition(windowPos.subtract(windowLightDir));
        windowLight.setDirection(windowLightDir);
        windowLight.setSpotRange(100f);
        chunk.addLight(windowLight);
        
        
        // generate a point light source of a random colour
        PointLight light = new PointLight();
        light.setRadius(40);
        light.setPosition(new Vector3f(15f, 10f, 6f));
        if (rand1 < -2) {
            light.setColor(ColorRGBA.Blue);
        } else if (rand1 < 2) {
            light.setColor(ColorRGBA.Red);
        } else {
            light.setColor(ColorRGBA.Green);
        }
        chunk.addLight(light);
        
        
        // attach everything physical to the node
        chunk.attachChild(platform1);
        chunk.attachChild(platform2);
        chunk.addToPhysicsSpace();
        // attach everything else to the node
        chunk.attachChild(wall);
        chunk.attachChild(window);
        
        chunk.setLocalTranslation(newChunkPosition);
        
        gameNode.attachChild(chunk);
        chunks.addLast(chunk);
        return chunk;
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
