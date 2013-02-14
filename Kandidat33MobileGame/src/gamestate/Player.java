/*
 *
 */
package gamestate;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
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
    public Box model;
    public Material material;
    
    // Hook scenegraph
    public Node node;
    public CharacterControl characterControl;
    private Player(){}
    
    public void addToNode(Node node){
        node.attachChild(this.node);
    }
    public void addToPhysicsSpace(PhysicsSpace physicsSpace){
        physicsSpace.add(this.characterControl);
    }
    
    public static Player createPlayer(){
        Player player = new Player();
        player.node = new Node();
        
        Geometry playerGeometry = new Geometry("player", player.model);
        playerGeometry.setMaterial(player.material);       
        player.node.attachChild(playerGeometry);
        
         /**
         * Create the players CharacterControl object
         */
        CapsuleCollisionShape shape = new CapsuleCollisionShape(2f, 2f);
        player.characterControl = new CharacterControl(shape, 0.05f);
        player.characterControl.setJumpSpeed(P.jump_speed);

        /**
         * Position the player
         */
        Vector3f vt = new Vector3f(0, 10, 0);
        player.node.setLocalTranslation(vt);

        player.node.addControl(player.characterControl);
        Vector3f walkDirection = Vector3f.UNIT_X.multLocal(P.run_speed);

        player.characterControl.setWalkDirection(walkDirection);
        return new Player();
    }
    
    private CapsuleCollisionShape createCapsuleCollisionShape(){
        return null;
        
    }
}
