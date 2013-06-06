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
import light.MultiColoredLight;
import spatial.MileStone;
import spatial.Plant;
import spatial.Platform;
import spatial.Platform.PType;
import spatial.PlayerInteractor;
import spatial.Torch;
import spatial.Wall;
import spatial.WindowFrame;
import spatial.hazard.BurstWizard;
import spatial.hazard.CalculatingWizard;
import spatial.hazard.LinearBat;
import spatial.PlayerInteractor;
import spatial.WindowFrame.Design;
import spatial.hazard.LinearBat;
import spatial.hazard.LinearFireball;
import spatial.hazard.SingleShotWizard;
import spatial.hazard.SpinningFireball;
import spatial.hazard.StationaryFireball;
import spatial.powerup.DoubleJumpPowerup;
import spatial.powerup.InvulnerabilityPowerup;
import spatial.powerup.SlowDownPowerup;
import spatial.powerup.SpeedPowerup;
import variables.EffectSettings;
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
    
    private float getPlatformLength(int i) {
        switch (i) {
            case (1):
                return P.shortPlatformLength;
            case (2):
                return P.mediumPlatformLength;
            case (3):
                return P.longPlatformLength;
        }
        return 0;
    }

    /**
     * Generates a new chunk of the level. The generated content is delivered in
     * a list of
     * <code>Object</code>s. All such objects are either
     * <code>Spatial</code>s or
     * <code>Light</code>s. One of the spatials must have the name "background".
     *
     * @return A list of <code>Spatial</code>s and <code>Light</code>s.
     *
     */
    public List<Object> generateChunk(int level) {
        if (level == 1) {
            this.reset();
        }

        Random random = new Random();

        LinkedList<Spatial> spatials = new LinkedList<Spatial>();
        LinkedList<Light> lights = new LinkedList<Light>();

        // Generate everything with a physical / game mechanical connection:
        
        

        // standard length and distance
        float totalLength = P.chunkLength;
        float dist = P.platformDistance;
        float d = distanceOverFlow;
        distanceOverFlow = 0;

        float windowHeight = Math.max(0, 5 * Math.round(height / 5));

        int platformLayoutType;
        int enemyType;
        int powerupType;
        int decorationType;
        WindowFrame.Design windowDesign = null;
        
        if (level < P.noOfStartingChunks) { //nothing special on the first few chunks
            platformLayoutType = -1;
            enemyType = 1;//-1;
            powerupType = -1;
            decorationType = 4;
        } else {
            /*results 0-7 are actual enemies; a 'roll' of 8 or higher
             * will give nothing */
            enemyType = random.nextInt(12);
            /* results 0-3 are powerups; 4 or higher gives nothing */
            powerupType = random.nextInt(8);
            
            decorationType = random.nextInt(7);

            // get back to normal height if we're too low or too high
            if (height < -2) {
                platformLayoutType = 2;
            } else if (height > 5) {
                platformLayoutType = 3;
            } else {
                platformLayoutType = random.nextInt(6);
            }
            
            if (decorationType >= 0 && decorationType <=2) {
                windowDesign = random.nextBoolean() ? WindowFrame.Design.BIRD : WindowFrame.Design.FLOWERS;
            }

        }


        PType pType;
        float nDist;

        switch (platformLayoutType) {
            case (-1): // starting platform
                while (d < totalLength) {
                    spatials.add(createPlatform(d, height, PType.LONG, windowDesign));
                    d += PType.LONG.length+4;
                }
                if (level == P.noOfStartingChunks-1) {
                    d += dist;
                }
                break;
            case (0): // standard platforms
                while (d < totalLength) {
                    spatials.add(createPlatform(d, height + random.nextFloat() * 3, PType.MEDIUM, windowDesign));
                    d += PType.MEDIUM.length + dist;
                }
                break;
            case (1): // differentlength platforms
                while (d < totalLength) {
                    if (random.nextBoolean()) {
                        pType = PType.SHORT;
                    } else {
                        pType = PType.MEDIUM;
                    }
                    nDist = dist * (1f + random.nextFloat());

                    spatials.add(createPlatform(d, height + random.nextFloat() * 3, pType, windowDesign));
                    d += pType.length + nDist;
                }
                break;
            case (2): // climbing platforms
                while (d < totalLength) {
                    if (random.nextBoolean()) {
                        pType = PType.SHORT;
                    } else {
                        pType = PType.MEDIUM;
                    }
                    nDist = dist * (0.6f + 0.5f * random.nextFloat());
                    height += 1 + 4 * random.nextFloat();
                    spatials.add(createPlatform(d, height + random.nextFloat() * 2, pType, windowDesign));
                    d += pType.length + nDist;
                }
                windowHeight = Math.max(0, 5 * Math.round(height / 5));
                break;
            case (3): // descending platforms
                float descent;
                while (d < totalLength) {
                    if (random.nextBoolean()) {
                        pType = PType.SHORT;
                    } else {
                        pType = PType.MEDIUM;
                    }
                    nDist = dist * (1f + random.nextFloat());
                    descent = 2 + 8 * random.nextFloat();
                    if (height - descent > P.deathTreshold + 2) {
                        height -= descent;
                    }
                    spatials.add(createPlatform(d, height + random.nextFloat() * 4, pType, windowDesign));
                    d += pType.length + nDist;
                }
                //powerupType = -1;
                break;
            case (4): // invulnerability only reachable with double-jump
                height = -1;
                spatials.add(createPlatform(d, height, PType.LONG, windowDesign));
                d += PType.LONG.length + dist;
                // one high, one low
                float nHeight = 5 + 3 * random.nextFloat();
                spatials.add(createPlatform(d, height + nHeight, PType.SHORT, windowDesign));
                spatials.add(createPlatform(d, height + nHeight - 13, PType.SHORT, windowDesign));
                height += nHeight;
                d += PType.SHORT.length + dist;
                spatials.add(createInvulnerabilityPowerup(d + 5, height - 6));
                // one higher
                height += 2 + 5 * random.nextFloat();
                spatials.add(createPlatform(d, height, PType.SHORT, windowDesign));
                d += PType.SHORT.length + 5;
                windowHeight = Math.max(0, 5 * Math.round(height / 5));
                height = -1;
                if (d < totalLength) {
                    spatials.add(createPlatform(d, height, PType.MEDIUM, windowDesign));
                    d += PType.MEDIUM.length + dist;
                }
                powerupType = -1;
                break;
            case (5): // long platform with short platforms above
                float d2 = d + PType.LONG.length/2; // start higher platforms a bit in
                while (d < totalLength) {
                    spatials.add(createPlatform(d, height, PType.LONG, windowDesign));
                    d += PType.LONG.length;
                }
                d += dist;
                height += 15;
                windowHeight = height;
                while (d2 < totalLength) {
                    spatials.add(createPlatform(d2, height,PType.SHORT, windowDesign));
                    spatials.add(createLinearBat(d2 + 40,height - 6));
                    d2 += PType.SHORT.length + 2*dist;
                    height += 1 + random.nextFloat();
                }
                height -= 20;
                enemyType = -1;
            default:
                break;
        }

        // fill up with platforms if whatever was in the switch statement didn't already
        while (d < totalLength) {
            spatials.add(createPlatform(d, height, PType.SHORT, windowDesign));
            height += random.nextInt(9) - 4;
            d += PType.SHORT.length + dist;
        }
        // record this number so that the next chunk doesn't overlap
        distanceOverFlow = d - totalLength;

        // generate enemies:
        SpotLight spotlight;
        switch (enemyType) {
            case (-1):
                // no enemies;
                break;
            case (0):
            case (1):
                // single shot wizard
                float wizardPosX = spatials.getLast().getLocalTranslation().getX()
                        + 15;
                float wizardPosY = spatials.getLast().getLocalTranslation().getY()
                        + 18;
                spotlight = new SpotLight();
                if (P.useWizardLights && EffectSettings.light == EffectSettings.Light.STANDARD_LIGHTING) {
                    lights.add(spotlight);
                }
                spatials.add(createWizard(wizardPosX, wizardPosY, spotlight));
                break;
            case (2):
                // burst wizard
                wizardPosX = spatials.getLast().getLocalTranslation().getX()
                        + 15;
                wizardPosY = spatials.getLast().getLocalTranslation().getY()
                        + 18;
                spotlight = new SpotLight();
                if (P.useWizardLights && EffectSettings.light == EffectSettings.Light.STANDARD_LIGHTING) {
                    lights.add(spotlight);
                }
                spatials.add(createBurstWizard(wizardPosX, wizardPosY, spotlight));
                break;
            case (3):
            case (4):
                // single bat
                spatials.add(createLinearBat(d, height + 4 + random.nextFloat() * 2));
                break;
            case (5):
            case (6):
                // three bats
                int temp1 = (random.nextInt(5) - 5) * 10; // 1st bat distance
                spatials.add(createLinearBat(d + temp1, height + 7));

                int temp2 = temp1;
                while (temp2 == temp1) {
                    temp2 = (random.nextInt(5) - 5) * 10; // 2nd bat distance
                }
                spatials.add(createLinearBat(d + temp2, height + 13));
                temp1 = temp2;
                while (temp2 == temp1) {
                    temp1 = (random.nextInt(5) - 5) * 10; // 3rd bat distance
                }
                spatials.add(createLinearBat(d + temp1, height + 19));
                break;
            case (7):
                // a wizard in the foreground shooting fireballs at where the player's going
                spotlight = new SpotLight();
                if (P.useWizardLights && EffectSettings.light == EffectSettings.Light.STANDARD_LIGHTING) {
                    lights.add(spotlight);
                }
                spatials.add(createCalculatingWizard(d, height, spotlight));
            default:
                // no enemies
                break;
        }


        // generate powerups:
        switch (powerupType) {
            case (-1): // nothing
                break;
            case (0): // speed boost
                spatials.add(createSpeedPowerup(d - 8, height + 5));
                break;
            case (1): // double jump
            case (2):
                spatials.add(createDoubleJumpPowerup(d - 3, height + 10));
                break;
            case (3):
                spatials.add(createSlowDownPowerup(d - 15, height + 7));
                break;
            case (4):
                spatials.add(createInvulnerabilityPowerup(d - 10, height + 6));
            default:
                break;
        }
        
        //test amount of lights that can be used in a scene
        /*
        double useLight = random.nextDouble();
        if (useLight <= 0.25) {
            lights.add(this.createTorchLight(15f, windowHeight + 15f));
            //lights.add(this.createWindowLight(30f, windowHeight + 23f));
        }*/
        
        // Generate the background:
        Node staticObjects = new Node("background");

        // the wall:
        Wall wall = new Wall(this.assetManager);
        staticObjects.attachChild(wall);

        // the decorations:
        switch (decorationType) {
            case (0): // window
            case (1):
            case (2):
                //WindowFrame.Design windowDesign = random.nextBoolean() ? WindowFrame.Design.BIRD : WindowFrame.Design.FLOWERS;
                WindowFrame window = createWindowFrame(30f, windowHeight + 23f, windowDesign);
                staticObjects.attachChild(window);
                if ((EffectSettings.light == EffectSettings.Light.STANDARD_LIGHTING && P.useWindowLights) || EffectSettings.light == EffectSettings.Light.TEXTURES_AND_WINDOW || EffectSettings.light == EffectSettings.Light.TEXTURES_SMALL_LIGHTS) {
                    lights.add(this.createWindowLight(30f, windowHeight + 23f, windowDesign));
                }
                break;
            case (3): // torch
            case (4):
            case (5):
                Torch torch = createTorch(15, windowHeight + 15);
                staticObjects.attachChild(torch);
                if ((EffectSettings.light == EffectSettings.Light.STANDARD_LIGHTING && P.useTorchLights) || EffectSettings.light == EffectSettings.Light.TEXTURES_SMALL_LIGHTS) {
                    lights.add(this.createTorchLight(15f, windowHeight + 15f));
                }
                break;
            default:
                break;
        }
        float plantProbability = random.nextFloat();
        if (plantProbability < 0.6f) {
            float plantX = 0f;
            float plantY = 0f;
            for (Spatial s : spatials) {
                if (s instanceof Platform) {
                    Platform p = (Platform) s;
                    plantX = p.getLocalTranslation().getX() + p.getType().length/2+0.2f;
                    plantY = p.getLocalTranslation().getY() + 15;
                }
            }
            Plant.Type plantType = plantProbability < 0.45 ? Plant.Type.LEAVES : Plant.Type.FLOWERS;
            Plant plant = createPlant(plantX, plantY, plantType);
            staticObjects.attachChild(plant);
        }

        if (level > 4 && level % 3 == 2) {
            staticObjects.attachChild(createMileStone((int) (level - 4) / 3 + 1, 10f, windowHeight + 20f));
        }
        
        spatials.add(staticObjects);



        LinkedList<Object> list = new LinkedList<Object>();
        list.addAll(spatials);
        list.addAll(lights);

        
        return list;

    }

    public void reset() {
        height = 0;
        distanceOverFlow = 0;

    }

    /**
     * Creates a platform at a given 2d position.
     */
    private Platform createPlatform(float positionX, float positionY, PType type, Design design) {
        Vector3f platformPos = new Vector3f(positionX, positionY, 0f);
        return new Platform(this.assetManager, platformPos, type, design);
    }

    /* Creates a windowframe on the wall at a given position */
    private WindowFrame createWindowFrame(float positionX, float positionY, WindowFrame.Design design) {
        Vector3f windowPos = new Vector3f(positionX, positionY, 0f);
        WindowFrame window = new WindowFrame(this.assetManager, windowPos, design);
        return window;
    }

    /* Creates a torch on the wall at a given psition */
    private Torch createTorch(float positionX, float positionY) {
        Vector3f windowPos = new Vector3f(positionX, positionY, 0f); //beware the Z is  not used
        Torch window = new Torch(this.assetManager, windowPos);
        return window;
    }
    
    private Plant createPlant (float positionX, float positionY, Plant.Type plantType) {
        Vector3f windowPos = new Vector3f(positionX, positionY, 0f); //beware the Z is  not used
        Plant window = new Plant(this.assetManager, windowPos, plantType);
        return window;
    }
    


    /* Creates a spotlight shining through a window at a given position */
    private Light createWindowLight(float positionX, float positionY, Design design) {
        //PointLight light = new PointLight();
        PointLight light = null;
        Vector3f pos = new Vector3f(positionX, positionY-5, -P.platformWidth / 2 + 0.2f);
        if (EffectSettings.light == EffectSettings.Light.STANDARD_LIGHTING) {
            light = new PointLight();
            light.setRadius(75);
            light.setColor(new ColorRGBA(99 / 255f, 184 / 255f, 1f, 0f));
        } else {
            light = new MultiColoredLight(assetManager, design);
            light.setColor(ColorRGBA.White);
            //light.setRadius(30);
            light.setRadius(39f/2f+4f);
            pos.add(0,-7,0);
            light.setColor(ColorRGBA.White);
        }
        light.setPosition(pos);
        return light;
        /*
        if(EffectSettings.light == EffectSettings.Light.STANDARD_LIGHTING) {
            SpotLight windowLight = new SpotLight();
            windowLight.setColor(new ColorRGBA(99 / 255f, 184 / 255f, 1f, 0f));
            windowLight.setSpotOuterAngle(45f * FastMath.DEG_TO_RAD);
            windowLight.setSpotInnerAngle(12f * FastMath.DEG_TO_RAD);
            Vector3f windowPosition = new Vector3f(positionX, positionY + 8f, -P.platformWidth / 2 + 0.2f);
            windowLight.setPosition(windowPosition);
            windowLight.setDirection(P.windowLightDirection);
            windowLight.setSpotRange(100f);
            return windowLight;
        } else {
            PointLight light = new PointLight();
            light.setRadius(75);
            light.setPosition(new Vector3f(positionX, positionY-5, -P.platformWidth / 2 + 0.2f));
            light.setColor(new ColorRGBA(99 / 255f, 184 / 255f, 1f, 0f));
            return light;
        }*/
    }

    /* Creates a pointlight, to use with a torch */
    private Light createTorchLight(float positionX, float positionY) {     
        PointLight light = new PointLight();
        light.setPosition(new Vector3f(positionX, positionY, -P.platformWidth / 2 + 0.2f));
        //light.setColor(new ColorRGBA(1f, 0.3f, 0f, 0f));
        light.setColor(ColorRGBA.Orange);
        if (EffectSettings.light == EffectSettings.Light.STANDARD_LIGHTING) {
            light.setRadius(50);
        } else {
            light.setRadius(10);
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
    private PlayerInteractor createLinearBat(float positionX, float positionY) {
        PlayerInteractor hazard = new LinearBat(assetManager, new Vector3f(-15, 0, 0));
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
    private PlayerInteractor createWizard(float positionX, float positionY, SpotLight spotlight) {
        PlayerInteractor wizard = new SingleShotWizard(assetManager, spotlight);
        wizard.move(positionX, positionY, 0f);
        return wizard;
    }

    /* Creates a wizard shooting multiple fireballs at the player.*/
    private PlayerInteractor createBurstWizard(float positionX, float positionY, SpotLight spotlight) {
        PlayerInteractor wizard = new BurstWizard(assetManager, spotlight);
        wizard.move(positionX, positionY, 0f);
        return wizard;
    }

    /* Creates a wizard in the foreground, shooting fireballs ahead of the player.*/
    private PlayerInteractor createCalculatingWizard(float positionX, float positionY, SpotLight spotlight) {
        PlayerInteractor wizard = new CalculatingWizard(assetManager, spotlight);
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

    private Spatial createMileStone(int progress, float positionX, float positionY) {
        MileStone mileStone = new MileStone(assetManager, progress);
        mileStone.setLocalTranslation(positionX, positionY, 0f);
        return mileStone;
    }
}
