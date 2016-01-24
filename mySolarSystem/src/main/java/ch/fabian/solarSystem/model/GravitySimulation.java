package ch.fabian.solarSystem.model;

import javafx.geometry.Point3D;

import java.util.List;

import static java.util.stream.Collectors.toList;


public class GravitySimulation {

    static final double GRAVITY_CONSTANT = 6.674*10E-11;

    private List<SpaceObject> objects;
    private SimulationParameters parameters;

    public GravitySimulation(List<SpaceObject> inObjects) {
        objects = inObjects;
    }

    void computeNextStep(){
        SimulationParameters paramsForStep = parameters;
        double currentTimeStep = paramsForStep.getTimeStep();
        for (SpaceObject current : objects) {
            Point3D gravitationalForce = computeGravityVector(current);
            //v2 = a * (dt) + v1
            Point3D newSpeed = gravitationalForce.multiply(currentTimeStep).add(current.getLastSpeed());
            Point3D distance = newSpeed.multiply(currentTimeStep);
            Point3D newPosition = current.getLastPosition().add(distance);
            current.setPosition(newPosition);
            current.setLastSpeed(newSpeed);
        }
        objects.stream().forEach(o -> o.setLastPosition(o.getPosition()));
    }



    Point3D computeGravityVector(SpaceObject current) {
        List<Point3D> forces = objects.stream().filter(other -> current != other).map(other -> {
            double force = computeGravityBetweenBodies(current, other);
            Point3D direction = other.getLastPosition().subtract(current.getLastPosition());
            //acceleration
            return direction.normalize().multiply(force).multiply(1 / current.getMass());
        }).collect(toList());
        return forces.stream().reduce(Point3D::add).orElse(new Point3D(0,0,0));
    }

    double computeGravityBetweenBodies(SpaceObject inBody1, SpaceObject inBody2) {
        double distance = inBody1.getLastPosition().distance(inBody2.getLastPosition());
        if(distance < 1 && parameters.isWeakenCollisions()){
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
}
