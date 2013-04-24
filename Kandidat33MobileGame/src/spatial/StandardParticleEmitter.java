package spatial;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
/**
 *
 * @author natwei
 */
public class StandardParticleEmitter extends ParticleEmitter {
    
    //private static Material materialForParticles;
    
    public StandardParticleEmitter () {}
    public static ParticleEmitter make (AssetManager assetManager) {
        ParticleEmitter fire = 
            new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            Material materialForParticles = new Material(assetManager, 
            "Common/MatDefs/Misc/Particle.j3md");
            materialForParticles.setTexture("Texture", assetManager.loadTexture(
            "Textures/Explosion/flame.png"));
        

            fire.setMaterial(materialForParticles.clone());
            fire.setImagesX(2); 
            fire.setImagesY(2); // 2x2 texture animation
        return fire;
    }
}
