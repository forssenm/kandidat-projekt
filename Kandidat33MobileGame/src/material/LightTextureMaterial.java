package material;

import com.jme3.asset.AssetManager;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.shader.Shader;
import com.jme3.shader.Uniform;
import com.jme3.shader.VarType;
import java.util.Date;

/**
 *
 * @author Nina
 */
public class LightTextureMaterial extends Material {
    
    private static final Date startTime = new Date();
    
     
    public LightTextureMaterial(MaterialDef def) {
        super(def);
    }

    public LightTextureMaterial(AssetManager contentMan, String defName) {
        super(contentMan, defName);
    }

    public LightTextureMaterial() {
        super();
    }
    
    @Override
    public void render(Geometry geom, RenderManager rm) {
        int time = (int)(new Date().getTime() - startTime.getTime())/100;
        float alpha = 1;
        if (time % 2 == 0) {
            alpha = 0;
        }
        this.setFloat("Alpha", alpha);
        super.render(geom, rm);
        
    }
    
    
}
