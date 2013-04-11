package spatial.hazard;

import com.jme3.asset.AssetManager;
import control.HazardControl;
import control.wizard.SingleShotWizardControl;

/**
 * A class for a Wizard, using a
 * <code>AbstractWizardControl</code> to control its behaviour.
 *
 * @author jonatankilhamn
 */
public class SingleShotWizard extends AbstractWizard {
    private final AssetManager assetManager;

    public SingleShotWizard(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.attachChild(createModel(assetManager));
        this.addControl(createControl());
    }

    @Override
    protected HazardControl createControl() {
        SingleShotWizardControl wizardControl = new SingleShotWizardControl(assetManager);
        return wizardControl;
    }
    
    
}