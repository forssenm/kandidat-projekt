package gamestate;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 * This is just a skeleton for controling the camera.
 * @author dagen
 */
public class CameraControl extends AbstractControl {
    
    @Override
    protected void controlUpdate(float tpf) {
        /* TODO Add code that moves the camera to look 
         * at the current spatial i.e. the player
         */
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        /* Do nothing */
    }
    
    /* This should be implemented since a camera can follow a 
     * spatial without being active*/
    public Control cloneForSpatial(Spatial spatial) {
        CameraControl control = new CameraControl();
        //TODO: copy parameters to new Control
        control.setSpatial(spatial);
        return control;
    }
    
    /* If we want to save and read camera settings etc 
     * else ignore.*/
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule in = im.getCapsule(this);
        //TODO: load properties of this Control, e.g.
        //this.value = in.readFloat("name", defaultValue);
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule out = ex.getCapsule(this);
        //TODO: save properties of this Control, e.g.
        //out.write(this.value, "name", defaultValue);
    }
}
