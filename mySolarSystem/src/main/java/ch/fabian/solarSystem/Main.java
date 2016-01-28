package ch.fabian.solarSystem;

import ch.fabian.solarSystem.model.GravitySimulation;
import ch.fabian.solarSystem.model.ModelSimulation;
import ch.fabian.solarSystem.model.SimulationParameters;
import ch.fabian.solarSystem.model.SpaceObject;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape3D;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Main extends Application {

    private Group simulation3d;
    private ControlsController controlsController;
    private ModelSimulation modelSimulation;
    private ViewSimulation viewSimulation;

    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox hBox = new HBox();
        SubScene content = createContent();
        Pane contentPane = new Pane(content);
        Pane controls = buildControls();
        hBox.getChildren().addAll(controls, contentPane);

        Scene scene = new Scene(hBox);
        CameraCreator cameraCreator = new CameraCreator();
        Camera camera = cameraCreator.createCamera();
        content.setCamera(camera);
        cameraCreator.handleSceneMouseEvents(scene, camera);

        content.heightProperty().bind(scene.heightProperty());
        content.widthProperty().bind(scene.widthProperty());
        primaryStage.setScene(scene);
        primaryStage.setHeight(800);
        primaryStage.setWidth(1500);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        modelSimulation.stop();
        super.stop();
    }

    private SubScene createContent() {
        modelSimulation = createModelSimulation();
        viewSimulation = new ViewSimulation(modelSimulation);

        // Build the Scene Graph
        PointLight pointLight = new PointLight(Color.BEIGE);
        pointLight.setTranslateY(50);
        pointLight.setTranslateZ(50);
        pointLight.setTranslateX(50);

        simulation3d = new Group();
        simulation3d.getChildren().addAll(pointLight);
        simulation3d.getChildren().addAll(viewSimulation.getShapes());
        simulation3d.getChildren().addAll(new AxisCreator().buildAxes());
        SubScene simulationScene = new SubScene(simulation3d, 200, 200);

        simulationScene.setFill(Color.GREY);

        startSimulation();

        return simulationScene;
    }

    private void startSimulation() {
        modelSimulation.startSimulation();
        viewSimulation.startViewSimulation();
    }

    private Pane buildControls() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("controls.fxml"));
        Pane myPane = loader.load();
        controlsController = loader.getController();
        controlsController.setCurrentSimulation(modelSimulation, viewSimulation);
        controlsController.addResetListener(this::resetScene);
        return myPane;
    }

    private void resetScene() {
        stopSimulation();
        simulation3d.getChildren().removeAll(simulation3d.getChildren().stream().filter(c -> c instanceof Shape3D).collect(toList()));

        modelSimulation = createModelSimulation();
        viewSimulation = new ViewSimulation(modelSimulation);

        controlsController.setCurrentSimulation(modelSimulation, viewSimulation);

        simulation3d.getChildren().addAll(viewSimulation.getShapes());
        startSimulation();
    }

    private void stopSimulation() {
        viewSimulation.stopViewSimulation();
        modelSimulation.stop();
    }

    private ModelSimulation createModelSimulation() {
        List<SpaceObject> spaceObjects = createMany();
        //List<SpaceObject> spaceObjects = create4();

        GravitySimulation gravitySimulation = new GravitySimulation(spaceObjects);
        gravitySimulation.setSimulationParameters(new SimulationParameters(0.01, true, false));
        return new ModelSimulation(gravitySimulation);
    }

    private List<SpaceObject> create4() {
        SpaceObject object1 = createObject(-5, -5, 0, 5000);
        object1.setLastSpeed(new Point3D(0.01, 0, 0.01));
        SpaceObject object2 = createObject(5, -5, 0, 5000);
        object2.setLastSpeed(new Point3D(0.01, 0.01, 0));
        SpaceObject object3 = createObject(0, 0, 0, 5000000);
        object3.setLastSpeed(new Point3D(0, 0, 0));
        SpaceObject object4 = createObject(5, 5, 0, 5000);
        object4.setLastSpeed(new Point3D(0.02, -0.015, 0.015));
        return Arrays.asList(object1, object2, object3, object4);
    }

    private SpaceObject createObject(int inX, int inY, int inZ, int inMass) {
        SpaceObject spaceObject = new SpaceObject();
        spaceObject.setLastPosition(new Point3D(inX, inY, inZ));
        spaceObject.setMass(inMass);
        return spaceObject;
    }


    private List<SpaceObject> createMany() {
        List<SpaceObject> spaceObjects = new ArrayList<>();
        for (int i = -100; i <= 100; i += 40) {
            for (int j = -100; j <= 100; j += 40) {
                for (int k = -100; k <= 100; k += 40) {
                    SpaceObject spaceObject1 = new SpaceObject();
                    spaceObject1.setLastPosition(new Point3D(i, j, k));
                    spaceObject1.setMass(10E7);
                    spaceObject1.setLastSpeed(new Point3D(Math.random() * 0.1 - 0.05, Math.random() * 0.1 - 0.05, Math.random() * 0.1 - 0.05));
                    spaceObjects.add(spaceObject1);
                }
            }
        }
        SpaceObject spaceObject1 = createObject(new Point3D(50, 0, -50), 10E9, new Point3D(0.1, 0.0,-0.1));
        SpaceObject spaceObject2 = createObject(new Point3D(-50, -0.1, 50), 10E9, new Point3D(0.1, 0.0, 0.1));
        spaceObjects.add(spaceObject1);
        spaceObjects.add(spaceObject2);
        return spaceObjects;
    }

    private SpaceObject createObject(Point3D inLastPosition, double inMass, Point3D inLastSpeed) {
        SpaceObject spaceObject1 = new SpaceObject();
        spaceObject1.setLastPosition(inLastPosition);
        spaceObject1.setMass(inMass);
        spaceObject1.setLastSpeed(inLastSpeed);
        return spaceObject1;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
