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

    @Override
    protected Spatial createModel(AssetManager assetManager) {
        Node fireball = new Node();
        /*Sphere model =
         new Sphere(5,5,0.1f);
        
         Geometry geometry = new Geometry("", model);
         Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
         material.setColor("Color", ColorRGBA.Red);
         geometry.setMaterial(material);
        
         fireball.attachChild(geometry);*/
        ParticleEmitter fire = getFireballParticleEmitter(assetManager);
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
        //COLOR ARRAY FOR BETTER fIREBALLS
        ColorRGBA[] colorArray = new ColorRGBA[9];
        colorArray[0] = ColorRGBA.White;
        colorArray[1] = ColorRGBA.Blue;
        colorArray[2] = ColorRGBA.Red;
        colorArray[3] = ColorRGBA.Yellow;
        colorArray[4] = ColorRGBA.Cyan;
        colorArray[5] = ColorRGBA.Green;
        colorArray[6] = ColorRGBA.Magenta;
        colorArray[7] = ColorRGBA.Orange;
        colorArray[8] = ColorRGBA.Pink;
        Random r = new Random();
        int i = r.nextInt(9);
        int j = i;
        while (j == i) {
            j = r.nextInt(9);
        }
        //STOP COLOR ARRAY
        // fire.setStartColor(  new ColorRGBA(1f, 0f, 0f, 1f));   // red
        fire.setStartColor(colorArray[i]);
        fire.setEndColor(colorArray[j]);
        // fire.setEndColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
        fire.setStartSize(3.5f);
        fire.setEndSize(0.1f);
        fire.setGravity(0, 0, 0);
        fire.setLowLife(0.4f);
        fire.setHighLife(1f);
        fire.getParticleInfluencer().setVelocityVariation(0.3f);
        return fire;
    }
}
