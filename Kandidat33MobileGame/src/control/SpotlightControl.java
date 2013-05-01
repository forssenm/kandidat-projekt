package control;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.light.SpotLight;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 * A class to keep a SpotLight focussed on a spatial.
 * Optionally also keeps a PssmShadowRenderer in the same diretion,
 * making sure the shadow follows the light's movements.
 * 
 * @author jonatankilhamn
 */
public class SpotlightControl implements Control {
    
    
    Spatial spatial;
    SpotLight spotlight;
    Vector3f defaultPositionOffset = new Vector3f(0,20,30);
    Vector3f directionOffset = new Vector3f();
    Vector3f positionOffset = new Vector3f();
    Vector3f spatialPosition = new Vector3f();
    Vector3f lightPosition = new Vector3f();
    
    /**
     * @param spotlight The spotlight to keep aimed at a spatial.
     */
    public SpotlightControl(SpotLight spotlight) {
        super();
        this.spotlight = spotlight;
    }

    /**
     * @Inheritdoc
     */
    public Control cloneForSpatial(Spatial spatial) {
        SpotlightControl newSSC = new SpotlightControl((SpotLight)spotlight.clone());
        newSSC.setSpatial(spatial);
        return newSSC;
    }

    /**
     * @Inheritdoc
     */
    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
        update(0f);
    }

    /**
     * Sets the default relative position between the spotlight
     * and the spatial it is shining on.
     * @param defaultPositionOffset 
     */
    public void setDefaultPositionOffset(Vector3f defaultPositionOffset) {
        this.defaultPositionOffset = defaultPositionOffset;
    }

    private float time;
    
    public void update(float tpf) {
        time += tpf;
        positionOffset.set(defaultPositionOffset).
                addLocal(
                (float)(20*Math.sin(time)),
                (float)(10*(-1-2*Math.sin(2*time))),
                /*(float)(10*Math.sin(1.4*time))*/0f);       
        
        spatialPosition.set(spatial.getLocalTranslation());
        lightPosition.set(spatialPosition).addLocal(positionOffset);
        
        spotlight.setPosition(lightPosition);
        spotlight.setDirection((spatialPosition.subtract(lightPosition)).addLocal(directionOffset));

    }

    public void render(RenderManager rm, ViewPort vp) {
    }

    public void write(JmeExporter ex) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void read(JmeImporter im) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
