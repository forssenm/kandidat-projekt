/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spatial.hazard;

import com.jme3.asset.AssetManager;
import control.HazardControl;
import control.fireball.HoveringFireballControl;

/**
 *
 * @author jonatankilhamn
 */
public class HoveringFireballHazard extends FireballHazard {

    public HoveringFireballHazard(AssetManager assetManager) {
        this.attachChild(this.createModel(assetManager));
        this.addControl(this.createControl());
    }
    
    @Override
    protected HazardControl createControl() {
        return new HoveringFireballControl();
    }
    
}
