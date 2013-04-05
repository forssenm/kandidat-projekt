package spatial.hazard;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import control.HazardControl;
import control.fireball.LinearFireballControl;

/**
 * A fireball using
 * <code>LinearFireballControl</code> to control its behaviour.
 * 
 * @author jonatankilhamn
 */
public class LinearFireballHazard extends FireballHazard {

    private Vector3f velocity;
    
    public LinearFireballHazard(AssetManager assetManager,Vector3f velocity) {
        this.velocity = velocity;
        this.attachChild(this.createModel(assetManager));
        this.addControl(this.createControl());
    }
    
    @Override
    protected HazardControl createControl() {
        return new LinearFireballControl(velocity);
    }
    
}
