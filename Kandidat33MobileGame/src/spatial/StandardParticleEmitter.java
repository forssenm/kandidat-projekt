/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spatial;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.asset.AssetManager;
/**
 *
 * @author natwei
 */
public class StandardParticleEmitter extends ParticleEmitter {
    
    public StandardParticleEmitter () {}
    public static ParticleEmitter make (AssetManager assetManager) {
        ParticleEmitter fire = 
            new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            Material mat_red = new Material(assetManager, 
            "Common/MatDefs/Misc/Particle.j3md");
            mat_red.setTexture("Texture", assetManager.loadTexture(
            "Textures/Explosion/flame.png"));
            fire.setMaterial(mat_red);
            fire.setImagesX(2); 
            fire.setImagesY(2); // 2x2 texture animation
        return fire;
    }
}
