package gamestate;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;

/**
 * This class activates the user controls used when the player is running. 
 * In The current implementation this means that jumps are triggered by 
 * <code>KeyInput.KEY_SPACE</code> and <code>MouseInput.BUTTON_LEFT</code>.
 * <br/> <br/>
 * Relavant things to add to this class could be to switch 
 * controls related to the player.
 * @author dagen
 */
public class RunningState extends AbstractAppState{
    private InputManager inputManager;
    
    /**
     * Initializes key mappings associated with the RunningState by 
     * running <code>initInput()</code>. 
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        inputManager = app.getInputManager();
        this.initInput();
    }
    
    /**
     * Initializes user input associated with the RunningState.
     * Mapps <code>KeyInput.KEY_SPACE</code> and 
     * <code>MouseInput.BUTTON_LEFT</code> to the "jump" mapping.
     */
    private void initInput(){
        inputManager.addMapping("jump", 
                new KeyTrigger(KeyInput.KEY_SPACE),
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT)
                );
    }
    
    /**
     * Clean up the input mappings associated with the RunningState.
     */
    @Override
    public void cleanup() {
        this.cleanupInput();
        super.cleanup();
    }
    
    /**
     * Cleans up the user input associated with the <code>RunningState</code>.
     * Deletes the "jump" input mapping.
     */
    private void cleanupInput(){
        inputManager.deleteMapping("jump");
    }
}
