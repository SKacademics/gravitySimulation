package ch.fadre.gravitySimulation.view;


import ch.fadre.gravitySimulation.model.ModelSimulation;
import ch.fadre.gravitySimulation.model.SpaceObject;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class ViewSimulation {

    private AnimationTimer animationTimer;
    private Group simulationViewParent;
    private List<ViewObject> viewObjects;

    private long lastFrameTimeStamp;
    private DoubleProperty fpsProperty = new SimpleDoubleProperty(0);
    private long frameCount;
    private double frameTimeAvg;

    private List<ObjectSelectionListener> selectionListeners = new ArrayList<>();

    ViewSimulation(ModelSimulation modelSimulation, Group simulationViewParent) {
        this.simulationViewParent = simulationViewParent;
        viewObjects = createShapes(modelSimulation);
        simulationViewParent.getChildren().addAll(getShapes(viewObjects));
    }

    private List<ViewObject> createShapes(ModelSimulation modelSimulation) {
        List<ViewObject> viewObjects = new ArrayList<>();
        modelSimulation.getGravitySimulation().getObjects().forEach(o -> viewObjects.add(createViewObject(o)));
        return viewObjects;
    }

    public void addObject(SpaceObject spaceObject) {
        ViewObject viewObject = createViewObject(spaceObject);
        viewObjects.add(viewObject);
        simulationViewParent.getChildren().addAll(viewObject.getShape());
    }

    private ViewObject createViewObject(SpaceObject o) {
        Shape3D shape = new Sphere(o.getRadius());
        Point3D lastPosition = o.getLastPosition();
        shape.setTranslateX(lastPosition.getX());
        shape.setTranslateY(lastPosition.getY());
        shape.setTranslateZ(lastPosition.getZ());
        ViewObject viewObject = new ViewObject(o, shape);
        shape.setOnMouseClicked(event -> handleSelection(viewObject));
        return viewObject;
    }

    private void handleSelection(ViewObject viewObject) {
        //reset all other selection
        viewObjects.stream().filter(vo -> vo != viewObject).map(ViewObject::getShape).forEach(s -> s.setMaterial(null));
        Shape3D selectedShape = viewObject.getShape();
        if (selectedShape.getMaterial() == null) {
            selectedShape.setMaterial(new PhongMaterial(Color.AQUAMARINE));
            selectionListeners.forEach(l -> l.objectSelected(viewObject));
        } else {
            selectedShape.setMaterial(null);
            selectionListeners.forEach(l -> l.objectSelected(null));
        }
    }


    private List<Shape3D> getShapes(List<ViewObject> viewObjects) {
        return viewObjects.stream().map(ViewObject::getShape).collect(toList());
    }

    DoubleProperty fpsProperty() {
        return fpsProperty;
    }

    public void startViewSimulation() {
        frameCount = 0;
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                List<ViewObject> removedObjects = viewObjects.stream()
                        .filter(viewObject -> viewObject.getSpaceObject().isRemoved())
                        .collect(Collectors.toList());
                viewObjects.removeAll(removedObjects);
                removedObjects.forEach(toRemove -> {
                    viewObjects.remove(toRemove);
                    simulationViewParent.getChildren().remove(toRemove.getShape());
                });
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

    private void removeAllObjects() {
        List<Node> toRemove = simulationViewParent.getChildren().stream()
                .filter(c -> c instanceof Shape3D)
                .collect(toList());
        simulationViewParent.getChildren().removeAll(toRemove);
    }

    private void updateObjectPosition(ViewObject s) {
        Shape3D shape = s.getShape();
        Sphere shape1 = (Sphere) shape;
        shape1.setRadius(s.getSpaceObject().getRadius());
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

    void addSelectionListener(ObjectSelectionListener selectionListener) {
        selectionListeners.add(selectionListener);
    }
}
