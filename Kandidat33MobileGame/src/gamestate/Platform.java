/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestate;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import variables.P;

/**
 *
 * @author dagen
 */
public class Platform extends Geometry {
    
    
    /**
     * 
     */
    public Platform(AssetManager assetManager){
        super("Platform");
        Box model =
            new Box(Vector3f.ZERO, P.platformLength, P.platformHeight, P.platformWidth);
        this.mesh = model;
        
        Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        material.setTexture("DiffuseMap", assetManager.loadTexture("Textures/BrickWall.jpg"));
        this.setMaterial(material);

        RigidBodyControl rigidBodyControl = new RigidBodyControl(0.0f);
        this.addControl(rigidBodyControl);

        this.setShadowMode(ShadowMode.Receive);
    }
   
    public float getX() {
        return this.getLocalTranslation().x;
    }
    
    public Vector3f getPlatformPosition() {
        return this.getLocalTranslation();
    }
    
    public void setPlatformPosition(Vector3f position) {
        RigidBodyControl rigidBodyControl = this.getControl(RigidBodyControl.class);
        rigidBodyControl.setEnabled(false);
        this.setLocalTranslation(position);
        rigidBodyControl.setEnabled(true);
    }
    
}
