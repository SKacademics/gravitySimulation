package ch.fabian.solarSystem;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class AxisCreator {

    public Group buildAxes() {
        System.out.println("Building Axes...()");
        final PhongMaterial redMaterial = createPhongMaterial(Color.DARKRED, Color.RED);
        final PhongMaterial greenMaterial = createPhongMaterial(Color.DARKGREEN, Color.GREEN);
        final PhongMaterial blueMaterial = createPhongMaterial(Color.DARKBLUE, Color.BLUE);

        final Box xAxis = new Box(240.0, 0.1, 0.1);
        final Box yAxis = new Box(0.1, 240.0, 0.1);
        final Box zAxis = new Box(0.1, 0.1, 240.0);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        System.out.println("Building Axes finished");
        return new Group(xAxis,yAxis,zAxis);
    }

    private PhongMaterial createPhongMaterial(Color diffuse, Color specular) {
        final PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(diffuse);
        material.setSpecularColor(specular);
        return material;
    }
}
