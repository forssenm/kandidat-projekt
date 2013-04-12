package spatial;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.util.Random;
import variables.P;

/**
 * A class for a non-physical window frame, purely for decoration.
 * @author jonatankilhamn
 */
public class Torch extends Node {

    /**
     * This constructor creates a
     * <code>Torch</code> represented by a
     * <code>Geometry</code> loaded internaly.
     *
     * @param assetManager is used to load the geometry and texture of
     * the <code>Window</code>.
     */
    public Torch(AssetManager assetManager, Vector3f position) {
        super("Torch");
        
        Node window = (Node)assetManager.loadModel("Models/torch/Torch.j3o");
       // window.scale(4);
       // window.rotate(90*FastMath.DEG_TO_RAD, 0f, 0f);
        window.attachChild (getTorchParticleEmitter(assetManager));
        this.attachChild(window);
        
       // this.setLocalTranslation(position.x, position.y, -P.platformWidth*2+0.5f);
        this.setLocalTranslation(position.x, position.y, 0);
        //this.setShadowMode(ShadowMode.Off);   

    }
    
    private ParticleEmitter getTorchParticleEmitter (AssetManager assetManager) {
             ParticleEmitter fire = 
            new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
    Material mat_red = new Material(assetManager, 
            "Common/MatDefs/Misc/Particle.j3md");
    mat_red.setTexture("Texture", assetManager.loadTexture(
            "Effects/Explosion/flame.png"));
   // mat_red.getAdditionalRenderState().setBlendMode(BlendMode.Alpha); för att kunna göra svarta partiklar
    fire.setMaterial(mat_red);
    fire.setImagesX(2); 
    fire.setImagesY(2); // 2x2 texture animation
    
    //Default values for a standard Torch
    ColorRGBA startColor = new ColorRGBA (0.9f, 0.3f, 0.1f, 0.8f);
    ColorRGBA endColor = new ColorRGBA (0.45f, 0.4f, 0f, 0.5f);
    Vector3f gravity = new Vector3f (0, -3f, 0);
    Vector3f initialVelocity = new Vector3f (0, 5, 0);
    float velocityVariation = 0.3f;
    float startSize = 1.4f;
    float endSize = 0.1f;
    float lowLife = 1.0f;
    float highLife = 1.4f;
        
    //Add different torches with differenc  velocity, gravity and life
    Random r = new Random();
    int i = r.nextInt(6); 
    //0,1,2,3 -> normal torch
    //4       -> fast burner
    //5       -> 1 out of 3 specials
    if (i == 5) {
        i = r.nextInt(3)+5;  //choose 5, 6 or 7 for a special effect
    }
    
    if (i == 4) { //faster burn, bigger flame
        gravity = new Vector3f (0, 0, 30f);
        startSize = 1.8f;
    }
    else if (i == 5) {  //dripping green stuff
       gravity = new Vector3f (0, 30f, 0);
       startColor = new ColorRGBA (0, 1f, 0.3f, 1f);
       velocityVariation = 0.5f;
       fire.setNumParticles(20);
       startSize = 1.2f;
       lowLife = 0.3f;
       highLife = 1f;
    }
    else if (i == 6) {  //red growing 
       fire.setNumParticles(10);
       startSize = 0.4f;
       endSize = 2.7f;
       lowLife = 1.7f;
       highLife = 2.2f;
       startColor = new ColorRGBA (0.1f, 0.1f, 0.1f, 1f);
       endColor   = ColorRGBA.Red;
       velocityVariation = 0.05f;
       initialVelocity.y = 1;
       gravity.y = -1;
    }
    
    else if (i == 7) {  //smoke 
       fire.setNumParticles(25);
       startSize = 0.1f;
       endSize = 1f;
       lowLife = 1.6f;
       highLife = 1.70f;
       startColor = new ColorRGBA (0.1f, 0.1f, 0.1f, 0.4f);
       endColor   = new ColorRGBA (0.2f, 0.2f, 0.2f, 0.4f);
       velocityVariation = 0.15f;
       initialVelocity.y = 1;
       gravity.y = -1;
    }
    
    fire.setStartColor(  startColor);
    fire.setEndColor(endColor);
    fire.getParticleInfluencer().setInitialVelocity(initialVelocity);
    fire.setStartSize(startSize);
    fire.setEndSize(endSize);
    fire.setGravity(gravity);
    fire.setLowLife(lowLife);
    fire.setHighLife(highLife);
    fire.getParticleInfluencer().setVelocityVariation(velocityVariation);
    return fire;
    }
    
}
