package spatial.hazard;

import com.jme3.asset.AssetManager;
import control.HazardControl;
import control.wizard.CalculatingWizardControl;

/**
 * A class for a Wizard, using a
 * <code>CalculatingWizardControl</code> to control its behaviour.
 *
 * @author jonatankilhamn
 */
public class CalculatingWizard extends AbstractWizard {
    private final AssetManager assetManager;

    public CalculatingWizard(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.attachChild(createModel(assetManager));
        this.addControl(createControl());
    }

    @Override
    protected HazardControl createControl() {
        CalculatingWizardControl wizardControl = new CalculatingWizardControl(assetManager);
        return wizardControl;
    }
    
    
}