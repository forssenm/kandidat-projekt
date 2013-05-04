package spatial.hazard;

import com.jme3.asset.AssetManager;
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

    public BurstWizard(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.attachChild(createModel(assetManager));
        this.addControl(createControl());
        if(EffectSettings.ambientOcclusion == EffectSettings.AmbientOcclusion.TEXTURE) {
            this.attachChild(this.addWallOcclusion(assetManager, new Vector3f(0f, -1f, -4f)));
        }
        this.scale(0.8f);
        
    }

    @Override
    protected PlayerInteractorControl createControl() {
        return new AbstractWizardControl(assetManager) {
            protected static final float burstCoolDown = 5.0f;
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
}