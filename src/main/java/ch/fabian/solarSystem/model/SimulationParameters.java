package ch.fabian.solarSystem.model;


public class SimulationParameters {

    private double timeStep;
    private boolean weakenCollisions;
    private boolean mergeObjects;

    public SimulationParameters(double timeStep) {
        this(timeStep, true, true);
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

    public boolean isMergeObjects() {
        return mergeObjects;
    }

    public void setTimeStep(double timeStep) {
        this.timeStep = timeStep;
    }

    public void setWeakenCollisions(boolean weakenCollisions) {
        this.weakenCollisions = weakenCollisions;
    }

    public void setMergeObjects(boolean mergeObjects) {
        this.mergeObjects = mergeObjects;
    }
}
