package ch.fabian.solarSystem.view;

import ch.fabian.solarSystem.SimulationController;
import ch.fabian.solarSystem.model.ModelSimulation;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape3D;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ViewBuilder implements FollowListener{

    private Camera camera;
    private Point3D positionBefore;
    private ChangeListener<Number> positionListener;

    public Scene createScene(ModelSimulation modelSimulation, SimulationController controller) throws IOException {
        Group viewRoot = createViewRoot();
        ViewSimulation viewSimulation = createViewSimulation(modelSimulation,viewRoot);
        controller.setViewSimulation(viewSimulation);

        SubScene simulationScene = createSubSceneContent(viewRoot);
        Pane simulationPane = new Pane(simulationScene);
        Pane controls = buildControls(controller);
        controls.setFocusTraversable(false);
        TitledPane selectedObjectPane = buildSelectedObjectPane(controller, viewSimulation);

        HBox hBox = new HBox();
        StackPane stackPane = new StackPane(simulationPane, selectedObjectPane);
        stackPane.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().addAll(controls, stackPane);

        Scene scene = new Scene(hBox);
        simulationScene.heightProperty().bind(scene.heightProperty());
        simulationScene.widthProperty().bind(scene.widthProperty());

        return scene;
    }

    private Pane buildControls(SimulationController controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(ViewBuilder.class.getResource("controls.fxml"));
        Pane myPane = loader.load();
        ControlsController controlsController = loader.getController();
        controlsController.setCurrentSimulation(controller);
        controlsController.addResetListener(controller::resetScene);
        controller.addParameterListener(controlsController::updateParams);
        return myPane;
    }

    private TitledPane buildSelectedObjectPane(SimulationController controller, ViewSimulation viewSimulation) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("selectedObject.fxml"));
        TitledPane myPane = loader.load();
        SelectedObjectController selectedObjectController = loader.getController();
        selectedObjectController.addFollowListener(this);
        viewSimulation.addSelectionListener(selectedObjectController);

        return myPane;
    }

    private SubScene createSubSceneContent(Group simulation3d) {
        // Build the Scene Graph
        SubScene simulationScene = new SubScene(simulation3d, 200, 200, true, SceneAntialiasing.BALANCED);
        simulationScene.setFill(Color.DARKGRAY);
        CameraController cameraController = new CameraController();
        camera = cameraController.createCamera();
        cameraController.handleSceneMouseEvents(simulationScene, camera);
        simulationScene.setCamera(camera);
        return simulationScene;
    }

    private Group createViewRoot() {
        Group simulation3d = new Group();
        simulation3d.getChildren().addAll(new AxisCreator().buildAxes());
        simulation3d.getChildren().addAll(createLights());
        return simulation3d;
    }

    private List<PointLight> createLights() {
        PointLight light1 = createPointLight(Color.BEIGE, 500, 500, 500);
        PointLight light2 = createPointLight(Color.BLANCHEDALMOND, -500, -500, -500);
        return Arrays.asList(light1, light2);
    }

    private PointLight createPointLight(Color color, int x, int z, int y) {
        PointLight light1 = new PointLight(color);
        light1.setTranslateX(x);
        light1.setTranslateY(y);
        light1.setTranslateZ(z);
        return light1;
    }

    public void followWithCamera(ViewObject viewObject){
        if(viewObject != null){
            Shape3D shape = viewObject.getShape();
            positionBefore = new Point3D(shape.getTranslateX(), shape.getTranslateY(), shape.getTranslateZ());
            positionListener = createListener(shape);
            shape.translateXProperty().addListener(positionListener);
            shape.translateYProperty().addListener(positionListener);
            shape.translateZProperty().addListener(positionListener);
        } else {
            camera.setTranslateX(positionBefore.getX());
            camera.setTranslateY(positionBefore.getY());
            camera.setTranslateZ(positionBefore.getZ());
        }
    }

    private ChangeListener<Number> createListener(Shape3D shape) {
        return (observable, oldValue, newValue) -> {
            camera.translateXProperty().setValue(shape.getTranslateX() + 10.0);
            camera.translateYProperty().setValue(shape.getTranslateY() + 10.0);
            camera.translateZProperty().setValue(shape.getTranslateZ() + 10.0);
        };
    }

    private ViewSimulation createViewSimulation(ModelSimulation modelSimulation, Group parent) {
        return new ViewSimulation(modelSimulation, parent);
    }


}
