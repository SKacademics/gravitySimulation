package ch.fabian.solarSystem.model;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class ModelSimulation {

    private GravitySimulation gravitySimulation;

    private AtomicBoolean stop = new AtomicBoolean(false);
    private AtomicBoolean simulationRunning = new AtomicBoolean(false);
    private AtomicBoolean parametersChanged = new AtomicBoolean(false);

    private AtomicLong simulationStepCount = new AtomicLong();
    private SimulationParameters currentParameters;

    public ModelSimulation(GravitySimulation gravitySimulation) {
        this.gravitySimulation = gravitySimulation;
        currentParameters = gravitySimulation.getParameters();
    }

    public GravitySimulation getGravitySimulation() {
        return gravitySimulation;
    }

    public void startSimulation() {
        stop.set(false);
        simulationRunning.set(true);
        simulationStepCount.set(0);
        Runnable simulationRunner = () -> {
            while (!stop.get()) {
                updateParametersIfNecessary();
                gravitySimulation.computeNextStep();
                long simulationStep = this.simulationStepCount.incrementAndGet();
                if (simulationStep % 1000 == 0) {
                    System.out.println("Simulation step " + simulationStep);
                }
            }
            simulationRunning.set(false);
        };
        new Thread(simulationRunner).start();

    }

    private void updateParametersIfNecessary() {
        synchronized (this) {
            if(parametersChanged.get()){
                gravitySimulation.setSimulationParameters(getCurrentParameters());
            }
        }
    }

    public long getSimulationStepCount() {
        return simulationStepCount.get();
    }

    public void stop() {
        stop.set(true);
        int i = 0;
        while (simulationRunning.get()) {
            i++;
        }
        System.out.println(i);
    }

    public SimulationParameters getCurrentParameters() {
        synchronized (this) {
            return currentParameters.copy();
        }
    }

    public void setSimulationParameters(SimulationParameters simulationParameters) {
        parametersChanged.set(true);
        synchronized (this) {
            currentParameters = simulationParameters.copy();
        }
    }

    public void setTimeStep(double newTimeStep) {
        parametersChanged.set(true);
        synchronized (this) {
            currentParameters.setTimeStep(newTimeStep);
        }
    }

    public void setNewObjects(List<SpaceObject> initalObjects) {
        if (simulationRunning.get()) {
            return;
        }
        gravitySimulation.setNewObjects(initalObjects);
    }

    public int getObjectCount() {
        synchronized (this){
            return gravitySimulation.getObjects().size();
        }
    }
}
