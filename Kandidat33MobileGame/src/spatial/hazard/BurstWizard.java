package spatial.hazard;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import control.AbstractWizardControl;
import control.PlayerInteractorControl;
import spatial.Player;
import variables.EffectSettings;

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
       
        if(EffectSettings.ambientOcclusion == EffectSettings.AmbientOcclusion.TEXTURE) {
            this.attachChild(this.addWallOcclusion(assetManager, new Vector3f(0f, -1f, -4f)));
        }
        this.scale(0.8f);
        
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
    
    private void redress() { //Changes wizard to black clothes and red particle
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors",true);
        mat.setColor("Diffuse", ColorRGBA.Black);
        this.getChild("Cylinder.0031").setMaterial(mat);
        ((ParticleEmitter)this.getChild("spark")).setStartColor(ColorRGBA.Red);
    }

}