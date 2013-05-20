package spatial;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.sun.org.apache.bcel.internal.generic.L2D;
import control.AbstractPlayerInteractorControl;
import control.PlayerControl;
import control.PlayerInteractorControl;
import material.LightTextureMaterial;
import variables.EffectSettings;

/**
 * A class for a non-physical torch, purely for decoration.
 * @author jonatankilhamn
 */
public class Plant extends PlayerInteractor implements AnimEventListener {

    private AnimChannel channel;
    private AnimControl control;
    private static final CollisionShape collisionShape =
            new CylinderCollisionShape(new Vector3f(1,10,1),1);
    private static final float modelHeight = 10;

    
    public enum Type {
        LEAVES ("Models/plant/plant-new.j3o", "Models/plant/AO/planta-ao-small.png", 6f),
        FLOWERS ("Models/plant/plant-new-2.j3o", "Models/plant/AO/planta2-ao-small.png", 3.5f);

        public final String modelSrc;
        public final String AOsrc;
        public final float scale;

        Type(String modelSrc, String AOsrc, float scale) {
            this.modelSrc = modelSrc;
            this.AOsrc = AOsrc;
            this.scale = scale;
        }
    }
    
    /**
     * This constructor creates a
     * <code>Torch</code> represented by a
     * <code>Geometry</code> loaded internally.
     *
     * @param assetManager is used to load the geometry and texture of
     * the <code>Window</code>.
     */
    public Plant(AssetManager assetManager, Vector3f position, Type type) {
        Node model = (Node)assetManager.loadModel(type.modelSrc).scale(type.scale);
        model.setName("model");
        
        control = model.getChild("Plane").getControl(AnimControl.class);
        ((Geometry)((Node)model.getChild("Plane")).getChild("Plane1")).getMaterial().getAdditionalRenderState().setDepthWrite(false);
        channel = control.createChannel(); 
        control.addListener(this);
        
        channel.setLoopMode(LoopMode.DontLoop);
        
        if(EffectSettings.ambientOcclusion == EffectSettings.AmbientOcclusion.TEXTURE || EffectSettings.ambientOcclusion == EffectSettings.AmbientOcclusion.INTERVAL_POST_PROCESSING) {
            this.addAOTexture(model, assetManager, type);
            this.attachChild(this.addWallOcclusion(assetManager, type));
        }
        
        this.attachChild(model);
        model.move(0,modelHeight/2,-2.5f);
        model.rotate(-1.74f,0,0);
        //model.move(0f, modelHeight,-P.platformWidth/2-P.playerZOffset+0.7f);
        this.addControl(this.createControl());
        this.setLocalTranslation(position.x, position.y, 1.5f);//-P.platformWidth/2-P.playerZOffset+0.6f);
        //this.setShadowMode(ShadowMode.Off);   
        
    }

    @Override
    protected Spatial createModel(AssetManager assetManager) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

  
    @Override
    protected PlayerInteractorControl createControl() {
        return new AbstractPlayerInteractorControl(collisionShape) {
            private boolean hasHit;
           

            public void collideWithPlayer(Player player) {
                if (!hasHit) {
                    PlayerControl pc = player.getControl(PlayerControl.class);
                    hasHit = true;
                    channel.setAnim("ArmatureAction.002");
                    //channel.setAnim("roll"); //renderar inte bra
                }
            }

            @Override
            protected void positionUpdate(float tpf) {/*do not move*/
            Spatial model = ((Node)this.spatial).getChild("model");
                if (model != null) {
                    //model.rotate(0.24f, 0.12f, 0.02f);
                }
            }

            public void collideWithStatic() {/*do nothing*/}

        };
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        channel.setLoopMode(LoopMode.DontLoop);
       // throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
       // throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private void addAOTexture(Node model, AssetManager assetManager, Type type) {
        ((Geometry)((Node)model.getChild("Sphere")).getChild("Sphere2")).getMaterial().setTexture("AOMap", assetManager.loadTexture(type.AOsrc));
    }

    private Geometry addWallOcclusion(AssetManager assetManager, Type type) {
        Box wallAO = null;
        if (type == Type.LEAVES) {
            wallAO = new Box(3.6f, 3.6f, 0f);
        } else {
            wallAO = new Box(3.2f, 3.2f, 0f);
        }
        Geometry wall = new Geometry("wallOcclusion", wallAO);
        if (type == Type.LEAVES) {
            wall.setLocalTranslation(-0.3f, 17, -6.9f);
        } else {
            wall.setLocalTranslation(0f, 12, -6.9f);
        }
        Material wallMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wallMaterial.setTexture("ColorMap", assetManager.loadTexture("Models/plant/AO/wall-ao-small.png"));
        wallMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha); // activate transparency
        wallMaterial.getAdditionalRenderState().setDepthWrite(false);
        wall.setMaterial(wallMaterial);
        wall.setQueueBucket(RenderQueue.Bucket.Transparent);
        return wall;
    }
    
   
}
