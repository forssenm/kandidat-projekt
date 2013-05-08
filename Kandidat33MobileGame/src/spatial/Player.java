package spatial;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import control.PlayerControl;
import java.util.Iterator;
import java.util.Random;
import state.InGameState;
import variables.EffectSettings;
import variables.EffectSettings.AmbientOcclusion;
import variables.P;

/**
 * Class for keeping everything relating to the player in one place. Currently
 * keeps the spatial (along with the model) and the physics control object which
 * makes the player run and fall.
 *
 * @author dagen
 */
public class Player extends Node implements AnimEventListener {

    private PlayerControl playerControl;
    private Node playerModel;
    private InGameState game;
    private Random random;
    
    private Geometry floorOcclusion;

    //animation
    private AnimChannel channel;
    private AnimControl control;
    //end animation
    /**
     * Constructs a
     * <code>Player</code> object which is a
     * <Code>Node</code> with the players 3D-model attached to it. The
     * <code>Player</code> also has a
     * <code>PlayerControl</code> attached.
     * @param assetManager is used for loading the players assets i.e. 3D-model
     * and textures.
     */
    public Player(AssetManager assetManager, InGameState game) {
        super("player");
        this.game = game;
        this.random = new Random();
        // the player casts shadows
        this.setShadowMode(RenderQueue.ShadowMode.Cast);

        // starting position
        this.setLocalTranslation(0.0f, 5.0f, 0.0f);
        
        float hoverHeight = 1;
                
        // set up the physics control
        playerControl = new PlayerControl(1f, 4f+hoverHeight);
        playerControl.setSpeedFactor(P.speedFactor);
        this.addControl(playerControl);

        //Sets the model of the player
        if (EffectSettings.ambientOcclusion == AmbientOcclusion.TEXTURE) {
            playerModel = (Node) assetManager.loadModel("Models/ghost/AO/ghost-with-ao.j3o");
            playerModel.setLocalTranslation(0f,1.8f+hoverHeight,0f); 
            control = playerModel.getChild("Plane").getControl(AnimControl.class);
            channel = control.createChannel();
            channel.setAnim("ArmatureAction");
            
            this.attachChild(this.addWallOcclusion(assetManager, hoverHeight));
            floorOcclusion = this.addFloorOcclusion(assetManager, hoverHeight);
            this.attachChild(floorOcclusion);
        } else {
            
            playerModel = (Node) assetManager.loadModel("Models/ghost/ghost04-014cloth003armature003UV002.j3o");
            playerModel.rotate(0, -2.07f, 0);
            playerModel.scale(1f);
            playerModel.setLocalTranslation(0f,2.8f+hoverHeight,0f); 
            control = playerModel.getChild("Sphere").getControl(AnimControl.class);
            channel = control.createChannel();
            channel.setAnim("ArmatureAction.000");
            // Nina's
            /*playerModel = (Node) assetManager.loadModel("Models/ghost/ghost2-moreanim-nolightcam.j3o");
            playerModel.setLocalTranslation(0f,1.8f+hoverHeight,0f); 
            control = playerModel.getChild("Plane").getControl(AnimControl.class);*/
        }
        
        ParticleEmitter dust = this.getDustParticleEmitter(assetManager);
        playerModel.attachChild(dust);
        dust.move(0.6f, -2.0f, 0f);
        
        this.attachChild(playerModel);
        //All the code below is for animation of the model
        
        
        control.addListener(this);
        
        channel.setLoopMode(LoopMode.Loop);

      //End of animation code
        
        // sound
        AudioNode jumpSoundNode = new AudioNode(assetManager, "Sound/Effects/Gun.wav", false);
        jumpSoundNode.setName("jumpsound");
        this.attachChild(jumpSoundNode);
        
        AudioNode pickupPowerup = new AudioNode(assetManager, "Sound/Effects/sfx-powerup-pickup-2.ogg", false);
        pickupPowerup.setName("pickupPowerup");
        this.attachChild(pickupPowerup);
    }

    /**
     *
     * @return a reference to the players 3D-model.
     */
    public Node getPlayerModel() {
        return this.playerModel;
    }
    
    public enum Powerup {
        SPEED, INVULN, DOUBLEJUMP
    }
    
    public void animateCollision () {
        if (EffectSettings.ambientOcclusion == AmbientOcclusion.TEXTURE) {
              return;
          }
        //chooses between 3 different get hit animations
        int a = random.nextInt(60);
        channel.setSpeed(1.5f);
        
        if (a < 20) {
            channel.setAnim ("ArmatureAction.001"); //belly punch
            channel.setSpeed (2f);
            return;
        }
        if (a > 40) {
            channel.setAnim ("ArmatureAction.002"); //backflip
            channel.setSpeed(2.4f);
            return;
        }
        
        channel.setAnim("ArmatureAction.003"); //shrink 
        
    }
    
