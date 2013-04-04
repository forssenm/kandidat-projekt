/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control.fireball;

import com.jme3.math.Vector3f;
import control.FireballControl;

/**
 *
 * @author jonatankilhamn
 */
public class LinearFireballControl extends FireballControl {

    private Vector3f velocity;

    public LinearFireballControl(Vector3f velocity) {
        this.velocity = velocity;
    }
    
    @Override
    protected void positionUpdate(float tpf) {
        Vector3f oldPos = spatial.getLocalTranslation();
        spatial.setLocalTranslation(oldPos.add(velocity.mult(tpf)));
    }
    
}
