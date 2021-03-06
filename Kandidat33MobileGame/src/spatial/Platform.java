package spatial;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import material.MultiColoredLightMaterial;
import variables.EffectSettings;
import variables.EffectSettings.AmbientOcclusion;
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
    private PType type;

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
    public Platform(AssetManager assetManager, Vector3f position, PType type, WindowFrame.Design windowDesign) {
        super("platform");
        this.type = type;
        
        
        float length = type.length;
        Node platform = (Node)assetManager.loadModel("Models/platform/untitled8-new.j3o");
        
        if (EffectSettings.light == EffectSettings.Light.TEXTURES_AND_WINDOW || EffectSettings.light == EffectSettings.Light.TEXTURES_SMALL_LIGHTS) {
            MultiColoredLightMaterial newMaterial = new MultiColoredLightMaterial(assetManager, "Materials/MultiColoredLighting.j3md");
            newMaterial.setOnlyOneWindowLight(true);
            
            Geometry platformGeom = ((Geometry)((Node)platform.getChild("Cube")).getChild("Cube1"));
            /*for (MatParam p : platform.getMaterial().getParams()) {
                System.out.println(p + " " + p.getName() + " " + p.getVarType() + " " + p.getValue());
                //newMaterial.setParam(p.getName(), p.getVarType(), p.getValue());
            }*/
            newMaterial.setTexture("DiffuseMap", assetManager.loadTexture("Models/platform/untitled8-uv-tegel.png"));
            newMaterial.setBoolean("UseMaterialColors", true);
            newMaterial.setVector4("Specular", new Vector4f( 1.0f, 1.0f, 1.0f, 1.0f));
            newMaterial.setVector4("Ambient", new Vector4f( 0, 0, 0, 1));
            newMaterial.setVector4("Diffuse", new Vector4f( 1.0f, 1.0f, 1.0f, 1.0f));
            platformGeom.setMaterial(newMaterial);
            
        }
        
        platform.rotate(180*FastMath.DEG_TO_RAD, 0, 0);
        if (EffectSettings.ambientOcclusion == AmbientOcclusion.TEXTURE || EffectSettings.ambientOcclusion == AmbientOcclusion.INTERVAL_POST_PROCESSING) {
            ((Geometry)((Node)platform.getChild("Cube")).getChild("Cube1")).getMaterial().setTexture("DiffuseMap", assetManager.loadTexture("Models/platform/AO/plaform-ao-small.png"));
        }
        switch(type) {
            case SHORT:
                this.attachChild(platform);
                break;
            case MEDIUM:
                this.attachChild(platform.move(-P.shortPlatformLength/2, 0f, 0f));
                this.attachChild(platform.clone().move(P.shortPlatformLength, 0f, 0f));
                break;
            case LONG:
                this.attachChild(platform);
                this.attachChild(platform.clone().move(-P.shortPlatformLength, 0f, 0f));
                this.attachChild(platform.clone().move(P.shortPlatformLength, 0f, 0f));
                break;
        }
             
        this.setLocalTranslation(length / 2 + position.x, position.y, -P.playerZOffset);

        RigidBodyControl rigidBodyControl = new RigidBodyControl(
                new BoxCollisionShape(new Vector3f(
                length / 2, P.platformHeight / 2, P.platformWidth / 2)), 0.0f);

        this.addControl(rigidBodyControl);
        
        if (windowDesign != null && (EffectSettings.light == EffectSettings.Light.TEXTURES_AND_WINDOW || EffectSettings.light == EffectSettings.Light.TEXTURES_SMALL_LIGHTS)) {
            for (Spatial part : this.getChildren()) {
                ((Geometry)((Node)((Node)part).getChild("Cube")).getChild("Cube1")).getMaterial().setTexture("LightTexture", assetManager.loadTexture(windowDesign.lightColorsSrc));
            }
        }
        
        if (EffectSettings.ambientOcclusion == AmbientOcclusion.TEXTURE || EffectSettings.ambientOcclusion == AmbientOcclusion.INTERVAL_POST_PROCESSING) {
            this.attachChild(addWallOcclusion(assetManager, length));
            //Node platformOccl = this.addPlatformOcclusion(assetManager, length);
            //this.attachChild(platformOccl);
            //wallOccl.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
            //platformOccl.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        }
        
        this.setShadowMode(ShadowMode.CastAndReceive);
    }

    private Geometry addWallOcclusion(AssetManager assetManager, float length) {
        Box wallAO = new Box(length*0.9f+0.9f, 12f, 0f);
        Geometry wall = new Geometry("wallOcclusion", wallAO);
        wall.setLocalTranslation(0f, 0.55f,-(P.platformWidth/2-0.05f));
        Material wallMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wallMaterial.setTexture("ColorMap", assetManager.loadTexture("Models/platform/AO/wall-ao-small.png"));
        wallMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha); // activate transparency
        wallMaterial.getAdditionalRenderState().setDepthWrite(false);
        wall.setMaterial(wallMaterial);
        wall.setQueueBucket(RenderQueue.Bucket.Transparent);
        return wall;
    }
    
    private Node addPlatformOcclusion(AssetManager assetManager, float length) {
        Node platformOcclusion = new Node("platformOcclusion");
        Box topAO = new Box(length/2-0.05f, 0f, P.platformWidth/2);
        Geometry top = new Geometry("topOcclusion", topAO);
        top.setLocalTranslation(-0.15f, P.platformHeight/2+0.10f, 0f);
        top.rotate(0f, 180*FastMath.DEG_TO_RAD, 0f);
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("Models/platform/AO/platform-top-ao-transparant-small-narrow.png"));
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha); // activate transparency
        top.setMaterial(material);
        //wall.setQueueBucket(RenderQueue.Bucket.Transparent);
        
        Box sideAO = new Box(0f, P.platformHeight/2, P.platformWidth/2);
        Geometry right = new Geometry("rightOcclusion", sideAO);
        right.setLocalTranslation(0f, 0f, length/2+0.1f);
        right.rotate(180*FastMath.DEG_TO_RAD, 0f,0f);
        right.setMaterial(material);
        
        Geometry left = new Geometry("leftOcclusion", sideAO);
        right.setLocalTranslation(0f, 0f, -(length/2+0.1f));
        right.rotate(0f, 0f,0f);
        right.setMaterial(material);
        
        platformOcclusion.attachChild(right);
        platformOcclusion.attachChild(top);
        return platformOcclusion;
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
    
    public PType getType() {
        return this.type;
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
