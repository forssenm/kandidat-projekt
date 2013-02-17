/*
 *
 */
package gamestate;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import variables.P;

/* Klassen är till att börja med bara 
 * en struktur för att knyta saker 
 * till en spelare. Tanken är att det 
 * ska utvecklas till att ha ett vettigt 
 * gränssnitt och eventuellt refaktoreras 
 * till några java interfaces som ärvs.
 */

/**
 *
 * @author dagen
 */
public class Player {
    // Scene Object
    public Spatial spatial;
    
    // Physics connection
    public CharacterControl characterControl;
    
    
    public Player(SceneObjectDataSource dataSource){
        this.spatial = dataSource.getSceneObject();
        
         /**
         * Create the players CharacterControl object
         */
        CapsuleCollisionShape shape = new CapsuleCollisionShape(0f, 4f);
        this.characterControl = new CharacterControl(shape, 0.05f);
        this.characterControl.setJumpSpeed(P.jump_speed);

        Vector3f walkDirection = Vector3f.UNIT_X.multLocal(P.run_speed);
        this.characterControl.setWalkDirection(walkDirection);
        
        /**
         * Position the player
         */
        Vector3f vt = new Vector3f(0, 5, 0);
        this.spatial.setLocalTranslation(vt);
        this.spatial.addControl(this.characterControl);
        
    }
    
    public void addToNode(Node node){
        node.attachChild(this.spatial);
    }
    
    public void addToPhysicsSpace(PhysicsSpace physicsSpace){
        physicsSpace.add(this.characterControl);
    }
    
    public void jump(){
        this.characterControl.jump();
    }
}
