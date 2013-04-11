package spatial.hazard;

import com.jme3.asset.AssetManager;
import control.HazardControl;
import control.wizard.BurstWizardControl;

/**
 * A class for a Wizard, using a
 * <code>AbstractWizardControl</code> to control its behaviour.
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
    protected HazardControl createControl() {
        BurstWizardControl wizardControl = new BurstWizardControl(assetManager);
        return wizardControl;
    }
    
    
}