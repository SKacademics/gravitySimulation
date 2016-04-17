package ch.fadre.gravitySimulation.model;

import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


public class GravitySimulation {

    static final double GRAVITY_CONSTANT = 6.674 * 10E-11;
    private static final int SINGLE_THREAD_OBJECT_LIMIT = 50;
    private static final double ONE_THIRD = 1.0 / 3.0;
    public static final double SPHERE_EQUATION_CONSTANT = 0.620350490899400;

    private List<SpaceObject> objects;
    private SimulationParameters parameters;

    public GravitySimulation(List<SpaceObject> inObjects, SimulationParameters inParameters) {
        objects = new ArrayList<>(inObjects);
        parameters = inParameters.copy();
    }

    void computeNextStep() {
        SimulationParameters paramsForStep = parameters;
        double currentTimeStep = paramsForStep.getTimeStep();
        getStream().forEach(current -> {
            Point3D gravitationalForce = computeGravityVector(current);
            //v2 = a * (dt) + v1
            Point3D newSpeed = gravitationalForce.multiply(currentTimeStep).add(current.getLastSpeed());
            Point3D distance = newSpeed.multiply(currentTimeStep);
            Point3D newPosition = current.getLastPosition().add(distance);
            current.setPosition(newPosition);
            current.setSpeed(newSpeed);
        });
        if (parameters.isMergeObjects()) {
            objects.removeAll(computeObjectsToRemove());
        }

        getStream().forEach(o -> o.setLastPosition(o.getPosition()));
        getStream().forEach(o -> o.setLastSpeed(o.getSpeed()));
    }

    private List<SpaceObject> computeObjectsToRemove() {
        List<SpaceObject> removed = new ArrayList<>();
        getStream().filter(c -> !c.isRemoved()).forEach(current -> {
                        List<SpaceObject> collidedWith = computeCollisionsOfCurrent(current);
                        collidedWith.stream().forEach(other -> {
                            if (current.getRadius() >= other.getRadius() && current.getMass() >= other.getMass()) {
                                synchronized (this){
                                    performCollision(current, other);

                                    other.setRemoved(true);

                                }
                                removed.add(other);
                            }
                        });
                    }
        );
        return removed;
    }

    private void performCollision(SpaceObject current, SpaceObject other) {
        current.setMass(computeCollisionResultMass(current, other));
        current.setRadius(computeCollisionResultRadius(current, other));
        current.setSpeed(computeCollisionResultSpeed(current, other));
    }

    private Point3D computeCollisionResultSpeed(SpaceObject current, SpaceObject other) {
        double m1 = current.getMass();
        double m2 = other.getMass();

        Point3D v1 = current.getLastSpeed();
        Point3D v2 = other.getLastSpeed();

        return v1.multiply(m1).add(v2.multiply(m2)).multiply(1.0 / (m1 + m2));
    }

    private double computeCollisionResultRadius(SpaceObject current, SpaceObject other) {
        double newVolume = current.getVolume() + other.getVolume();
        //V = 4/3 * PI * r^3 , solved to r = constant * V^1/3
        return SPHERE_EQUATION_CONSTANT * Math.pow(newVolume, ONE_THIRD);
    }

    private double computeCollisionResultMass(SpaceObject current, SpaceObject other) {
        return current.getMass() + other.getMass();
    }


    private List<SpaceObject> computeCollisionsOfCurrent(SpaceObject current) {
        return getStream().filter(other -> other != current)
                          .filter(other -> isCollided(current, other))
                          .collect(toList());
    }

    private boolean isCollided(SpaceObject current, SpaceObject other) {
        double noCollisionDistance = current.getRadius() + other.getRadius();
        double objectDistance = other.getPosition().distance(current.getPosition());
        return objectDistance < noCollisionDistance;
    }

    private Stream<SpaceObject> getStream() {
        if (objects.size() > SINGLE_THREAD_OBJECT_LIMIT) {
            return objects.parallelStream();
        } else {
            return objects.stream();
        }
    }

    Point3D computeGravityVector(SpaceObject current) {
        List<Point3D> forces = objects.stream().filter(other -> current != other).map(other -> {
            double force = computeGravityBetweenBodies(current, other);
            Point3D direction = other.getLastPosition().subtract(current.getLastPosition());
            //acceleration
            return direction.normalize().multiply(force).multiply(1 / current.getMass());
        }).collect(toList());
        return forces.stream().reduce(Point3D::add).orElse(new Point3D(0, 0, 0));
    }

    double computeGravityBetweenBodies(SpaceObject inBody1, SpaceObject inBody2) {
        double distance = inBody1.getLastPosition().distance(inBody2.getLastPosition());
        double collisionDistance = inBody1.getRadius() + inBody2.getRadius();
        if (distance < collisionDistance && parameters.isWeakenCollisions()) {
            return 0;
        }
        double mass1 = inBody1.getMass();
        double mass2 = inBody2.getMass();
        return GRAVITY_CONSTANT * mass1 * mass2 / (distance * distance);
    }

    public List<SpaceObject> getObjects() {
        return objects;
    }

    public SimulationParameters getParameters() {
        return parameters;
    }

    public void setSimulationParameters(SimulationParameters newParameters) {
        parameters = newParameters;
    }

    public void setNewObjects(List<SpaceObject> objects) {
        this.objects = objects;
    }
}
