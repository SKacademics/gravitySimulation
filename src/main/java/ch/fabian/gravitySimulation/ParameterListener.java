package ch.fabian.gravitySimulation;

import ch.fabian.gravitySimulation.model.SimulationParameters;

public interface ParameterListener {

    void parametersChanged(SimulationParameters parameters);

}
