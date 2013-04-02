package leveldata;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import control.FireballControl;
import java.util.Random;
import spatial.Hazard;
import spatial.LevelChunk;
import spatial.Platform;
import spatial.Wall;
import spatial.WindowFrame;
import variables.P;

/**
 *
 * @author jonatankilhamn
 */
public class ChunkFactory {

    private AssetManager assetManager;

    public ChunkFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * Fills a chunk with content
     * @param chunk 
     */
    public void fillChunk(LevelChunk chunk) {
        
        // Generate everything with a physical / game mechanical connection:
        
        // generate platform positions
        Random random = new Random();
        int rand1 = random.nextInt(6) - 3;
        int rand2 = rand1 + random.nextInt(6) - 3;

        // generate two platforms
        Platform platform1 = createPlatform(0f, rand1);
        Platform platform2 = createPlatform(P.platformLength + P.platformDistance, rand2);
        chunk.attachChild(platform1);
        chunk.attachChild(platform2);
        
        // create a fireball hazard:
        /*
         * This code creates a FireballControl-type hazard floating in mid-air,
         * triggering the first time the player bumps into it. Doesn't work on
         * the phone.
         chunk.attachChild(createHazard());
         */
        
        
        // Generate the background:

        // the wall:
        Wall wall = new Wall(this.assetManager);
        chunk.attachChild(wall);

        // the decorations:
        WindowFrame window = createWindowFrame(5f, 5f);
        chunk.attachChild(window);

        // the lights:
        /*
         * This code creates a spotlight for each window. Slow on the phone.
         // a light shining out the window:
         chunk.addLight(createWindowLight(5f,5f));
         */

        /*
         * This code creates a light of random colour. Slow on the phone.
         // generate a point light source of a random colour
         chunk.addLight(createColouredLight());
         */

    }

    /* Creates a platform at a given 2d position */
    private Platform createPlatform(float positionX, float positionY) {
        Vector3f platformPos = new Vector3f(positionX, positionY, 0f);
        Platform platform = new Platform(this.assetManager, platformPos, P.platformLength, P.platformHeight, P.platformWidth);
        return platform;
    }

    /* Creates a windowframe on the wall at a given position */
    private WindowFrame createWindowFrame(float positionX, float positionY) {
        Vector3f windowPos = new Vector3f(positionX, positionY, 0f);
        WindowFrame window = new WindowFrame(this.assetManager, windowPos);
        return window;
    }

    /* Creates a spotlight shining through a window at a given position */
    private Light createWindowLight(float positionX, float positionY) {
        SpotLight windowLight = new SpotLight();
        windowLight.setSpotOuterAngle(15f * FastMath.DEG_TO_RAD);
        windowLight.setSpotInnerAngle(13f * FastMath.DEG_TO_RAD);
        Vector3f windowPosition = new Vector3f(positionX, positionY, 0f);
        windowLight.setPosition(windowPosition.subtract(P.windowLightDirection));
        windowLight.setDirection(P.windowLightDirection);
        windowLight.setSpotRange(100f);
        return windowLight;
    }

    /* Creates a light of a random colour, a bit above and in front of the player */
    private Light createColouredLight() {
        Random random = new Random();
        int rand = random.nextInt(6);
        PointLight light = new PointLight();
        light.setRadius(40);
        light.setPosition(new Vector3f(15f, 10f, 0f));
        if (rand < 3) {
            light.setColor(ColorRGBA.Blue);
        } else if (rand < 5) {
            light.setColor(ColorRGBA.Red);
        } else {
            light.setColor(ColorRGBA.Green);
        }
        return light;
    }

    /* Creates a fireball hazard floating in the air.*/
    private Hazard createHazard() {
        Hazard hazard = new Hazard(assetManager);
        hazard.setLocalTranslation(10f, 3f, 0f);
        GhostControl hazardGhostControl = new GhostControl(new BoxCollisionShape(new Vector3f(1, 1, 1)));
        hazard.addControl(hazardGhostControl);
        FireballControl fireballControl = new FireballControl();
        hazard.addControl(fireballControl);
        return hazard;
    }
}
