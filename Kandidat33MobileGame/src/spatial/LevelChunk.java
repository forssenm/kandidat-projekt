/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spatial;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import java.util.ArrayList;

/**
 *
 * @author jonatankilhamn
 */
public class LevelChunk extends Node {
    
    private Node levelRootNode;
    private PhysicsSpace physicsSpace;
    private ArrayList<Light> lights = new ArrayList<Light>();
    
    public LevelChunk(PhysicsSpace physicsSpace, Node levelRootNode) {
        super();
        this.physicsSpace = physicsSpace;
        this.levelRootNode = levelRootNode;
    }
    
    @Override
    /**
     * @Inheritdoc
     * Physics-safe movement of chunk. Any other transformation might disrupt
     * the physics of platforms etc.
     */
    public void setLocalTranslation(Vector3f v) {
        disablePhysics();
        super.setLocalTranslation(v);
        enablePhysics();
        for (Light light : lights) {
            if (light instanceof PointLight) {
                PointLight pointLight = ((PointLight)light);
                pointLight.setPosition(v.add(pointLight.getPosition()));
            }
        }
    }
    
    @Override
    /**
     * 
     */
    public void addLight(Light light) {
        lights.add(light);
        levelRootNode.addLight(light);
    }
    
    
    /**
     * Removes this chunk from the level.
     */
    public void remove() {
        removeFromPhysicsSpace();
        for (Light light : lights) {
            levelRootNode.removeLight(light);
        }
        this.getParent().detachChild(this);
    }

    /**
     * Adds all objects with a PhysicsControl in this chunk to the PhysicsSpace.
     *
     */
    public void addToPhysicsSpace() {
        // traverse the scenegraph starting from the chunk node
        this.depthFirstTraversal(new SceneGraphVisitor() {
            public void visit(Spatial spatial) {
                physicsSpace.addAll(spatial);
            }
        });
    }

    /**
     * Removes all objects with a PhysicsControl in this chunk from the
     * PhysicsSpace.
     *
     */
    private void removeFromPhysicsSpace() {
        // traverse the scenegraph starting from the chunk node
        this.depthFirstTraversal(new SceneGraphVisitor() {
            public void visit(Spatial spatial) {
                physicsSpace.removeAll(spatial);
            }
        });
    }   

    /**
     * Disables the physics of all objects in this chunk.
     */
    private void disablePhysics() {
        // traverse the scenegraph starting from the chunk node
        this.depthFirstTraversal(new SceneGraphVisitor() {
            public void visit(Spatial spatial) {
                // get the PhysicsControl if there is any
                PhysicsControl physicsControl = spatial.getControl(PhysicsControl.class);
                if (physicsControl != null) {
                    physicsControl.setEnabled(false);
                }
            }
        });
    }

    /**
     * Enables the physics of all objects in this chunk.
     */
    private void enablePhysics() {
        // traverse the scenegraph starting from the chunk node
        this.depthFirstTraversal(new SceneGraphVisitor() {
            public void visit(Spatial spatial) {
                // get the PhysicsControl if there is any
                PhysicsControl physicsControl = spatial.getControl(PhysicsControl.class);
                if (physicsControl != null) {
                    physicsControl.setEnabled(true);
                }
            }
        });
    }

}
