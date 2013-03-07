/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestate;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.light.SpotLight;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.shadow.PssmShadowRenderer;
import java.io.IOException;

/**
 * A class to keep a SpotLight focussed on a spatial.
 * Optionally also keeps a PssmShadowRenderer in the same diretion,
 * making sure the shadow follows the light's movements.
 * 
 * @author jonatankilhamn
 */
public class ShadowedSpotlightControl implements Control {
    
    
    Spatial spatial;
    SpotLight spotlight;
    PssmShadowRenderer shadowRenderer;
    Vector3f defaultPositionOffset = new Vector3f(0,50,0);
    
    /**
     * @param spotlight The spotlight to keep aimed at a spatial.
     */
    public ShadowedSpotlightControl(SpotLight spotlight) {
        super();
        this.spotlight = spotlight;
    }

    /**
     * @Inheritdoc
     */
    public Control cloneForSpatial(Spatial spatial) {
        ShadowedSpotlightControl newSSC = new ShadowedSpotlightControl((SpotLight)spotlight.clone());
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
     * Sets the shadowrenderer to follow this light.
     * More than one shadowrenderers can be active in a scene, and could
     * then follow one light each.
     * @param shadowRenderer 
     */
    public void setShadowRenderer(PssmShadowRenderer shadowRenderer) {
        this.shadowRenderer = shadowRenderer;
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
        Vector3f positionOffset = defaultPositionOffset.
                add(Vector3f.ZERO);/*
                (float)(20*Math.sin(time)),
                (float)(10*(-1-2*Math.sin(2*time))),
                (float)(10*Math.sin(1.4*time)));*/
        Vector3f directionOffset = new Vector3f(0f,0f,0f);
        
        
        Vector3f spatialPosition = spatial.getLocalTranslation();
        Vector3f lightPos = spatialPosition.add(positionOffset);
        
        spotlight.setPosition(lightPos);
        spotlight.setDirection((spatialPosition.subtract(lightPos)).add(directionOffset));

        if (shadowRenderer != null) {
            shadowRenderer.setDirection(spotlight.getDirection());
        }
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
