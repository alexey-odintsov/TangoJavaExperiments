package com.alekso.tangojavaexperiments;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.Box;

/**
 * Created by alekso on 25/03/2017.
 */

public class App extends SimpleApplication {
    @Override
    public void simpleInitApp() {

        Node groundPivot = new Node("ground_pivot");
        rootNode.attachChild(groundPivot);

        Material blueMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blueMat.setColor("Color", ColorRGBA.Blue);

        Material whiteMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        whiteMat.setColor("Color", ColorRGBA.White);

        Grid grid = new Grid(10, 10, 1f);
        Geometry gridGeom = new Geometry("grid", grid);
        gridGeom.setMaterial(whiteMat);
        gridGeom.setLocalTranslation(-5f, -1f, 0f);
        groundPivot.attachChild(gridGeom);

        Box b = new Box(1, 1, 1);
        Geometry boxGeom = new Geometry("box", b);
        boxGeom.setMaterial(blueMat);
        boxGeom.setLocalTranslation(0f, 0f, -1f);
        rootNode.attachChild(boxGeom);

    }
}
