package control;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import leveldata.LevelContentGenerator;
import spatial.hazard.LinearFireball;

/**
 *
 * @author jonatankilhamn
 */
@Deprecated
public class WizardControl extends AbstractControl implements LevelContentGenerator {

    private static final float fireballCoolDown = 6.0f;
    private static final float fireballSpeed = 15.0f;
    private LevelControl levelControl;
    private float time;
    private AssetManager assetManager;

    public WizardControl(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
    
    public void setLevelControl(LevelControl levelControl) {
        this.levelControl = levelControl;
    }

    @Override
    protected void controlUpdate(float tpf) {
        time += tpf;
        if (time > 5) {
            time -= fireballCoolDown;
            Vector3f direction = levelControl.getPlayer().getLocalTranslation().
                    subtract(this.spatial.getWorldTranslation());
            direction.normalizeLocal();
            LinearFireball fireball = new LinearFireball(assetManager, direction.mult(fireballSpeed));
             
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
