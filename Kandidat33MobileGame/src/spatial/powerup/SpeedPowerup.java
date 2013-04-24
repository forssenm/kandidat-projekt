package spatial.powerup;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import control.AbstractPlayerInteractorControl;
import control.AbstractPowerupControl;
import control.PlayerControl;
import control.PlayerInteractorControl;
import spatial.Player;
import spatial.PlayerInteractor;
import spatial.StandardParticleEmitter;

/**
 * An powerup that gives the player a boost to run and jump speed.
 *
 * @author jonatankilhamn
 */
public class SpeedPowerup extends PlayerInteractor {

    public SpeedPowerup(AssetManager assetManager) {
        this.attachChild(this.createModel(assetManager));
        this.addControl(this.createControl());
    }

    @Override
    protected Spatial createModel(AssetManager assetManager) {
        Node fireball = new Node();
        
        Node ico = (Node)assetManager.loadModel("Models/icosphere/ico001.j3o");
        ParticleEmitter glow = getPowerupParticleEmitter(assetManager);
        fireball.attachChild(glow);
        fireball.attachChild(ico);

        return fireball;
    }

    private ParticleEmitter getPowerupParticleEmitter(AssetManager assetManager) {
        ParticleEmitter glow = StandardParticleEmitter.make(assetManager);
                
        glow.setStartColor(ColorRGBA.Cyan);
        glow.setEndColor(ColorRGBA.White);
        glow.getParticleInfluencer().setInitialVelocity(Vector3f.ZERO);
        glow.setStartSize(3.5f);
        glow.setEndSize(0.1f);
        glow.setGravity(0, -20, 0);
        glow.setLowLife(0.4f);
        glow.setHighLife(1f);
        glow.getParticleInfluencer().setVelocityVariation(0.3f);
        return glow;
    }
    
    public void destroy() {
        this.setName("");
        ParticleEmitter pe = (ParticleEmitter) this.getChild("Emitter");
        pe.setLowLife(0f);
        pe.setHighLife(0f);
    }

    @Override
    protected PlayerInteractorControl createControl() {
        return new AbstractPowerupControl() {
            private boolean hasHit;

            public void collideWithPlayer(Player player) {
                if (!hasHit) {
                    PlayerControl pc = player.getControl(PlayerControl.class);
                    pc.speedBoostPowerup();
                    hasHit = true;
                    SpeedPowerup.this.destroy();
                }
            }            

        };
    }
}
