package ch.fadre.gravitySimulation.view;

import ch.fadre.gravitySimulation.SimulationController;
import ch.fadre.gravitySimulation.model.SpaceObject;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.control.TextField;

public class AddObjectController {

    @FXML private TextField posX;
    @FXML private TextField posY;
    @FXML private TextField posZ;
    @FXML private TextField speedX;
    @FXML private TextField speedY;
    @FXML private TextField speedZ;
    @FXML private TextField mass;

    private SimulationController simulationController;

    public AddObjectController() {

    }

    void setSimulationController(SimulationController simulationController) {
        this.simulationController = simulationController;
    }

    public void onAddObject() {
        SpaceObject spaceObject = new SpaceObject(1.0, asDouble(mass));
        spaceObject.setLastPosition(new Point3D(asDouble(posX), asDouble(posY), asDouble(posZ)));
        spaceObject.setLastSpeed(new Point3D(asDouble(speedX), asDouble(speedY), asDouble(speedZ)));
        simulationController.addObject(spaceObject);
    }

    private double asDouble(TextField textField) {
        return Double.parseDouble(textField.getText());
    }
}
