/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control.fireball;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import control.FireballControl;

/**
 *
 * @author dagen
 */
public class SpinningFireballControl extends FireballControl{
    private float time = 0;
    private final float radius = 5.0f;
    public SpinningFireballControl(){
        
    }
    
    @Override
    protected void positionUpdate(float tpf) {
        time += tpf;
                
        float x = tpf*radius*FastMath.cos(time);
        float y = tpf*radius*FastMath.sin(time);
        this.spatial.setLocalTranslation(this.spatial.getLocalTranslation().add(new Vector3f(x, y, 0.0f)));
    }
}
