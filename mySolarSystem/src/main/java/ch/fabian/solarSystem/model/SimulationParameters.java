package ch.fabian.solarSystem.model;


public class SimulationParameters {

    private final double timeStep;
    private final boolean weakenCollisions;
    private final boolean mergeObjects;

    public SimulationParameters(double timeStep) {
        this(timeStep, false, false);
    }

    public SimulationParameters(double timeStep, boolean weakenCollisions, boolean mergeObjects) {
        this.timeStep = timeStep;
        this.weakenCollisions = weakenCollisions;
        this.mergeObjects = mergeObjects;
    }

    public SimulationParameters copy() {
        return new SimulationParameters(timeStep,weakenCollisions,mergeObjects);
    }

    public double getTimeStep() {
        return timeStep;
    }

    public boolean isWeakenCollisions() {
        return weakenCollisions;
    }
}
