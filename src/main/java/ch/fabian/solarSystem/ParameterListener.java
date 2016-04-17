package ch.fabian.solarSystem;

import ch.fabian.solarSystem.model.SimulationParameters;

public interface ParameterListener {

    void parametersChanged(SimulationParameters parameters);

}
