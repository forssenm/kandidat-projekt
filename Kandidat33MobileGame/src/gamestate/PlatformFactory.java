/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestate;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.scene.Node;

/**
 *
 * @author dagen
 */
public class PlatformFactory {
    AssetManager assetManager;
    Node platformRootNode;
    PhysicsSpace physicsSpace;
    
    public PlatformFactory(AssetManager assetManager,
            Node platformRootNode,PhysicsSpace physicsSpace){
        this.assetManager = assetManager;
        this.platformRootNode = platformRootNode;
        this.physicsSpace = physicsSpace;
    }
    
    public Platform createPlatform(){
        Platform platform = new Platform(
                new PlatformSceneObjectDataSource(this.assetManager));
        platform.addToNode(this.platformRootNode);
        platform.addToPhysicsSpace(this.physicsSpace);
        return platform;
    }
}
