package spatial.hazard;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import control.AbstractWizardControl;
import control.PlayerInteractorControl;
import java.util.Random;
import spatial.Player;

/**
 * A class for a Wizard, using a
 * <code>BurstWizardControl</code> to control its behaviour.
 *
 * @author jonatankilhamn
 */
public class BurstWizard extends AbstractWizard {

    private final AssetManager assetManager;

    public BurstWizard(AssetManager assetManager, SpotLight spotlight) {
        super(spotlight);
        this.assetManager = assetManager;
        this.attachChild(createModel(assetManager));
        this.addControl(createControl());
       
        this.redress();
       
    }

    @Override
    protected PlayerInteractorControl createControl() {
        return new AbstractWizardControl(assetManager, spotlight) {
            protected static final float burstCoolDown = 2.5f;
            protected static final float fireballCoolDown = 0.5f;
            protected int shotsFired;

            @Override
            protected void shootAtPlayerAndReload(Player player) {
                this.shootFireballAt(player.getLocalTranslation());

                readyToShoot = false;
                shotsFired++;
                if (shotsFired < 3) {
                    reloadTimer -= fireballCoolDown;
                } else {
                    shotsFired = 0;
                    reloadTimer -= burstCoolDown;

                }
            }
        };
    }
    
    private void redress() {
        Spatial apa = (Spatial) this.getChild("Cylinder.0031");
        //apa.getChild("Mesh").setMaterial(null);
        
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors",true);
        mat.setColor("Diffuse", ColorRGBA.Black);
        apa.setMaterial(mat);
        
        ParticleEmitter spark = (ParticleEmitter)this.getChild("spark");
        spark.setStartColor(ColorRGBA.Red);
        
        
    }
}