package leveldata;

import com.jme3.asset.AssetManager;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import spatial.Platform;
import spatial.Torch;
import spatial.Wall;
import spatial.WindowFrame;
import spatial.hazard.BurstWizard;
import spatial.hazard.CalculatingWizard;
import spatial.PlayerInteractor;
import spatial.hazard.LinearFireball;
import spatial.hazard.SingleShotWizard;
import spatial.hazard.SpinningFireball;
import spatial.hazard.StationaryFireball;
import spatial.powerup.DoubleJumpPowerup;
import spatial.powerup.InvulnerabilityPowerup;
import spatial.powerup.SlowDownPowerup;
import spatial.powerup.SpeedPowerup;
import util.RomanNumber;
import variables.P;

/**
 * A class for generating chunks of the level. An instance of this class can
 * take create a chunk with platforms, windows, enemies etc. Keeps some internal
 * track of height and distance so that it doesn't place the enxt chunk out of
 * reach from the previous one.
 *
 * This is the de facto level generator – the code for where each platform etc
 * is placed can be found here.
 *
 * @author jonatankilhamn
 */
public class ChunkFactory {

    private AssetManager assetManager;
    private float height;
    private float distanceOverFlow;

    public ChunkFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * Generates a new chunk of the level. The generated content is delivered in
     * a list of
     * <code>Spatial</code>s. One of the spatials must have the name
     * "background".
     *
     * @return A list of <code>Spatial</code>s.
     *
     */
    public List<Spatial> generateChunk(int level) {
        
        if (level == 1) {
            this.reset();
        }


        LinkedList<Spatial> list = new LinkedList<Spatial>();

        // Generate the background:
        Node staticObjects = new Node("background");

        // the wall:
        Wall wall = new Wall(this.assetManager);
        staticObjects.attachChild(wall);

        // the decorations:
        float wHeight = Math.max(0, 5 * Math.round(height / 5));

        WindowFrame window = createWindowFrame(5f, wHeight + 23f);
        Torch torch = createTorch (30, wHeight+ 15);
        
        staticObjects.attachChild(window);
        staticObjects.attachChild(torch);
        list.add(staticObjects);


        // Generate everything with a physical / game mechanical connection:

        // standard length and distance
        float totalLength = P.chunkLength;
        float dist = P.platformDistance;
        float length = P.platformLength;
        float d = distanceOverFlow;
        distanceOverFlow = 0;

        Random random = new Random();

        int platformLayoutType;
        int enemyType;
        int powerupType;
        if (level < 5) { //nothing special on the first few chunks
            platformLayoutType = -1;
            enemyType = -1;
            powerupType = -1;
            if (level == 4) {
                d += dist; // distance after the "starting strip" is over
            }
        } else {
            /*results 0-7 are actual enemies; a 'roll' of 8 or higher
             * will give nothing */
            enemyType = random.nextInt(12);
            /* results 0-3 are powerups; 4 or higher gives nothing */
            powerupType = random.nextInt(8);

            // get back to normal height if we're too low or too high
            if (height < -2) {
                platformLayoutType = 2;
            } else if (height > 5) {
                platformLayoutType = 3;
            } else {
                platformLayoutType = random.nextInt(4);
            }
        }
        if (level > 7 && level%3 == 0) {
            //list.add(createMilestone((level-4)/3);
            System.out.println(RomanNumber.romanNumberString((int)(level-6)/3));
        }


        float nLength;
        float nDist;

        switch (platformLayoutType) {
            case (-1): // one long platform (boring)
                list.add(createPlatform(0, height, P.chunkLength));
                d += P.chunkLength;
                break;
            case (0): // chilling platforms
                while (d < totalLength) {
                    list.add(createPlatform(d, height + random.nextFloat() * 3, length));
                    d += length + dist;
                }
                break;
            case (1): // chilling differentlength platforms
                while (d < totalLength) {
                    nLength = length * (0.8f + random.nextFloat());
                    nDist = dist * (1f + random.nextFloat());

                    list.add(createPlatform(d, height + random.nextFloat() * 3, nLength));
                    d += nLength + nDist;
                }
                break;
            case (2): // climbing platforms
                while (d < totalLength) {
                    nLength = length * (0.5f + random.nextFloat() / 2);
                    nDist = dist * (1f + random.nextFloat());
                    height += 1 + 4 * random.nextFloat();
                    list.add(createPlatform(d, height + random.nextFloat() * 2, nLength));
                    d += nLength + nDist;
                }
                break;
            case (3): // descending platforms
                float descent;
                while (d < totalLength) {
                    nLength = length * (0.5f + random.nextFloat() / 2);
                    nDist = dist * (1f + random.nextFloat());
                    descent = 2 + 8 * random.nextFloat();
                    if (height - descent > P.deathTreshold + 2) {
                        height -= descent;
                    }
                    list.add(createPlatform(d, height + random.nextFloat() * 4, nLength));
                    d += nLength + nDist;
                }
                break;
            default:
                break;
        }

        // fill up with platforms if whatever was in the switch statement didn't already
        while (d < totalLength) {
            list.add(createPlatform(d, height, length));
            height += random.nextInt(9) - 4;
            d += length + dist;
        }
        // record this number so that the next chunk doesn't overlap
        distanceOverFlow = d - totalLength;

        // generate enemies:
        switch (enemyType) {
            case (-1):
                // no enemies;
                break;
            case (0):
            case (1):
                // single shot wizard
                float wizardPosX = list.getLast().getLocalTranslation().getX()
                        + random.nextFloat() * length;
                float wizardPosY = list.getLast().getLocalTranslation().getY()
                        + 18;
                list.add(createWizard(wizardPosX, wizardPosY));
                break;
            case (2):
                // burst wizard
                wizardPosX = list.getLast().getLocalTranslation().getX()
                        + random.nextFloat() * length;
                wizardPosY = list.getLast().getLocalTranslation().getY()
                        + 18;
                list.add(createBurstWizard(wizardPosX, wizardPosY));
                break;
            case (3):
            case (4):
                // single fireball
                list.add(createLinearFireball(d, height + 1 + random.nextFloat() * 2));
                break;
            case (5):
            case (6):
                // three fireballs
                int temp1 = (random.nextInt(5)-1) * 10; // 1st fireball distance
                list.add(createLinearFireball(d + temp1, height + 2));

                int temp2 = temp1;
                while (temp2 == temp1) {
                    temp2 = (random.nextInt(5)-1) * 10; // 2nd fireball distance
                }
                list.add(createLinearFireball(d + temp2, height + 7));
                temp1 = temp2;
                while (temp2 == temp1) {
                    temp1 = (random.nextInt(5)-1) * 10; // 3rd fireball distance
                }
                list.add(createLinearFireball(d + temp1, height + 12));
                break;
            case (7):
                // a wizard in the foreground shooting fireballs at where the player's going
                list.add(createCalculatingWizard(d, height));
            default:
                // no enemies
                break;
        }
        
        
        // generate powerups:
        switch (powerupType) {
            case (-1): // nothing
                break;
            case (0): // speed boost
                list.add(createSpeedPowerup(d-8,height+5));
                break;
            case (1): // double jump
            case (2):
                list.add(createDoubleJumpPowerup(d-3,height+10));
                break;
            case (3):
                list.add(createSlowDownPowerup(d-15, height + 7));
                break;
            case (4):
                list.add(createInvulnerabilityPowerup(d-10, height + 6));
            default:
                break;
        }


        // the lights:
        /*
         * This code creates a spotlight for each window. Slow on the phone.
         // a light shining out the window:
         staticObjects.addLight(createWindowLight(5f,5f));
         */

        /*
         * This code creates a light of random colour. Slow on the phone.
         // generate a point light source of a random colour
         staticObjects.addLight(createColouredLight());
         */

        return list;

    }

