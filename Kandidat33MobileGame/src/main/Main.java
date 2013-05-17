package main;

import com.jme3.app.DebugKeysAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import java.util.logging.Level;
import java.util.logging.Logger;
import state.InGameState;
import state.InMainMenuState;
import variables.P;

/**
 * The main application class. This sets up the relevant AppStates, starting
 * the game.
 * @author Mathias Forssén
 * @author David Dagson
 * @author Jonatan Kilhamn
 */
public class Main extends SimpleApplication {
    
    private InMainMenuState inMainMenuState;
    private InGameState inGameState;
    
    public static void main(String[] args) {
        AppSettings appSettings = new AppSettings(true);
        appSettings.setSamples(2);
        appSettings.setVSync(true);
        //appSettings.setFrameRate(30);
        appSettings.setResolution(800, 480);
        appSettings.setBitsPerPixel(16);
        Main app = new Main();
        app.setShowSettings(true);
        app.setSettings(appSettings); 
        app.start();
    }

    public Main(){
        /* This call to super makes sure to not load the flyCam. */
        super(new StatsAppState(), new DebugKeysAppState());
        //super(new DebugKeysAppState()); // use this to remove the stats in lower-left corner
        Logger.getLogger("").setLevel(Level.SEVERE);
        Logger.getLogger("Kandidat").setLevel(Level.FINE);
    }
    
    @Override
    public void simpleInitApp() {
        P.screenWidth = settings.getWidth();
        P.screenHeight = settings.getHeight();
        inGameState = new InGameState();
        inMainMenuState = new InMainMenuState();
        stateManager.attach(inMainMenuState);
        //stateManager.attach(inGameState);
        
        //FilterPostProcessor processor = (FilterPostProcessor) assetManager.loadAsset("Filters/PostFilter.j3f");
        //viewPort.addProcessor(processor);
        
        /*
        AmbientOcclusionFilter aof = new AmbientOcclusionFilter();
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        //Filter testFilter = new SSAOFilter(4, 3, 0.2f, 0.1f);
        Filter testFilter = aof;
        fpp.addFilter(testFilter);
        viewPort.addProcessor(fpp);
        */
    }

    @Override
    public void simpleUpdate(float tpf) {
    }
    
    public void gameStart() {
        this.inMainMenuState.setEnabled(false);
        if (!inGameState.isInitialized()) {
            this.stateManager.attach(inGameState);
        } else {
            inGameState.setEnabled(true);
            inGameState.restartLevel();
        }
    }
    
    public void gameOver() {
        //this.inGameState.setEnabled(false);
        this.inMainMenuState.setEnabled(true);
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }
    
}
