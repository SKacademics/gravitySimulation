package ch.fabian.gravitySimulation.model;

import javafx.geometry.Point3D;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class GravitySimulationStrongCollisionsTest {

    private GravitySimulation gravitySimulation;
    private SpaceObject spaceObject1;
    private SpaceObject spaceObject2;

    @Before
    public void setUp() throws Exception {
        spaceObject1 = createObject(0, 0, 0, 10E6, 1);
        spaceObject2 = createObject(0, 0, 0.5, 1E6, 1);
        SimulationParameters simulationParameters = createParameters();
        gravitySimulation = new GravitySimulation(Arrays.asList(spaceObject1, spaceObject2), simulationParameters);
    }

    private SimulationParameters createParameters() {
        return new SimulationParameters(0.1, true, false);
    }

    @Test
    public void testComputeGravitationalForce() throws Exception {

        double force = gravitySimulation.computeGravityBetweenBodies(spaceObject1, spaceObject2);

        //if bodies are close together, ignore force
        assertThat(force, is(0.0));
    }


    private GravitySimulation createNewSimulation(SpaceObject spaceObject1, SpaceObject spaceObject2) {
        return new GravitySimulation(Arrays.asList(spaceObject1, spaceObject2), createParameters());
    }

    @Test
    public void testGravitationalVectorDirection() throws Exception {
        SpaceObject spaceObject1 = createObject(0, 0, 0, 1);
        SpaceObject spaceObject2 = createObject(-1, 0, 0, 1);
        Point3D forceVector = createNewSimulation(spaceObject1, spaceObject2).computeGravityVector(spaceObject1);

        assertThat(forceVector.getX(), is(-0.0));
        assertThat(forceVector.getY(), is(0.0));
        assertThat(forceVector.getZ(), is(0.0));
    }

    @Test
    public void testLargerBodies_Collision() throws Exception {
        SpaceObject spaceObject1 = createObject(0, 0, 0, 2, 2);
        SpaceObject spaceObject2 = createObject(2, 0, 0, 2, 1);
        GravitySimulation gravitySimulation = createNewSimulation(spaceObject1, spaceObject2);
        Point3D forceVector = gravitySimulation.computeGravityVector(spaceObject1);

        assertThat(forceVector.getX(), is(0.0));
        assertThat(forceVector.getY(), is(0.0));
        assertThat(forceVector.getZ(), is(0.0));
    }

    @Test
    public void testLargerBodies_NoCollision() throws Exception {
        SpaceObject spaceObject1 = createObject(0, 0, 0, 10E7, 2);
        SpaceObject spaceObject2 = createObject(4, 0, 0, 10E7, 1);
        GravitySimulation gravitySimulation = createNewSimulation(spaceObject1, spaceObject2);
        Point3D forceVector = gravitySimulation.computeGravityVector(spaceObject1);

        assertThat(forceVector.getX(), is(0.0041712500000000005));
        assertThat(forceVector.getY(), is(0.0));
        assertThat(forceVector.getZ(), is(0.0));
    }

    @Test
    public void testGravitationalVectorRadius1Bodies() throws Exception {
        SpaceObject spaceObject1 = createObject(0, 0, 0, 5);
        SpaceObject spaceObject2 = createObject(3, 0, 0, 6);
        Point3D forceVector = createNewSimulation(spaceObject1, spaceObject2).computeGravityVector(spaceObject1);

        assertThat(forceVector.getX(), is(4.4493333333333343E-10));
        assertThat(forceVector.getY(), is(0.0));
        assertThat(forceVector.getZ(), is(0.0));
    }

    private SpaceObject createObject(double inX, double inY, double inZ, double inMass) {
        return createObject(inX, inY, inZ, inMass, 1);
    }

    private SpaceObject createObject(double inX, double inY, double inZ, double inMass, int radius) {
        SpaceObject spaceObject1 = new SpaceObject(radius);
        spaceObject1.setLastPosition(new Point3D(inX, inY, inZ));
        spaceObject1.setMass(inMass);
        return spaceObject1;
    }
}