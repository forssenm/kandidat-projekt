package spatial.hazard;

import spatial.PlayerInteractor;
import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import java.util.Random;

/**
 * An abstract AbstractFireball. Any class extending this one will make a PlayerInteractor
 * that looks like a fireball.
 *
 * @author jonatankilhamn
 */
public abstract class AbstractFireball extends PlayerInteractor {
    
    private ParticleEmitter fire;
    
    private static final ColorRGBA[] colorArray = new ColorRGBA[9];
    
    //for each of the colors in the array, make a list of forbidden combo colors
    private static int[][] forbidden = {{},{8,6},{6,8},{7,5},{1,5},{4},{1,2},{},{1,2}};
    static {
        colorArray[0] = ColorRGBA.White;
        colorArray[1] = ColorRGBA.Blue;
        colorArray[2] = ColorRGBA.Red;
        colorArray[3] = ColorRGBA.Yellow;
        colorArray[4] = ColorRGBA.Cyan;
        colorArray[5] = ColorRGBA.Green;
        colorArray[6] = ColorRGBA.Magenta;
        colorArray[7] = ColorRGBA.Orange;
        colorArray[8] = ColorRGBA.Pink; 
    }
    
    private boolean forbiddenColorPair(int x, int y) {
        for (int i = 0; i < forbidden[x].length; i++) {
            if (forbidden[x][i] == y) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected Spatial createModel(AssetManager assetManager) {
        Node fireball = new Node();
        
        fire = getFireballParticleEmitter(assetManager);
        fireball.attachChild(fire);

        return fireball;
    }

    private ParticleEmitter getFireballParticleEmitter(AssetManager assetManager) {
        ParticleEmitter fire =
                new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
        Material mat_red = new Material(assetManager,
                "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture(
                "Effects/Explosion/flame.png"));
        fire.setMaterial(mat_red);
        fire.setImagesX(2);
        fire.setImagesY(2); // 2x2 texture animation
        
        Random r = new Random();
        int i = r.nextInt(9);
        int j = i;
        while (j == i  || forbiddenColorPair (i,j)) {
            j = r.nextInt(9);
        }
       
        fire.setStartColor(colorArray[i]);
        fire.setEndColor(colorArray[j]);
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
        fire.setStartSize(3.5f);
        fire.setEndSize(0.1f);
        fire.setGravity(0, 0, 0);
        fire.setLowLife(0.4f);
        fire.setHighLife(1f);
        fire.getParticleInfluencer().setVelocityVariation(0.3f);
        return fire;
    }
    
    public void destroy() {
        this.setName("");
        fire.setHighLife(0f);
        fire.setLowLife(0f);
    }
    
}
