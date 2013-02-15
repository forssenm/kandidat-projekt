/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestate;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author dagen
 */
public class Platform {
       // Scene Object
    public Spatial spatial;
    
    // Physics connection
    public RigidBodyControl rigidBodyControl;
    
    
    public Platform(SceneObjectDataSource dataSource){
        this.spatial = dataSource.getSceneObject();   
        this.rigidBodyControl = new RigidBodyControl(0.0f);
        this.spatial.addControl(this.rigidBodyControl);
    }
    
    public void addToNode(Node node){
        node.attachChild(this.spatial);
    }
    
    public void addToPhysicsSpace(PhysicsSpace physicsSpace){
        physicsSpace.add(this.rigidBodyControl);
    }
}
