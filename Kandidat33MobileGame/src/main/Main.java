package main;

import com.jme3.app.DebugKeysAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.Filter;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import filters.AmbientOcclusionFilter;
import filters.MosaicFilter;
import filters.RedOverlayFilter;
import java.util.logging.Level;
import java.util.logging.Logger;
import state.InGameState;
import state.InMainMenuState;
import variables.P;

/**
 * The main application class. This sets up the relevant AppStates, starting
 * the game.
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    public static void main(String[] args) {
        AppSettings appSettings = new AppSettings(true);
        appSettings.setSamples(2);
        appSettings.setVSync(true);
        Main app = new Main();
        app.setSettings(appSettings); 
        app.start();
    }

    public Main(){
        /* This call to super makes sure to not load the flyCam. */
        super(new StatsAppState(), new DebugKeysAppState());
        Logger.getLogger("").setLevel(Level.SEVERE);
        Logger.getLogger("Kandidat").setLevel(Level.FINE);
    }
    
    
    
    @Override
    public void simpleInitApp() { 
        P.screenWidth = settings.getWidth();
        P.screenHeight = settings.getHeight();
        stateManager.attach(new InGameState());
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
        
        //stateManager.attach(new InMainMenuState());
    }

    @Override
    public void simpleUpdate(float tpf) {
            //System.out.println(viewPort.getCamera().getScreenCoordinates(viewPort.getCamera().getWorldCoordinates(new Vector2f(10, 10), 0)));
            
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    // BAra skriver en kommentar för att testa push
}
