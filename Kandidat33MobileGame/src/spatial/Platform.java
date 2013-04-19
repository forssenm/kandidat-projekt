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
 * A class for a physical platform of variable position and size.
 * @author dagen
 */
public class Platform extends Geometry {
    /**
     * This constructor creates a <code>Platform</code> represented by a 
     * <code>Geometry</code> loaded internally. A <code>RigidBodyControl</code> 
     * is attached to the <code>Platform</code>. The dimentions of the 
     * <code>Platform</code> is currently loaded from the <code>P</code>-class 
     * containing some global variables. 
     * @param assetManager is used to load the geometry and 
     * texture of the <code>Platform</code>.
     */
    public Platform(AssetManager assetManager, Vector3f position, float length, float height, float width) {
        super("platform");
        Box model =
            new Box(Vector3f.ZERO, length/2, height/2, width/2);
        this.mesh = model;
        this.setLocalTranslation(length/2 + position.x, position.y, -P.playerZOffset);
        
        Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        material.setTexture("DiffuseMap", assetManager.loadTexture("Textures/BrickWall.jpg"));
        this.setMaterial(material);

        RigidBodyControl rigidBodyControl = new RigidBodyControl(0.0f);
        this.addControl(rigidBodyControl);

        this.setShadowMode(ShadowMode.CastAndReceive);
    }
    
    /**
     * Returns the x-coordinate of this platform 
     * relative to it's parent <code>Node</code>.
     * @return the x-coordinate of the <code>Platform</code>s center 
     * relative to it's parent <code>Node</code>.
     */
    public float getX() {
        return this.getLocalTranslation().x;
    }
    
    /**
     * Returns the position of the <code>Platform</code>s relative to it's parent <code>Node</code>
     * @return the <code>Vector3f</code> representing the translation from the 
     * <code>Platform</code>s parent <code>Node</code>. 
     */
    public Vector3f getPlatformPosition() {
        return this.getLocalTranslation();
    }
    
    /**
     * This method first turns of the <code>Platform</code>s 
     * <code>RigidBodyControl</code> and then sets the position relative to 
     * the <code>Platform</code>s parent <code>Node</code>. Finaly if the 
     * <code>RigidBodyControl</code> was enabled the 
     * it is turned on again. 
     * @param position 
     */
    public void setPlatformPosition(Vector3f position) {
        RigidBodyControl rigidBodyControl = this.getControl(RigidBodyControl.class);
        boolean wasEnabled = rigidBodyControl.isEnabled();
        rigidBodyControl.setEnabled(false);
        this.setLocalTranslation(position);
        rigidBodyControl.setEnabled(wasEnabled);
    }
    
}