    public void reset() {
        height = 0;
        distanceOverFlow = 0;

    }

    /* Creates a platform at a given 2d position */
    private Platform createPlatform(float positionX, float positionY, float length) {
        Vector3f platformPos = new Vector3f(positionX, positionY, 0f);
        Platform platform = new Platform(this.assetManager, platformPos, length, P.platformHeight, P.platformWidth);
        return platform;
    }

    /* Creates a windowframe on the wall at a given position */
    private WindowFrame createWindowFrame(float positionX, float positionY) {
        Vector3f windowPos = new Vector3f(positionX, positionY, 0f);
        WindowFrame window = new WindowFrame(this.assetManager, windowPos);
        return window;
    }
    
    /* Creates a torch on the wall at a given psition */
    private Torch createTorch (float positionX, float positionY) {
        Vector3f windowPos = new Vector3f(positionX, positionY, 0f); //beware the Z is  not used
        Torch window = new Torch(this.assetManager, windowPos);
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
    private PlayerInteractor createHoveringFireball() {
        PlayerInteractor hazard = new StationaryFireball(assetManager);
        hazard.move(10f, 15f, 0f);
        return hazard;
    }

    /* Creates a fireball hazard flying in a straight line.*/
    private PlayerInteractor createLinearFireball(float positionX, float positionY) {
        PlayerInteractor hazard = new LinearFireball(assetManager, new Vector3f(-15, 0, 0));
        hazard.move(positionX + P.chunkLength, positionY, 0f);
        return hazard;
    }

    /* Creates a fireball hazard flying in a counter-clockwise circle.*/
    private PlayerInteractor createSpinningFireball() {
        PlayerInteractor hazard = new SpinningFireball(assetManager);
        hazard.move(5f, 6f, 0f);
        return hazard;
    }

    /* Creates a wizard shooting fireballs at the player.*/
    private PlayerInteractor createWizard(float positionX, float positionY) {
        PlayerInteractor wizard = new SingleShotWizard(assetManager);
        wizard.move(positionX, positionY, 0f);
        return wizard;
    }

    /* Creates a wizard shooting multiple fireballs at the player.*/
    private PlayerInteractor createBurstWizard(float positionX, float positionY) {
        PlayerInteractor wizard = new BurstWizard(assetManager);
        wizard.move(positionX, positionY, 0f);
        return wizard;
    }

    /* Creates a wizard in the foreground, shooting fireballs ahead of the player.*/
    private PlayerInteractor createCalculatingWizard(float positionX, float positionY) {
        PlayerInteractor wizard = new CalculatingWizard(assetManager);
        wizard.move(positionX, positionY, 15f);
        return wizard;
    }

    private PlayerInteractor createSpeedPowerup(float positionX, float positionY) {
        SpeedPowerup speedPowerup = new SpeedPowerup(assetManager);
        speedPowerup.move(positionX, positionY, 0f);
        return speedPowerup;
    }
    
    private PlayerInteractor createDoubleJumpPowerup(float positionX, float positionY) {
        DoubleJumpPowerup doubleJumpPowerup = new DoubleJumpPowerup(assetManager);
        doubleJumpPowerup.move(positionX, positionY, 0f);
        return doubleJumpPowerup;
    }

    private Spatial createSlowDownPowerup(float positionX, float positionY) {
        SlowDownPowerup slowDownPowerup = new SlowDownPowerup(assetManager);
        slowDownPowerup.move(positionX, positionY, 0f);
        return slowDownPowerup;
    }
    
    private Spatial createInvulnerabilityPowerup(float positionX, float positionY) {
        InvulnerabilityPowerup invulnerabilityPowerup =
                new InvulnerabilityPowerup(assetManager);
        invulnerabilityPowerup.move(positionX, positionY, 0f);
        return invulnerabilityPowerup;
    }
}
