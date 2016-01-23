package ch.fabian.solarSystem;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;

public class SpaceObject {

    //in kg
    private double mass = 1;

    private double radius = 1;

    //in m/s
    private Point3D speed = new Point3D(0,0,0);
    private Point3D lastSpeed = new Point3D(0,0,0);

    private DoubleProperty xProperty = new SimpleDoubleProperty();
    private DoubleProperty yProperty = new SimpleDoubleProperty();
    private DoubleProperty zProperty = new SimpleDoubleProperty();

    private DoubleProperty lastXProperty = new SimpleDoubleProperty();
    private DoubleProperty lastYProperty = new SimpleDoubleProperty();
    private DoubleProperty lastZProperty = new SimpleDoubleProperty();

    public SpaceObject(double radius) {
        this.radius = radius;
    }

    public SpaceObject(double radius, double mass) {
        this(radius);
        this.mass = mass;
    }

    public SpaceObject() {

    }

    public Point3D getPosition() {
        return new Point3D(xProperty.get(),yProperty.get(),zProperty.get());
    }

    public Point3D getLastPosition() {
        return new Point3D(lastXProperty.get(),lastYProperty.get(),lastZProperty.get());
    }

    public void setPosition(Point3D inPosition) {
        xProperty.set(inPosition.getX());
        yProperty.set(inPosition.getY());
        zProperty.set(inPosition.getZ());
    }

    public void setLastPosition(Point3D inLastPosition) {
        lastXProperty.set(inLastPosition.getX());
        lastYProperty.set(inLastPosition.getY());
        lastZProperty.set(inLastPosition.getZ());
    }

    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    public void setMass(double inMass) {
        mass = inMass;
    }

    public double getXProperty() {
        return xProperty.get();
    }

    public DoubleProperty xPropertyProperty() {
        return xProperty;
    }

    public double getyProperty() {
        return yProperty.get();
    }

    public DoubleProperty yPropertyProperty() {
        return yProperty;
    }

    public double getzProperty() {
        return zProperty.get();
    }

    public DoubleProperty zPropertyProperty() {
        return zProperty;
    }

    public double getLastXProperty() {
        return lastXProperty.get();
    }

    public DoubleProperty lastXPropertyProperty() {
        return lastXProperty;
    }

    public double getLastYProperty() {
        return lastYProperty.get();
    }

    public DoubleProperty lastYPropertyProperty() {
        return lastYProperty;
    }

    public double getLastZProperty() {
        return lastZProperty.get();
    }

    public DoubleProperty lastZPropertyProperty() {
        return lastZProperty;
    }

    public Point3D getLastSpeed() {
        return lastSpeed;
    }

    public Point3D getSpeed() {
        return speed;
    }

    public void setSpeed(Point3D inSpeed) {
        speed = inSpeed;
    }

    public void setLastSpeed(Point3D inLastSpeed) {
        lastSpeed = inLastSpeed;
    }
}
