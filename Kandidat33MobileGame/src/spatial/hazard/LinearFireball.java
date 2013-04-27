package spatial.hazard;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import control.AbstractHazardControl;
import control.PlayerInteractorControl;

/**
 * A fireball using
 * <code>LinearFireballControl</code> to control its behaviour.
 *
 * @author jonatankilhamn
 */
public class LinearFireball extends AbstractFireball {

    private Vector3f velocity;

    public LinearFireball(AssetManager assetManager, Vector3f velocity) {
        this.velocity = velocity;
        this.attachChild(this.createModel(assetManager));
        this.addControl(this.createControl());
    }

    @Override
    protected PlayerInteractorControl createControl() {
        return new AbstractHazardControl(1f) {
            
            @Override
            protected void positionUpdate(float tpf) {
                Vector3f oldPos = spatial.getLocalTranslation();
                spatial.setLocalTranslation(oldPos.add(velocity.mult(tpf)));
            }

            @Override
            public void collideWithStatic() {
                LinearFireball.this.destroy();
            }

            @Override
            public void afterPlayerCollision() {
                LinearFireball.this.destroy();
            }
        };
    }
}
