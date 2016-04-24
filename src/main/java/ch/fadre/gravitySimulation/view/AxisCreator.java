package ch.fadre.gravitySimulation.view;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import java.util.ArrayList;
import java.util.List;

class AxisCreator {

    Group buildAxes() {
        System.out.println("Building Axes...()");
        final PhongMaterial redMaterial = createPhongMaterial(Color.DARKRED, Color.RED);
        final PhongMaterial greenMaterial = createPhongMaterial(Color.DARKGREEN, Color.GREEN);
        final PhongMaterial blueMaterial = createPhongMaterial(Color.DARKBLUE, Color.BLUE);

        final Box xAxis = new Box(250.0, 0.1, 0.1);
        final Box yAxis = new Box(0.1, 250.0, 0.1);
        final Box zAxis = new Box(0.1, 0.1, 250.0);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);


        System.out.println("Building Axes finished");
        Group group = new Group(xAxis, yAxis, zAxis);
//        group.getChildren().addAll(createGrid());
        return group;
    }

    private List<Node> createGrid() {
        List<Node> nodes = new ArrayList<>();
        for (int i = -1000; i < 1000; i+=100) {
            for (int j = -1000; j < 1000; j+=100) {
                for (int k = -1000; k < 1000; k+=100) {
                    final Box xAxis = new Box(1000, 0.1, 0.1);
                    xAxis.setTranslateX(i);
                    xAxis.setTranslateY(j);
                    xAxis.setTranslateZ(k);
                    xAxis.setMaterial(createPhongMaterial(Color.BLACK, Color.BLACK));
                    final Box yAxis = new Box(0.1, 1000, 0.1);
                    yAxis.setTranslateX(i);
                    yAxis.setTranslateY(j);
                    yAxis.setTranslateZ(k);
                    yAxis.setMaterial(createPhongMaterial(Color.BLACK, Color.BLACK));
                    final Box zAxis = new Box(0.1, 0.1, 1000);
                    zAxis.setTranslateX(i);
                    zAxis.setTranslateY(j);
                    zAxis.setTranslateZ(k);
                    zAxis.setMaterial(createPhongMaterial(Color.BLACK, Color.BLACK));
                    nodes.add(xAxis);
                    nodes.add(yAxis);
                    nodes.add(zAxis);

                }
            }
        }
        return nodes;

    }

    private PhongMaterial createPhongMaterial(Color diffuse, Color specular) {
        final PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(diffuse);
        material.setSpecularColor(specular);
        return material;
    }
}
