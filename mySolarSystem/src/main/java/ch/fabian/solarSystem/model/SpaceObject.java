package ch.fabian.solarSystem.model;

import javafx.geometry.Point3D;

public class SpaceObject {

    //in kg
    private double mass = 1;

    private double radius = 1;

    //in m/s
    private Point3D speed = new Point3D(0,0,0);
    private Point3D lastSpeed = new Point3D(0,0,0);

    private double x;
    private double y;
    private double z;

    private double lastX;
    private double lastY;
    private double lastZ;

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
        return new Point3D(x, y, z);
    }

    public Point3D getLastPosition() {
        return new Point3D(lastX, lastY, lastZ);
    }

    public void setPosition(Point3D inPosition) {
        x = inPosition.getX();
        y = inPosition.getY();
        z = inPosition.getZ();
    }

    public void setLastPosition(Point3D inLastPosition) {
        lastX = inLastPosition.getX();
        lastY = inLastPosition.getY();
        lastZ = inLastPosition.getZ();
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
