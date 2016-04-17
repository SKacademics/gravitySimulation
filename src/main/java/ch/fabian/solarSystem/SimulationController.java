package ch.fabian.solarSystem;

import ch.fabian.solarSystem.model.ModelSimulation;
import ch.fabian.solarSystem.model.ResetHandler;
import ch.fabian.solarSystem.model.SimulationParameters;
import ch.fabian.solarSystem.view.ViewSimulation;

import java.util.ArrayList;
import java.util.List;

public class SimulationController {

    private ModelSimulation modelSimulation;
    private ViewSimulation viewSimulation;
    private ResetHandler resetHandler;
    private List<ParameterListener> parameterListeners = new ArrayList<>();

    public SimulationController(ModelSimulation modelSimulation) {
        this.modelSimulation = modelSimulation;
        resetHandler = new ResetHandler();
    }

    public void setViewSimulation(ViewSimulation viewSimulation) {
        this.viewSimulation = viewSimulation;
    }

    public void startSimulation() {
        resetHandler.init(modelSimulation);
        modelSimulation.startSimulation();
        if(viewSimulation != null){
            viewSimulation.startViewSimulation();
        }
    }

    public void stopSimulation() {
        if(viewSimulation != null){
            viewSimulation.stopViewSimulation();
        }
        modelSimulation.stop();
    }

    public ModelSimulation getModelSimulation() {
        return modelSimulation;
    }

    public ViewSimulation getViewSimulation() {
        return viewSimulation;
    }

    public void addParameterListener(ParameterListener listener){
        parameterListeners.add(listener);
    }

    public void resetScene() {
        stopSimulation();

        SimulationParameters initialParameters = resetHandler.getInitialSimulationParameters();
        modelSimulation.setSimulationParameters(initialParameters);
        parameterListeners.stream().forEach(l -> l.parametersChanged(initialParameters));
        modelSimulation.setNewObjects(resetHandler.getInitialObjects());
        viewSimulation.setModelSimulation(modelSimulation);

        startSimulation();
    }

}
