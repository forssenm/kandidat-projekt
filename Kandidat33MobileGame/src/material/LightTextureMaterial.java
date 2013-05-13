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
        int mod = time % 15;
        float alpha = 1;
        if (mod == 0 || mod == 7) {
            alpha = 0.5f;
        } else if (mod == 1 || mod == 11) {
            alpha = 0.625f;
        } else if (mod == 9 || mod == 6 || mod == 12) {
            alpha = 0.75f;
        } else if (mod == 8 || mod == 4 || mod == 13 || mod == 10) {
            alpha = 0.875f;
        }
        this.setFloat("Alpha", alpha);
        super.render(geom, rm);
        
    }
    
    
}
