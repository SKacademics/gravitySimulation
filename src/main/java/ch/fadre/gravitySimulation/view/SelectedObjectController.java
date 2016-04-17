package ch.fadre.gravitySimulation.view;

import ch.fadre.gravitySimulation.model.SpaceObject;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.shape.Shape3D;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SelectedObjectController implements ObjectSelectionListener {

    @FXML
    private Label positionLabel;

    @FXML
    private Label speedLabel;

    @FXML
    private Label speedMagnitudeLabel;

    @FXML
    private Label massLabel;

    @FXML
    private Label radiusLabel;

    @FXML
    private CheckBox followCheckbox;

    @FXML
    private TitledPane selectedObjectPane;

    private ViewObject currentSelection;
    private ChangeListener<Number> positionListener;

    private List<FollowListener> followListenerList = new ArrayList<>();


    private String createPositionString(Shape3D obj) {
        return format3D(obj.getTranslateX(), obj.getTranslateY(), obj.getTranslateZ());
    }

    private String createSpeedString(ViewObject obj) {
        Point3D lastSpeed = obj.getSpaceObject().getLastSpeed();
        return format3D(lastSpeed.getX(), lastSpeed.getY(), lastSpeed.getZ()) + " m/s";
    }


    private String format3D(double valueX, double valueY, double valueZ) {
        DecimalFormat formatter = new DecimalFormat();
        formatter.setMaximumFractionDigits(4);
        formatter.setMinimumFractionDigits(4);
        return "(" + formatter.format(valueX) + "/" + formatter.format(valueY) + "/" + formatter.format(valueZ) + ")";
    }


    @Override
    public void objectSelected(ViewObject selectedObject) {
        removeListenerFromPrevious();
        currentSelection = selectedObject;
        if (selectedObject != null) {
            Shape3D selectedShape = selectedObject.getShape();
            ChangeListener<Number> shapeListener = (observable, oldValue, newValue) -> {
                positionLabel.setText(createPositionString(selectedShape));
                speedLabel.setText(createSpeedString(selectedObject));
                SpaceObject spaceObject = selectedObject.getSpaceObject();
                speedMagnitudeLabel.setText(String.format("%.4f",spaceObject.getLastSpeed().magnitude()) + " m/s");
                massLabel.setText(String.format("%G",spaceObject.getMass()) + " kg");
                radiusLabel.setText(String.format("%.2f",spaceObject.getRadius()) + " m");
            };
            followCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue){
                    followListenerList.forEach(l -> l.followWithCamera(selectedObject));
                } else {
                    followListenerList.forEach(l -> l.followWithCamera(null));
                }
            });
            positionListener = shapeListener;
            selectedShape.translateXProperty().addListener(shapeListener);
            selectedShape.translateYProperty().addListener(shapeListener);
            selectedShape.translateZProperty().addListener(shapeListener);
            selectedObjectPane.setVisible(true);
        } else {
            selectedObjectPane.setVisible(false);
        }

    }

    private void removeListenerFromPrevious() {
        if (currentSelection != null) {
            Shape3D currentShape = currentSelection.getShape();
            currentShape.translateXProperty().removeListener(positionListener);
            currentShape.translateYProperty().removeListener(positionListener);
            currentShape.translateZProperty().removeListener(positionListener);
        }
    }

    public void addFollowListener(FollowListener listener) {
        followListenerList.add(listener);
    }
}
