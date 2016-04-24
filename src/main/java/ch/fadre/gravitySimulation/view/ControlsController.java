package ch.fadre.gravitySimulation.view;

import ch.fadre.gravitySimulation.IResetListener;
import ch.fadre.gravitySimulation.SimulationController;
import ch.fadre.gravitySimulation.model.GravitySimulation;
import ch.fadre.gravitySimulation.model.ModelSimulation;
import ch.fadre.gravitySimulation.model.SimulationParameters;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ControlsController {

    private List<IResetListener> simulationResetListeners;
    private List<IResetListener> cameraResetListeners;

    @FXML
    private Label currentTimeStep;
    @FXML
    private Slider timeStepSlider;
    @FXML
    private Label animationFPSLabel;
    @FXML
    private Label simulationStepsLabel;
    @FXML
    private Label simulatedTimeLabel;
    @FXML
    private TextField maximumTimeStepField;
    @FXML
    private CheckBox weakCollisionsCheckBox;
    @FXML
    private CheckBox mergeObjectsCheckBox;
    @FXML
    private Label objectCountLabel;

    private ModelSimulation currentSimulation;
    private SimulationController simulationController;

    public ControlsController() {
        simulationResetListeners = new ArrayList<>();
        cameraResetListeners = new ArrayList<>();
    }

    void setCurrentSimulation(SimulationController controller) {
        this.simulationController = controller;
        if (this.currentSimulation != null) {
            timeStepSlider.valueProperty().unbind();
        }
        this.currentSimulation = controller.getModelSimulation();
        GravitySimulation gravitySimulation = currentSimulation.getGravitySimulation();
        double timeStep = gravitySimulation.getParameters().getTimeStep();
        timeStepSlider.setValue(timeStep);
        mergeObjectsCheckBox.selectedProperty().setValue(currentSimulation.getCurrentParameters().isMergeObjects());
        weakCollisionsCheckBox.selectedProperty().setValue(currentSimulation.getCurrentParameters().isWeakenCollisions());

        StringConverter<Number> numberStringConverter = new NumberStringConverter();
        Bindings.bindBidirectional(maximumTimeStepField.textProperty(), timeStepSlider.maxProperty(), numberStringConverter);
        timeStepSlider.setMin(0);
        timeStepSlider.setMajorTickUnit(0.01);
        currentTimeStep.setText(formatTimeStepLabel(timeStep));
        timeStepSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            currentTimeStep.setText(formatTimeStepLabel(newValue));
            currentSimulation.setTimeStep(newValue.doubleValue());
        });
        mergeObjectsCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            SimulationParameters currentParameters = currentSimulation.getCurrentParameters();
            currentParameters.setMergeObjects(newValue);
            currentSimulation.setSimulationParameters(currentParameters);
        });

        weakCollisionsCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            SimulationParameters currentParameters = currentSimulation.getCurrentParameters();
            currentParameters.setWeakenCollisions(newValue);
            currentSimulation.setSimulationParameters(currentParameters);
        });
        controller.getViewSimulation().fpsProperty().addListener((observable, oldValue, newValue) -> {
            animationFPSLabel.setText(formatTimeStepLabel(newValue) + " FPS");
            if (changeCount % 2 == 0) {
                simulationStepsLabel.setText(formatSimulationSteps(computeSimulationStepsPerSecond(currentSimulation.getSimulationStepCount())));
            }
            objectCountLabel.setText(String.valueOf(currentSimulation.getObjectCount()));
            simulatedTimeLabel.setText(String.format("%.2f", currentSimulation.getSimulatedTime().doubleValue()) + " s");
            changeCount++;
        });
    }

    private long changeCount = 0;

    private double computeSimulationStepsPerSecond(long stepCount) {
        long currentTime = System.nanoTime();
        long stepDifference = stepCount - lastStepCount;
        long timeDifferenceNs = currentTime - lastStepTimeStamp;
        double stepsPerSecond = ((double) stepDifference / timeDifferenceNs) * 1_000_000_000;
        double weightedStepsPerSecond = 0.9 * lastStepsPerSecond + 0.1 * stepsPerSecond;
        lastStepTimeStamp = currentTime;
        lastStepCount = stepCount;
        lastStepsPerSecond = weightedStepsPerSecond;
        return weightedStepsPerSecond;
    }

    private double lastStepsPerSecond;
    private long lastStepCount;
    private long lastStepTimeStamp;

    private String formatSimulationSteps(Number value) {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setMaximumFractionDigits(2);
        return decimalFormat.format(value);
    }

    private String formatTimeStepLabel(Number value) {
        return String.format("%.02f", value.doubleValue()) + " s";
    }

    public void resetButtonAction() {
        simulationResetListeners.forEach(IResetListener::reset);
        lastStepCount = 0;
    }

    void addResetListener(IResetListener listener) {
        simulationResetListeners.add(listener);
    }

    void updateParams(SimulationParameters newParameters) {
        timeStepSlider.setValue(newParameters.getTimeStep());
        weakCollisionsCheckBox.selectedProperty().setValue(newParameters.isWeakenCollisions());
        mergeObjectsCheckBox.selectedProperty().setValue(newParameters.isMergeObjects());
    }

    public void resetCameraAction() {
        cameraResetListeners.forEach(IResetListener::reset);
    }

    void addCameraResetListener(IResetListener resetListener) {
        cameraResetListeners.add(resetListener);
    }

    public void showAddObject() throws IOException {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initModality(Modality.NONE);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Add Object");
        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        FXMLLoader loader = new FXMLLoader(AddObjectController.class.getResource("addObject.fxml"));
        Pane myPane = loader.load();
        AddObjectController controller = loader.getController();
        controller.setSimulationController(simulationController);

        alert.onCloseRequestProperty().addListener(e -> alert.close());
        alert.setOnCloseRequest(e -> alert.close());
        alert.getDialogPane().setContent(myPane);
        alert.show();
    }
}

