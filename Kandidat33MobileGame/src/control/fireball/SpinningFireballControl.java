package control.fireball;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import control.FireballControl;

/**
 * A control moving a fireball in a counter-clockwise circle in the x-y-plane.
 * @author dagen
 */
public class SpinningFireballControl extends FireballControl{
    private float time = 0;
    private final float radius = 5.0f;
    
    @Override
    protected void positionUpdate(float tpf) {
        time += tpf;
                
        float x = tpf*radius*FastMath.cos(time);
        float y = tpf*radius*FastMath.sin(time);
        this.spatial.setLocalTranslation(this.spatial.getLocalTranslation().add(new Vector3f(x, y, 0.0f)));
    }
}
