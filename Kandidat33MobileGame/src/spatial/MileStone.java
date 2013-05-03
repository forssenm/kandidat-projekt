package spatial;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import util.RomanNumber;
import variables.P;

/**
 * A class for a chunk of background wall.
 * @author jonatankilhamn
 */
public class MileStone extends Node {
    private static final float LENGTH = 2f;
    
    private static Material materialForI;
    private static Material materialForV;
    private static Material materialForX;
    private static Material materialForL;
    private static Material materialForD;
    
    private static Geometry geometryForMilestone;
    
    
    
    
    /**
     * This constructor creates a <code>MileStone</code> represented by a 
     * <code>Geometry</code> loaded internaly.
     * @param assetManager is used to load the geometry and 
     * texture of the <code>MileStone</code>.
     */
    public MileStone(AssetManager assetManager, int progress){
        super("MileStone");
        
        if (materialForI == null) {
            initMaterials(assetManager);
        }
        
        String mileStoneText = RomanNumber.romanNumberString(progress);
        Geometry temp;
        float length = 0;
        for (int i = 0; i < mileStoneText.length(); i++) {
            temp = null;
            switch (mileStoneText.charAt(i)) {
                case ('I'):
                    temp = romanIBox();
                    temp.setLocalTranslation(length, 0f, 0f);
                    break;
                case ('V'):
                    temp = romanVBox();
                    temp.setLocalTranslation(length, 0f, 0f);
                    break;
                case ('X'):
                    temp = romanXBox();
                    temp.setLocalTranslation(length, 0f, 0f);
                    break;
                case ('L'):
                    temp = romanLBox();
                    temp.setLocalTranslation(length, 0f, 0f);
                    break;
                case ('C'):
                    temp = romanDBox();
                    temp.setLocalTranslation(length, 0f, 0f);
                    break;
                default:
                    break;
            }
            if (temp != null) {
                this.attachChild(temp);
                length += LENGTH*2;
            }
        }
       
 
        this.setShadowMode(ShadowMode.Off);
        
    }
    
    private static void initMaterials(AssetManager assetManager) {
        Texture textureForI = assetManager.loadTexture("Textures/RomanNumerals/RomanI2.jpg");
        Texture textureForV = assetManager.loadTexture("Textures/RomanNumerals/RomanV2.jpg");
        Texture textureForX = assetManager.loadTexture("Textures/RomanNumerals/RomanX2.jpg");
        Texture textureForL = assetManager.loadTexture("Textures/RomanNumerals/RomanL2.jpg");
        Texture textureForD = assetManager.loadTexture("Textures/RomanNumerals/RomanD2.jpg");
        
        textureForI.setWrap(Texture.WrapMode.EdgeClamp);
        textureForV.setWrap(Texture.WrapMode.EdgeClamp);
        textureForX.setWrap(Texture.WrapMode.EdgeClamp);
        textureForL.setWrap(Texture.WrapMode.EdgeClamp);
        textureForD.setWrap(Texture.WrapMode.EdgeClamp);
                
        materialForI = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        materialForI.setTexture("DiffuseMap", textureForI);
        materialForV = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        materialForV.setTexture("DiffuseMap", textureForV);
        materialForX = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        materialForX.setTexture("DiffuseMap", textureForX);
        materialForL = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        materialForL.setTexture("DiffuseMap", textureForL);
        materialForD = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        materialForD.setTexture("DiffuseMap", textureForD);
        
        Box model = new Box(new Vector3f(LENGTH,0,-P.platformWidth/2-P.playerZOffset), LENGTH, LENGTH, 0.1f);
        geometryForMilestone = new Geometry("",model);
    }
    
    private Geometry romanIBox() {
        Geometry geometry = geometryForMilestone.clone();
        geometry.setMaterial(materialForI);
        return geometry;
    }
    
    private Geometry romanVBox() {
        Geometry geometry = geometryForMilestone.clone();
        geometry.setMaterial(materialForV);
        return geometry;
    }
    
    private Geometry romanXBox() {
        Geometry geometry = geometryForMilestone.clone();
        geometry.setMaterial(materialForX);
        return geometry;
    }
    
    private Geometry romanDBox() {
        Geometry geometry = geometryForMilestone.clone();
        geometry.setMaterial(materialForD);
        return geometry;
    }
    
    private Geometry romanLBox() {
        Geometry geometry = geometryForMilestone.clone();
        geometry.setMaterial(materialForL);
        return geometry;
    }
    
}
