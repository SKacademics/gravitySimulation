package ch.fabian.solarSystem;

import java.util.concurrent.atomic.AtomicBoolean;

public class SimulationRunner {

    private GravitySimulation gravitySimulation;

    private AtomicBoolean stop = new AtomicBoolean(false);

    public SimulationRunner(GravitySimulation gravitySimulation) {
        this.gravitySimulation = gravitySimulation;
    }

    public GravitySimulation getGravitySimulation() {
        return gravitySimulation;
    }

    public void startSimulation(){
        Runnable simulationRunner = () -> {
            int i = 0;
            while (!stop.get()) {
                gravitySimulation.computeNextStep();
                i++;
            }
        };
        Thread simulationThread = new Thread(simulationRunner);
        simulationThread.start();
    }

    public void stop(){
        stop.set(true);
    }
}
