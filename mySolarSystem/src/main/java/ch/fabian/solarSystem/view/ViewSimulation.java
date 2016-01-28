package ch.fabian.solarSystem.view;


import ch.fabian.solarSystem.model.ModelSimulation;
import ch.fabian.solarSystem.model.SpaceObject;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ViewSimulation {

    private AnimationTimer animationTimer;
    private Group simulationViewParent;
    private List<ViewObject> viewObjects;

    private long lastFrameTimeStamp;
    private DoubleProperty fpsProperty = new SimpleDoubleProperty(0);
    private long frameCount;
    private double frameTimeAvg;

    public ViewSimulation(ModelSimulation modelSimulation, Group simulationViewParent) {
        this.simulationViewParent = simulationViewParent;
        viewObjects = createShapes(modelSimulation);
        simulationViewParent.getChildren().addAll(getShapes(viewObjects));
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

    private List<Shape3D> getShapes(List<ViewObject> viewObjects) {
        return viewObjects.stream().map(ViewObject::getShape).collect(toList());
    }

    public DoubleProperty fpsProperty() {
        return fpsProperty;
    }

    public void startViewSimulation() {
        frameCount = 0;
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                viewObjects.forEach(s -> updateObjectPosition(s));
                updateAnimationFPS(now);
            }
        };
        animationTimer.start();
    }

    private void updateAnimationFPS(long now) {
        frameCount++;
        double timeForFrameMs = ((double) now - lastFrameTimeStamp) / 1000000.0;
        frameTimeAvg = 0.1 * timeForFrameMs + frameTimeAvg * 0.9;
        if (frameCount % 3 == 0) {
            double fps = 1000 / frameTimeAvg;
            fpsProperty.set(fps);
        }
        lastFrameTimeStamp = now;
    }

    public void stopViewSimulation() {
        animationTimer.stop();
    }

    public void removeAllObjects() {
        List<Node> toRemove = simulationViewParent.getChildren().stream()
                .filter(c -> c instanceof Shape3D)
                .collect(toList());
        simulationViewParent.getChildren().removeAll(toRemove);
    }

    private void updateObjectPosition(ViewObject s) {
        Shape3D shape = s.getShape();
        SpaceObject spaceObject = s.getSpaceObject();
        Point3D lastPosition = spaceObject.getLastPosition();
        shape.setTranslateX(lastPosition.getX());
        shape.setTranslateY(lastPosition.getY());
        shape.setTranslateZ(lastPosition.getZ());
    }

    public void setModelSimulation(ModelSimulation modelSimulation) {
        removeAllObjects();
        viewObjects = createShapes(modelSimulation);
        simulationViewParent.getChildren().addAll(getShapes(viewObjects));
    }
}
