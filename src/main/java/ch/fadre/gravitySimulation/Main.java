package ch.fadre.gravitySimulation;

import ch.fadre.gravitySimulation.model.*;
import ch.fadre.gravitySimulation.view.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    private SimulationController simulationController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Gravity Simulation");
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
        //List<SpaceObject> spaceObjects = new ModelFactory().create2Colliding();
        //List<SpaceObject> spaceObjects = new ModelFactory().create2NonColliding();

        SimulationParameters parameters = new SimulationParameters(0.01, true, true);
        GravitySimulation gravitySimulation = new GravitySimulation(spaceObjects, parameters);
        gravitySimulation.setSimulationParameters(parameters);
        return new ModelSimulation(gravitySimulation);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
