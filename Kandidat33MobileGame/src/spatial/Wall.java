package spatial;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import variables.P;

/**
 * 
 * @author jonatankilhamn
 */
public class Wall extends Geometry {
    /**
     * This constructor creates a <code>Wall</code> represented by a 
     * <code>Geometry</code> loaded internaly. The dimensions of the 
     * <code>Wall</code> is currently loaded from the <code>P</code>-class 
     * containing some global variables. 
     * @param assetManager is used to load the geometry and 
     * texture of the <code>Wall</code>.
     */
    public Wall(AssetManager assetManager){
        super("Wall");
        Box model =
            new Box(new Vector3f(P.chunkLength/2,0,-P.platformWidth/2), P.chunkLength/2, 40, 0);
        this.mesh = model;
        
        Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        material.setTexture("DiffuseMap", assetManager.loadTexture("Textures/BrickWall.jpg"));
        this.setMaterial(material);

        this.setShadowMode(ShadowMode.Receive);
    }
}
