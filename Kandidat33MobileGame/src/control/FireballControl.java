/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.io.IOException;
import spatial.Player;

/**
 *
 * @author jonatankilhamn
 */
public class FireballControl implements HazardControl {
    
    Spatial spatial;
    boolean hasHit = false;

    public void collideWithPlayer(Player player) {
        if (!hasHit) {
        RunningControl rC = player.getControl(RunningControl.class);
            rC.setJumpSpeed(rC.getJumpSpeed()-2f);
        hasHit = true;
        }
    }

    public Control cloneForSpatial(Spatial spatial) {
        FireballControl newControl = new FireballControl();
        spatial.addControl(newControl);
        return newControl;
    }

    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
    }

    public void update(float tpf) {
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
