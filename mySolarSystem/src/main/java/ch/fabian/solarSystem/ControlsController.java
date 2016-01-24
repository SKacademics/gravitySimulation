package ch.fabian.solarSystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.util.ArrayList;
import java.util.List;

public class ControlsController {

    private List<IResetListener> resetListeners;

    @FXML
    private Label currentTimeStep;

    @FXML
    private Slider timeStepSlider;

    private GravitySimulation simulation;

    public ControlsController() {
        resetListeners = new ArrayList<>();
    }

    public void setSimulation(GravitySimulation simulation) {
        this.simulation = simulation;
        currentTimeStep.setText(simulation.timeStepProperty().toString() + " s");
        simulation.timeStepProperty().addListener((observable, oldValue, newValue) -> {
            currentTimeStep.setText(newValue.toString() +" s");
        });
        timeStepSlider.valueProperty().bindBidirectional(simulation.timeStepProperty());
    }

    public void resetButtonAction(){
        resetListeners.forEach(IResetListener::reset);
    }


    public void addResetListener(IResetListener listener) {
        resetListeners.add(listener);
    }
}
