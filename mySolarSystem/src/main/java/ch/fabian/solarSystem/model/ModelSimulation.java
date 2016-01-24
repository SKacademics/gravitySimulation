package ch.fabian.solarSystem.model;

import java.util.concurrent.atomic.AtomicBoolean;

public class ModelSimulation {

    private GravitySimulation gravitySimulation;

    private AtomicBoolean stop = new AtomicBoolean(false);
    private long simulationStep = 0L;

    public ModelSimulation(GravitySimulation gravitySimulation) {
        this.gravitySimulation = gravitySimulation;
    }

    public GravitySimulation getGravitySimulation() {
        return gravitySimulation;
    }

    public void startSimulation(){
        Runnable simulationRunner = () -> {
            while (!stop.get()) {
                gravitySimulation.computeNextStep();
                simulationStep++;
                if(simulationStep % 100 == 0){
                    System.out.println("Simulation step " + simulationStep);
                }
            }
        };
        Thread simulationThread = new Thread(simulationRunner);
        simulationThread.start();
    }

    public void stop(){
        stop.set(true);
    }
}
