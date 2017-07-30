package com.alekso.tangojavaexperiments;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.Box;

/**
 * Created by alekso on 25/03/2017.
 */

public class App extends SimpleApplication {
    /**
     * Device rotation
     */
    public static float[] mRotation = new float[]{0f, 0f, 0f, 0f};
    /**
     * Device position
     */
    public static float[] mPosition = new float[]{0f, 0f, 0f};

    private Geometry mPhoneGeometry;

    private AmbientLight mSunLight;

    @Override
    public void simpleInitApp() {

        Node groundPivot = new Node("ground_pivot");
        rootNode.attachChild(groundPivot);

        Material phoneMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        phoneMat.setColor("Diffuse", ColorRGBA.White);
        phoneMat.setColor("Specular", ColorRGBA.White);
        phoneMat.setFloat("Shininess", 64f);
        phoneMat.setTexture("DiffuseMap", assetManager.loadTexture("simple_phab2pro.png"));
        Material whiteMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        whiteMat.setColor("Color", ColorRGBA.White);

        Grid grid = new Grid(10, 10, 1f);
        Geometry gridGeom = new Geometry("grid", grid);
        gridGeom.setMaterial(whiteMat);
        gridGeom.setLocalTranslation(-5f, -1f, 0f);
        groundPivot.attachChild(gridGeom);

        Box b = new Box(1, 1, 1);
        mPhoneGeometry = new Geometry("phone", b);
        mPhoneGeometry.setMaterial(phoneMat);
        mPhoneGeometry.setLocalScale(.5f, 1f, .1f);
        mPhoneGeometry.setLocalTranslation(0f, 0f, -1f);
        rootNode.attachChild(mPhoneGeometry);

        mSunLight = new AmbientLight();
        mSunLight.setColor(ColorRGBA.White);
        rootNode.addLight(mSunLight);

        DirectionalLight lamp = new DirectionalLight();
        lamp.setDirection(new Vector3f(1, 0, -2).normalizeLocal());
        lamp.setColor(ColorRGBA.Yellow);
        rootNode.addLight(lamp);
    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);

        mPhoneGeometry.setLocalTranslation(mPosition[0], mPosition[1], mPosition[2]);
        mPhoneGeometry.setLocalRotation(new Quaternion(mRotation[0], mRotation[1], mRotation[2], 1f));
    }
}
