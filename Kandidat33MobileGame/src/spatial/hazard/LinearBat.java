package spatial.hazard;

import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import control.AbstractHazardControl;
import control.PlayerInteractorControl;
import variables.P;

/**
 * A bat using
 * <code>LinearFireballControl</code> to control its behaviour.
 * 
 * @author jonatankilhamn
 */
public class LinearBat extends AbstractBat {

    private Vector3f velocity;
    
    public LinearBat(AssetManager assetManager,Vector3f velocity) {
        this.velocity = velocity;
        this.attachChild(this.createModel(assetManager));
        this.addControl(this.createControl());
    }
    
    @Override
    protected PlayerInteractorControl createControl() {
        return new AbstractHazardControl(1f) {
            
            boolean fallingOut;
            float falloutTime;
            
            @Override
            protected void positionUpdate(float tpf) {
                Vector3f oldPos = spatial.getLocalTranslation();
                if (!fallingOut) {
                    spatial.setLocalTranslation(oldPos.add(velocity.mult(tpf)));
                } else {
                    falloutTime += tpf;
                    spatial.setLocalTranslation(oldPos.add
                            (0f, P.platformWidth*(2*(-4)*falloutTime + 4)*tpf, P.platformWidth*tpf));
                
                }
            }

            @Override
            public void collideWithStatic() {
                if (this.hasHit) {
                    fallingOut = true;
                } else {
                    this.afterPlayerCollision();
                }
            }

            @Override
            public void afterPlayerCollision() {
                LinearBat.this.velocity.set(0f, -30f, 0f);
                LinearBat.this.channel.setLoopMode(LoopMode.DontLoop);
            }
            
        };
    }

    
}
