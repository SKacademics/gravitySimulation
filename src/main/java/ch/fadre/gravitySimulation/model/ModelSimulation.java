package ch.fadre.gravitySimulation.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class ModelSimulation {

    private GravitySimulation gravitySimulation;

    private AtomicBoolean stop = new AtomicBoolean(false);
    private AtomicBoolean simulationRunning = new AtomicBoolean(false);
    private AtomicBoolean parametersChanged = new AtomicBoolean(false);

    private AtomicLong simulationStepCount = new AtomicLong();
    private BigDecimal simulatedTime = BigDecimal.ZERO;

    private List<SpaceObject> newObjects = new ArrayList<>();

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
                simulatedTime = simulatedTime.add(BigDecimal.valueOf(currentParameters.getTimeStep()));
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

    public void addObject(SpaceObject newObject){
        newObjects.add(newObject);
    }

    public BigDecimal getSimulatedTime() {
        synchronized (this) {
            return simulatedTime;
        }
    }

    private void updateParametersIfNecessary() {
        synchronized (this) {
            if(parametersChanged.get() || !newObjects.isEmpty()){
                SimulationParameters currentParameters = getCurrentParameters();
                gravitySimulation.setSimulationParameters(currentParameters);
                newObjects.forEach(o -> gravitySimulation.addObject(o));
                newObjects.clear();
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

    public void setNewObjects(List<SpaceObject> initialObjects) {
        if (simulationRunning.get()) {
            return;
        }
        gravitySimulation.setNewObjects(initialObjects);
    }

    public int getObjectCount() {
        synchronized (this){
            return gravitySimulation.getObjects().size();
        }
    }
}
