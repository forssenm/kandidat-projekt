package material;

import com.jme3.asset.AssetManager;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.shader.Shader;
import com.jme3.shader.Uniform;
import com.jme3.shader.VarType;
import java.util.Date;
import variables.P;

/**
 *
 * @author Nina
 */
public class WallMaterial extends Material {
    
    
    
     
    public WallMaterial(MaterialDef def) {
        super(def);
    }

    public WallMaterial(AssetManager contentMan, String defName) {
        super(contentMan, defName);
    }

    public WallMaterial() {
        super();
    }
    
    @Override
    public void render(Geometry geom, RenderManager rm) {
        
        this.setVector3("SunColor", P.currentSunColor.divide(2));
        super.render(geom, rm);
        
    }
        
}
