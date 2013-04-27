package spatial;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import control.AbstractPowerupControl;
import control.PlayerControl;
import control.PlayerInteractorControl;
import java.util.Random;
import variables.P;

/**
 * A class for a non-physical torch, purely for decoration.
 * @author jonatankilhamn
 */
public class Plant extends PlayerInteractor implements AnimEventListener {

    private static Node modelForTorch;
    private AnimChannel channel;
    private AnimControl control;
    
    /**
     * This constructor creates a
     * <code>Torch</code> represented by a
     * <code>Geometry</code> loaded internally.
     *
     * @param assetManager is used to load the geometry and texture of
     * the <code>Window</code>.
     */
    public Plant(AssetManager assetManager, Vector3f position) {
        //super("Plant");
        
        if (modelForTorch == null) {
            modelForTorch = (Node)assetManager.loadModel("Models/plant/plant002arm007.j3o");
        modelForTorch.scale(2.5f);
        modelForTorch.rotate(0, -1.6f, 0);
        
        }
        
        Node model = (Node)modelForTorch.clone();
        control = model.getChild("Sphere").getControl(AnimControl.class);
        channel = control.createChannel(); 
        control.addListener(this);
        
        //channel.setAnim("ArmatureAction");
        //channel.setLoopMode(LoopMode.Loop);
        
        
        this.attachChild(model);
        this.addControl(this.createControl());
        this.setLocalTranslation(position.x, position.y, 0);//-P.platformWidth/2-P.playerZOffset+0.6f);
        //this.setShadowMode(ShadowMode.Off);   
        
    }

    @Override
    protected Spatial createModel(AssetManager assetManager) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

  
    @Override
    protected PlayerInteractorControl createControl() {
        return new AbstractPowerupControl() {
            private boolean hasHit;
           

            public void collideWithPlayer(Player player) {
                if (!hasHit) {
                    PlayerControl pc = player.getControl(PlayerControl.class);
                    //pc.speedBoostPowerup();
                    hasHit = true;
                    
                    channel.setAnim("ArmatureAction");
                    channel.setLoopMode(LoopMode.Cycle);
                    channel.setSpeed(2.0f);
                  //  SpeedPowerup.this.destroy();
                }
            }
            
            

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
