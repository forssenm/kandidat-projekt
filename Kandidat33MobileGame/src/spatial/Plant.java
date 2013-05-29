package spatial;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.sun.org.apache.bcel.internal.generic.L2D;
import control.AbstractPlayerInteractorControl;
import control.PlayerControl;
import control.PlayerInteractorControl;

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
        LEAVES ("Models/plant/plant-new.j3o", 6f),
        FLOWERS ("Models/plant/plant-new-2.j3o", 3.5f);

        public final String modelSrc;
        public final float scale;

        Type(String modelSrc, float scale) {
            this.modelSrc = modelSrc;
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
        channel = control.createChannel(); 
        control.addListener(this);
        
        channel.setLoopMode(LoopMode.DontLoop);
        
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
    
   
}
