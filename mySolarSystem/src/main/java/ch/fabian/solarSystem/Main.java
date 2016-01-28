package ch.fabian.solarSystem;

import ch.fabian.solarSystem.model.*;
import ch.fabian.solarSystem.view.*;
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

import static java.util.stream.Collectors.toList;

public class Main extends Application {


    private SimulationController simulationController;

    @Override
    public void start(Stage primaryStage) throws Exception {

        ModelSimulation modelSimulation = createModelSimulation();

        simulationController = new SimulationController(modelSimulation);
        Scene scene = new ViewBuilder().createScene(modelSimulation, simulationController);
        primaryStage.setScene(scene);
        primaryStage.setHeight(800);
        primaryStage.setWidth(1500);
        primaryStage.show();

        simulationController.startSimulation();
    }

    @Override
    public void stop() throws Exception {
        simulationController.stopSimulation();
        super.stop();
    }

    private ModelSimulation createModelSimulation() {
        List<SpaceObject> spaceObjects = new ModelFactory().createManyObjects();
        //List<SpaceObject> spaceObjects = new ModelFactory().create4Objects();

        GravitySimulation gravitySimulation = new GravitySimulation(spaceObjects);
        gravitySimulation.setSimulationParameters(new SimulationParameters(0.01, true, false));
        return new ModelSimulation(gravitySimulation);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
