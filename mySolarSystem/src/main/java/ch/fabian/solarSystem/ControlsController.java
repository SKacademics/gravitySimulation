package ch.fabian.solarSystem;

import ch.fabian.solarSystem.model.GravitySimulation;
import ch.fabian.solarSystem.model.ModelSimulation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    private ModelSimulation currentSimulation;

    public ControlsController() {
        resetListeners = new ArrayList<>();
    }

    public void setCurrentSimulation(ModelSimulation currentSimulation) {
        if(this.currentSimulation != null){
            timeStepSlider.valueProperty().unbind();
        }
        GravitySimulation gravitySimulation = currentSimulation.getGravitySimulation();
        this.currentSimulation = currentSimulation;
        currentTimeStep.setText(formatTimeStepLabel(gravitySimulation.getParameters().getTimeStep()));
        timeStepSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            currentTimeStep.setText(formatTimeStepLabel(newValue));
            currentSimulation.setTimeStep(newValue.doubleValue());
        });
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
