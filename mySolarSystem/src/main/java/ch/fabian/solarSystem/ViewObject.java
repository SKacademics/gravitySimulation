package ch.fabian.solarSystem;

import javafx.scene.shape.Shape3D;

public class ViewObject {

    private SpaceObject spaceObject;
    private Shape3D shape;

    public ViewObject(SpaceObject spaceObject, Shape3D shape) {
        this.spaceObject = spaceObject;
        this.shape = shape;
    }

    public SpaceObject getSpaceObject() {
        return spaceObject;
    }

    public void setSpaceObject(SpaceObject spaceObject) {
        this.spaceObject = spaceObject;
    }

    public Shape3D getShape() {
        return shape;
    }

    public void setShape(Shape3D shape) {
        this.shape = shape;
    }
}
