/*
 *
 */
package gamestate;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

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
    public Box playerModel;
    public Material playerMaterial;
    public Node playerNode;
    public CharacterControl playerCharacter;
    private Player(){}
    
    public static Player createPlayer(){
        return new Player();
    }
}
