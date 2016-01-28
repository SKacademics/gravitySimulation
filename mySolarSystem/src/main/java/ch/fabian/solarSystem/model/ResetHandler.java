package ch.fabian.solarSystem.model;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class ResetHandler {

    private SimulationParameters simulationParameters;
    private List<SpaceObject> initalObjects;

    public void init(ModelSimulation modelSimulation) {
        simulationParameters = modelSimulation.getCurrentParameters().copy();
        initalObjects = modelSimulation.getGravitySimulation().getObjects().stream().map(this::createCopy).collect(toList());
    }

    public SimulationParameters getInitialSimulationParameters() {
        return simulationParameters;
    }

    public List<SpaceObject> getInitialObjects() {
        return initalObjects;
    }

    private SpaceObject createCopy(SpaceObject spaceObject){
        SpaceObject copy = new SpaceObject(spaceObject.getRadius(), spaceObject.getMass());
        copy.setPosition(spaceObject.getPosition());
        copy.setLastPosition(spaceObject.getLastPosition());
        copy.setSpeed(spaceObject.getSpeed());
        copy.setLastSpeed(spaceObject.getLastSpeed());
        return copy;
    }
}

