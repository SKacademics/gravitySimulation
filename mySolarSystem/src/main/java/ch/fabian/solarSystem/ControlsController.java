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

    private GravitySimulation currentSimulation;

    public ControlsController() {
        resetListeners = new ArrayList<>();
    }

    public void setCurrentSimulation(GravitySimulation currentSimulation) {
        if(this.currentSimulation != null){
            timeStepSlider.valueProperty().unbind();
        }
        this.currentSimulation = currentSimulation;
        currentTimeStep.setText(formatTimeStepLabel(currentSimulation.timeStepProperty().getValue()));
        currentSimulation.timeStepProperty().addListener((observable, oldValue, newValue) -> {
            currentTimeStep.setText(formatTimeStepLabel(newValue));
        });
        timeStepSlider.valueProperty().bindBidirectional(currentSimulation.timeStepProperty());
    }

    private String formatTimeStepLabel(Number value) {
        return String.format("%.02f",value.doubleValue()) + " s";
    }

    public void resetButtonAction(){
        resetListeners.forEach(IResetListener::reset);
    }


    public void addResetListener(IResetListener listener) {
        resetListeners.add(listener);
    }
}
