package control;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import leveldata.LevelContentGenerator;
import spatial.Player;
import spatial.hazard.LinearFireballHazard;

/**
 *
 * @author jonatankilhamn
 */
public class WizardControl extends AbstractControl implements LevelContentGenerator {
    //private Player player;

    private LevelControl levelControl;
    private float time;
    private AssetManager assetManager;

    public WizardControl(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
    
    /*
     public void setPlayer(Player player) {
     this.player = player;
     }*/
    
    public void setLevelControl(LevelControl levelControl) {
        this.levelControl = levelControl;
    }

    @Override
    protected void controlUpdate(float tpf) {
        time += tpf;
        if (time > 1) {
            time -= 15;
            Vector3f direction = levelControl.getPlayer().getLocalTranslation().
                    subtract(this.spatial.getWorldTranslation());
            direction.normalizeLocal();
            LinearFireballHazard fireball = new LinearFireballHazard(assetManager, direction.mult(15));
            this.levelControl.addToLevel(fireball,this.spatial.getWorldTranslation());
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