    private void setFrenzyAnimation(boolean frenzy) {
        if (EffectSettings.ambientOcclusion == AmbientOcclusion.TEXTURE) {
              return;
          }
        //invulnerability also makes you tougher and raging!
        if (frenzy) {
            channel.setAnim("frenzy");
            channel.setSpeed(1.15f);
        } else {
            channel.setAnim("ArmatureAction.000");
            channel.setSpeed(1.0f);
        }
    }
    
    private void animateJump() {
        //does not allow switching from frenzy / get hit animation into jump
        String s = channel.getAnimationName();
        if (s.equals("ArmatureAction.000") || s.equals( "jump")) {
            channel.setAnim ("jump");
            channel.setSpeed(1.8f);
        }
    }


    private Geometry addWallOcclusion(AssetManager assetManager, float hoverHeight) {
        Quad wallAO = new Quad(13, 13);
        Geometry wall = new Geometry("wallOcclusion", wallAO);
        wall.setLocalTranslation(-wallAO.getWidth()/2, -wallAO.getHeight()/2+hoverHeight, -P.platformWidth/2-P.playerZOffset+0.2f);
        Material wallMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wallMaterial.setTexture("ColorMap", assetManager.loadTexture("Models/ghost/AO/wall-ao-transparent.png"));
        wallMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha); // activate transparency
        wall.setMaterial(wallMaterial);
        wall.setQueueBucket(Bucket.Transparent);
        return wall;
    }
    
    private Geometry addFloorOcclusion(AssetManager assetManager, float hoverHeight) {
        Box floorAO = new Box(6.5f, 0f, 6.5f);
        Geometry floor = new Geometry("floorOcclusion", floorAO);
        floor.setLocalTranslation(0f, 0.25f, 0f);
        Material floorMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        floorMaterial.setTexture("ColorMap", assetManager.loadTexture("Models/ghost/AO/floor-ao-transparent.png"));
        floorMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha); // activate transparency
        floor.setMaterial(floorMaterial);
        floor.setQueueBucket(Bucket.Transparent);
        return floor;
    }
    
    public void updateModelAfterPowerup(Powerup powerup, boolean setting) {
        ParticleEmitter dust = (ParticleEmitter)this.playerModel.getChild("Emitter");
        switch(powerup) {
            case SPEED:
                if (setting) {
                    dust.setHighLife(4f);
                } else {
                    dust.setHighLife(0.2f);
                }
                break;
            case INVULN:
                game.setInvulnerable(setting);
                this.setFrenzyAnimation(setting);
                if (setting) {
                    dust.setGravity(0f,20f,0f);
                    dust.setEndSize(3.5f);
                } else {
                    dust.setGravity(0f,0f,0f);
                    dust.setEndSize(0.1f);
                }
                break;
            case DOUBLEJUMP:
                if (setting) {
                    dust.setEndColor(ColorRGBA.Green);
                } else {
                    dust.setEndColor(new ColorRGBA(0.05f, 0.05f, 0.05f, 0.5f));
                }
                break;
                
        }
    }
    
    public void updateModelAfterJump() {
        if (EffectSettings.ambientOcclusion == AmbientOcclusion.TEXTURE) {
              return;
          }
        this.animateJump();
        //((AudioNode)this.getChild("jumpsound")).playInstance();
    }
    
    /**
     * Call this to stop the player in its tracks. Useful for a delay before
     * respawning – the player won't fall miles away from the level.
     * @param enabled 
     */
    public void setEnabled(boolean enabled) {
        playerControl.setPaused(!enabled);
        /*if (enabled) {
            this.attachChild(playerModel);
        } else {
            this.detachChild(playerModel);
        }*/
    }
    
    private ParticleEmitter getDustParticleEmitter (AssetManager assetManager) {
             ParticleEmitter fire = StandardParticleEmitter.standard(assetManager);
            
    fire.setStartColor(  new ColorRGBA(.40f, 0.40f, 0.40f, 1f));   // bluish grey
    fire.setEndColor(new ColorRGBA(0.05f, 0.05f, 0.05f, 0.5f)); // grey
    fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, -1, 0));
    fire.setStartSize(3.5f);
    fire.setEndSize(0.1f);
    fire.setGravity(0, 0f, 0);
    fire.setLowLife(0.2f);
    fire.setHighLife(0.2f);
    fire.getParticleInfluencer().setVelocityVariation(0.3f);
    return fire;
    }
    
    //animation function that must be implemented even if unused
     public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    // unused
  }
    //animation function that must be implemented even if unused 
      public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
          if (EffectSettings.ambientOcclusion == AmbientOcclusion.TEXTURE) {
              return;
          }
          channel.setAnim("ArmatureAction.000");
          channel.setSpeed(1.0f);
    /*if (animName.equals("ArmatureAction")) {
      channel.setAnim("ArmatureAction", 0.50f);
      channel.setLoopMode(LoopMode.DontLoop);
      channel.setSpeed(1f);
    }*/
  }
      
      public void enableFloorOcclusion() {
          this.attachChild(this.floorOcclusion);
      }
      
      public void disableFloorOcclusion() {
          this.floorOcclusion.removeFromParent();
      }
}