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
            ParticleEmitter fire = getFireballParticleEmitter();
            fireball.attachChild(fire);
             
            // STOP FIREBALL TEST
            this.levelControl.addToLevel(fireball,this.spatial.getWorldTranslation());
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public ParticleEmitter getFireballParticleEmitter () {
    //   FIREBALL TEST
             ParticleEmitter fire = 
            new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
    Material mat_red = new Material(assetManager, 
            "Common/MatDefs/Misc/Particle.j3md");
    mat_red.setTexture("Texture", assetManager.loadTexture(
            "Effects/Explosion/flame.png"));
    fire.setMaterial(mat_red);
    fire.setImagesX(2); 
    fire.setImagesY(2); // 2x2 texture animation
    fire.setStartColor(  new ColorRGBA(1f, 0f, 0f, 1f));   // red
    fire.setEndColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
    fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
    fire.setStartSize(3.5f);
    fire.setEndSize(0.1f);
    fire.setGravity(0, 0, 0);
    fire.setLowLife(0.4f);
    fire.setHighLife(1f);
    fire.getParticleInfluencer().setVelocityVariation(0.3f);
    return fire;
    }
}
