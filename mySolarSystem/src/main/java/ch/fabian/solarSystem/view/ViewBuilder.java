package ch.fabian.solarSystem.view;

import ch.fabian.solarSystem.IResetListener;
import ch.fabian.solarSystem.SimulationController;
import ch.fabian.solarSystem.model.ModelSimulation;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.IOException;

public class ViewBuilder {

    public Scene createScene(ModelSimulation modelSimulation, SimulationController controller) throws IOException {
        Group viewRoot = createViewRoot();
        ViewSimulation viewSimulation = createViewSimulation(modelSimulation,viewRoot);
        controller.setViewSimulation(viewSimulation);

        SubScene simulationScene = createSubSceneContent(viewRoot);
        Pane simulationPane = new Pane(simulationScene);
        Pane controls = buildControls(controller);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(controls, simulationPane);

        Scene scene = new Scene(hBox);
        CameraCreator cameraCreator = new CameraCreator();
        Camera camera = cameraCreator.createCamera();
        cameraCreator.handleSceneMouseEvents(scene, camera);
        simulationScene.setCamera(camera);

        simulationScene.heightProperty().bind(scene.heightProperty());
        simulationScene.widthProperty().bind(scene.widthProperty());

        return scene;
    }

    private Pane buildControls(SimulationController controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("controls.fxml"));
        Pane myPane = loader.load();
        ControlsController controlsController = loader.getController();
        controlsController.setCurrentSimulation(controller);
        controlsController.addResetListener(controller::resetScene);
        controller.addParameterListener(controlsController::updateParams);
        return myPane;
    }



    private SubScene createSubSceneContent(Group simulation3d) {
        // Build the Scene Graph
        SubScene simulationScene = new SubScene(simulation3d, 200, 200);
        simulationScene.setFill(Color.DARKGRAY);
        return simulationScene;
    }

    private Group createViewRoot() {
        Group simulation3d = new Group();
        simulation3d.getChildren().addAll(new AxisCreator().buildAxes());
        simulation3d.getChildren().addAll(createLight());
        return simulation3d;
    }

    private PointLight createLight() {
        PointLight pointLight = new PointLight(Color.BEIGE);
        pointLight.setTranslateY(50);
        pointLight.setTranslateZ(50);
        pointLight.setTranslateX(50);
        return pointLight;
    }

    private ViewSimulation createViewSimulation(ModelSimulation modelSimulation, Group parent) {
        return new ViewSimulation(modelSimulation, parent);
    }


}
