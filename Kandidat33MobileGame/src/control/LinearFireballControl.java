/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author jonatankilhamn
 */
public class LinearFireballControl extends FireballControl {

    private Vector3f velocity;
   
    
    @Override
    protected void positionUpdate(float tpf) {
        Vector3f oldPos = spatial.getLocalTranslation();
        spatial.setLocalTranslation(oldPos.add(velocity.mult(tpf)));
    }
    
}
