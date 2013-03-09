package main;

import com.jme3.app.DebugKeysAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.renderer.RenderManager;
import state.InGameState;
import state.InMainMenuState;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    public Main(){
        /* This call to super makes sure to not load the flyCam. */
        super(new StatsAppState(), new DebugKeysAppState());
    }
    
    @Override
    public void simpleInitApp() {
        stateManager.attach(new InGameState());
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    // BAra skriver en kommentar för att testa push
}
