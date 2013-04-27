package spatial;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.Random;
import variables.P;

/**
 * A class for a non-physical torch, purely for decoration.
 * @author jonatankilhamn
 */
public class Torch extends Node {

    private static Node modelForTorch;
    
    /**
     * This constructor creates a
     * <code>Torch</code> represented by a
     * <code>Geometry</code> loaded internally.
     *
     * @param assetManager is used to load the geometry and texture of
     * the <code>Window</code>.
     */
    public Torch(AssetManager assetManager, Vector3f position) {
        super("Torch");
        
        if (modelForTorch == null) {
            modelForTorch = (Node)assetManager.loadModel("Models/torch/Torch-nolightcam.j3o");
        }
        
        Node model = (Node)modelForTorch.clone();
       // window.scale(4);
       // window.rotate(90*FastMath.DEG_TO_RAD, 0f, 0f);
        model.attachChild (getTorchParticleEmitter(assetManager));
        this.attachChild(model);
        
       // this.setLocalTranslation(position.x, position.y, -P.platformWidth*2+0.5f);
        this.setLocalTranslation(position.x, position.y, -P.platformWidth/2-P.playerZOffset+0.6f);
        //this.setShadowMode(ShadowMode.Off);   

    }
    
    private ParticleEmitter getTorchParticleEmitter (AssetManager assetManager) {
        
        ParticleEmitter fire = StandardParticleEmitter.make(assetManager);
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
    int[] type = {0,0,0,0,0,0,0,1,1,1,1,2,2,2,3,4,5,5,5,5,5};
    int i = r.nextInt(type.length); 
    //type 0 is default torch
    if (type[i] == 1) { //faster burn, bigger flame
        gravity = new Vector3f (0, 0, 30f);
        startSize = 1.8f;
    }
    else if (type[i] == 2) {  //smoke 
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
    else if (type[i] == 3) {  //dripping green stuff
       gravity = new Vector3f (0, 30f,0 );
       startColor = new ColorRGBA (0, 1f, 0.3f, 1.0f);
       initialVelocity.z = 5;
       velocityVariation = 0.5f;
       fire.setNumParticles(25);
       startSize = 1.7f;
       lowLife = 0.3f;
       highLife = 1f;
    }
    else if (type[i] == 4) {  //red growing 
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
    else if (type[i] == 5) {  //standard torch with random factors
        float redMod = r.nextFloat() * 4f / 10f -0.3f;   // from -0.3 to +0.1
        float greenMod = r.nextFloat() * 3f / 10f -0.1f;  //from -0.1 to +0.2
        
        startColor.r += redMod;
        endColor.r   += redMod;
        startColor.g += greenMod;
        endColor.g   += greenMod;
      
        gravity.y += r.nextInt(3) -1;
        //velocityVariation += (r.nextFloat()*0.3f-0.1f);  // -0.1 to +0.1
        
        
        
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
