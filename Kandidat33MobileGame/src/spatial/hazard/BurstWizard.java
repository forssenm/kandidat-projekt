package spatial.hazard;

import com.jme3.asset.AssetManager;
import control.PlayerInteractorControl;
import control.wizard.BurstWizardControl;

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
    }

    @Override
    protected PlayerInteractorControl createControl() {
        BurstWizardControl wizardControl = new BurstWizardControl(assetManager);
        return wizardControl;
    }
    
    
}