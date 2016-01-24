package ch.fabian.solarSystem;


import ch.fabian.solarSystem.model.ModelSimulation;
import ch.fabian.solarSystem.model.SpaceObject;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewSimulation {

    private AnimationTimer animationTimer;

    private List<ViewObject> viewObjects;

    public ViewSimulation(ModelSimulation modelSimulation) {
        viewObjects = createShapes(modelSimulation);
    }

    private List<ViewObject> createShapes(ModelSimulation modelSimulation) {
        List<ViewObject> viewObjects = new ArrayList<>();
        modelSimulation.getGravitySimulation().getObjects().forEach(o -> {
                    Shape3D shape = new Sphere(o.getRadius());
                    Point3D lastPosition = o.getLastPosition();
                    shape.setTranslateX(lastPosition.getX());
                    shape.setTranslateY(lastPosition.getY());
                    shape.setTranslateZ(lastPosition.getZ());
                    viewObjects.add(new ViewObject(o, shape));
                }
        );
        return viewObjects;
    }

    List<Shape3D> getShapes() {
        return viewObjects.stream().map(ViewObject::getShape).collect(Collectors.toList());
    }

    public void startViewSimulation() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                viewObjects.forEach(s -> updateObjectPosition(s));


            }
        };
        animationTimer.start();
    }

    public void stopViewSimulation(){
        animationTimer.stop();
    }

    private void updateObjectPosition(ViewObject s) {
        Shape3D shape = s.getShape();
        SpaceObject spaceObject = s.getSpaceObject();
        Point3D lastPosition = spaceObject.getLastPosition();
        shape.setTranslateX(lastPosition.getX());
        shape.setTranslateY(lastPosition.getY());
        shape.setTranslateZ(lastPosition.getZ());
    }
}
