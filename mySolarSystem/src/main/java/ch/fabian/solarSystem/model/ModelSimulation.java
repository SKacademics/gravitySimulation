package ch.fabian.solarSystem.model;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class ModelSimulation {

    private GravitySimulation gravitySimulation;

    private AtomicBoolean stop = new AtomicBoolean(false);
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

    public void startSimulation(){
        Runnable simulationRunner = () -> {
            while (!stop.get()) {
                synchronized (this){
                    gravitySimulation.setSimulationParameters(getCurrentParameters());
                }
                gravitySimulation.computeNextStep();
                long simulationStep = this.simulationStepCount.incrementAndGet();
                if(simulationStep % 100 == 0){
                    System.out.println("Simulation step " + simulationStep);
                }
            }
        };
        Thread simulationThread = new Thread(simulationRunner);
        simulationThread.start();
    }

    public long getSimulationStepCount(){
        return simulationStepCount.get();
    }

    public void stop(){
        stop.set(true);
    }

    public SimulationParameters getCurrentParameters() {
        synchronized (this){
            return currentParameters.copy();
        }
    }

    public void setTimeStep(double newTimeStep) {
        parametersChanged.set(true);
        synchronized (this){
            currentParameters = new SimulationParameters(newTimeStep, currentParameters.isWeakenCollisions(), false);
        }
    }
}
