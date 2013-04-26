package spatial.hazard;

import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import control.AbstractHazardControl;
import control.PlayerInteractorControl;

/**
 * A fireball using
 * <code>SpinningFireballControl</code> to control its behaviour.
 *
 * @author jonatankilhamn
 */
public class SpinningFireball extends AbstractFireball {

    public SpinningFireball(AssetManager assetManager) {
        this.attachChild(this.createModel(assetManager));
        this.addControl(this.createControl());
    }

    @Override
    protected PlayerInteractorControl createControl() {
        return new AbstractHazardControl(1f) {
            private float time = 0;
            private final float radius = 5.0f;

            @Override
            protected void positionUpdate(float tpf) {
                time += tpf;

                float x = tpf * radius * FastMath.cos(time);
                float y = tpf * radius * FastMath.sin(time);
                this.spatial.setLocalTranslation(this.spatial.getLocalTranslation().add(new Vector3f(x, y, 0.0f)));
            }

            @Override
            public void collideWithStatic() {
                SpinningFireball.this.destroy();
            }

            @Override
            public void afterPlayerCollision() {
                SpinningFireball.this.destroy();
            }
        };
    }
}
