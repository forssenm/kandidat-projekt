package spatial;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import variables.P;

/**
 * A class for a physical platform of variable position and size.
 *
 * @author dagen
 */
public class Platform extends Node {

    public enum PType {
        SHORT (P.shortPlatformLength),
        MEDIUM (P.mediumPlatformLength),
        LONG (P.longPlatformLength);
        
        public final float length;
        
        PType(float length) {
            this.length = length;
        }
    }
    private static Material materialForPlatforms;
    private static Node modelForShortPlatform;
    private static Node modelForMediumPlatform;
    private static Node modelForLongPlatform;

    /**
     * This constructor creates a
     * <code>Platform</code> represented by a
     * <code>Geometry</code> loaded internally. A
     * <code>RigidBodyControl</code> is attached to the
     * <code>Platform</code>. The dimensions of the
     * <code>Platform</code> is currently loaded from the
     * <code>P</code>-class containing some global variables.
     *
     * @param assetManager is used to load the geometry and texture of
     * the <code>Platform</code>.
     */
    public Platform(AssetManager assetManager, Vector3f position, PType type) {
        super("platform");
        
        if (modelForShortPlatform == null) {
            initModels(assetManager);
        }
        
        switch(type) {
            case SHORT:
                this.attachChild(modelForShortPlatform.clone());
                break;
            case MEDIUM:
                this.attachChild(modelForMediumPlatform.clone());
                break;
            case LONG:
                this.attachChild(modelForLongPlatform.clone());
                break;
        }
        
        float length = type.length;

        
        this.setLocalTranslation(length / 2 + position.x, position.y, -P.playerZOffset);

        RigidBodyControl rigidBodyControl = new RigidBodyControl(
                new BoxCollisionShape(new Vector3f(
                length / 2, P.platformHeight / 2, P.platformWidth / 2)), 0.0f);
        this.addControl(rigidBodyControl);

        this.setShadowMode(ShadowMode.CastAndReceive);
    }
    
    private static void initModels(AssetManager assetManager) {
        modelForShortPlatform = (Node) assetManager.loadModel("Models/platform/untitled8.j3o");
        //modelForMediumPlatform = ...
        //modelForLongPlatform = ...
        
        // replace everything below with simply loading the other two models
        if (materialForPlatforms == null) {
            materialForPlatforms = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            Texture texture = assetManager.loadTexture("Textures/tegel.png");
            texture.setWrap(Texture.WrapMode.Repeat);
            materialForPlatforms.setTexture("DiffuseMap", texture);
        }
        modelForMediumPlatform = new Node();
        modelForLongPlatform = new Node();
        
        Box mediumBox =
                new Box(Vector3f.ZERO, P.platformWidth / 2, P.platformHeight / 2, PType.MEDIUM.length / 2);
        Box longBox = 
                new Box(Vector3f.ZERO, P.platformWidth / 2, P.platformHeight / 2, PType.LONG.length / 2);
        Geometry mediumGeometry = new Geometry("",mediumBox);
        Geometry longGeometry = new Geometry("",longBox);
        
        modelForMediumPlatform.attachChild(mediumGeometry);
        modelForLongPlatform.attachChild(longGeometry);
        
        mediumBox.scaleTextureCoordinates(new Vector2f(Math.round(PType.MEDIUM.length/8f), 1.25f));
        mediumGeometry.setMaterial(materialForPlatforms);
        
        longBox.scaleTextureCoordinates(new Vector2f(Math.round(PType.LONG.length/8f), 1.25f));
        longGeometry.setMaterial(materialForPlatforms);
        
        mediumGeometry.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        longGeometry.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
    
    
    }

    /**
     * Returns the x-coordinate of this platform relative to it's parent
     * <code>Node</code>.
     *
     * @return the x-coordinate of the <code>Platform</code>s center relative to
     * it's parent <code>Node</code>.
     */
    public float getX() {
        return this.getLocalTranslation().x;
    }

    /**
     * Returns the position of the
     * <code>Platform</code>s relative to it's parent
     * <code>Node</code>
     *
     * @return the <code>Vector3f</code> representing the translation from the
     * <code>Platform</code>s parent <code>Node</code>.
     */
    public Vector3f getPlatformPosition() {
        return this.getLocalTranslation();
    }

    /**
     * This method first turns of the
     * <code>Platform</code>s
     * <code>RigidBodyControl</code> and then sets the position relative to the
     * <code>Platform</code>s parent
     * <code>Node</code>. Finaly if the
     * <code>RigidBodyControl</code> was enabled the it is turned on again.
     *
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