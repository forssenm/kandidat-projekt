package spatial.powerup;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import control.AbstractPowerupControl;
import control.PlayerControl;
import control.PlayerInteractorControl;
import spatial.Player;
import spatial.PlayerInteractor;
import spatial.StandardParticleEmitter;
import variables.EffectSettings;

/**
 * An powerup that gives the player the ability to double jump.
 *
 * @author jonatankilhamn
 */
public class DoubleJumpPowerup extends PlayerInteractor {

    public DoubleJumpPowerup(AssetManager assetManager) {
        this.attachChild(this.createModel(assetManager));
        this.addControl(this.createControl());
    }

    @Override
    protected Spatial createModel(AssetManager assetManager) {
        Node fireball = new Node();
        fireball.setName ("fireball");
        
       
        Node model = (Node) assetManager.loadModel("Models/powerups/torus/torus002black.j3o");
        model.setName("model");
        model.move(-0.2f, 0.1f, 1.2f);
        model.scale(0.9f);
        fireball.attachChild(model);
       
        
        if (EffectSettings.particles == EffectSettings.Particles.ON) {
        
        ParticleEmitter glow = getPowerupParticleEmitter(assetManager);
        fireball.attachChild(glow);
        }
        return fireball;
    }

    private ParticleEmitter getPowerupParticleEmitter(AssetManager assetManager) {
        ParticleEmitter glow = StandardParticleEmitter.standard(assetManager);
           
        glow.setNumParticles(5);
        glow.setStartColor(ColorRGBA.Green);
        glow.setEndColor(ColorRGBA.White);
        glow.getParticleInfluencer().setInitialVelocity(Vector3f.ZERO);
        glow.setStartSize(3.5f);
        glow.setEndSize(0.1f);
        glow.setGravity(0, 0, 2);
        glow.setLowLife(1f);
        glow.setHighLife(1f);
        glow.getParticleInfluencer().setVelocityVariation(0.3f);
        return glow;
    }
    
    public void destroy() {
        this.setName("");
        if (EffectSettings.particles == EffectSettings.Particles.ON) {
        ParticleEmitter pe = (ParticleEmitter) this.getChild("Emitter");
        pe.setLowLife(0f);
        pe.setHighLife(0f);
        }
        ((Node)this.getChild("fireball")).detachChild(this.getChild("model"));
    }

    @Override
    protected PlayerInteractorControl createControl() {
        return new AbstractPowerupControl() {
            private boolean hasHit;

            public void collideWithPlayer(Player player) {
                if (!hasHit) {
                    PlayerControl pc = player.getControl(PlayerControl.class);
                    pc.doubleJumpPowerup();
                    hasHit = true;
                    DoubleJumpPowerup.this.destroy();
                }
            }

            @Override
            protected void positionUpdate(float tpf) {
                //time += tpf;
                Spatial model = ((Node)this.spatial).getChild("model");
                if (model != null) {
                    model.rotate(0.24f, 0.12f, 0.02f);
                //model.setLocalTranslation(
                  //      (float)Math.cos(time*15)*2f,
                    //    (float)Math.sin(time*18)*2f,
                      //  (float)Math.sin(2+time*21)*2f);
                }
            }
        };
    }
}
