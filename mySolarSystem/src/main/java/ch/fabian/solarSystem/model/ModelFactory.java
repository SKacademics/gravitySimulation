package ch.fabian.solarSystem.model;

import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelFactory {

    public List<SpaceObject> create2Colliding(){
        SpaceObject o1 = createObject(100,0,0,100);
        o1.setLastSpeed(new Point3D(-0.02,0,0));
        SpaceObject o2 = createObject(0,0,0,1000);
        return Arrays.asList(o1, o2);
    }

    public List<SpaceObject> create2NonColliding(){
        SpaceObject o1 = createObject(100,0,0,1000);
        o1.setLastSpeed(new Point3D(-0.002,0.001,0));
        SpaceObject o2 = createObject(0,0,0, 1000000);
        return Arrays.asList(o1, o2);
    }

    public List<SpaceObject> create4Objects() {
        SpaceObject object1 = createObject(-50, -50, 0, 5000);
        object1.setLastSpeed(new Point3D(0.005, 0, 0.001));
        SpaceObject object2 = createObject(50, -50, 0, 5000);
        object2.setLastSpeed(new Point3D(0.001, 0.001, 0.005));
        SpaceObject object3 = createObject(0, 0, 0, 5000000);
        object3.setRadius(4);
        object3.setLastSpeed(new Point3D(0, 0, 0));
        SpaceObject object4 = createObject(50, 50, 0, 5000);
        object4.setLastSpeed(new Point3D(0.001, -0.0015, 0.005));
        SpaceObject object5 = createObject(50, 0, 50, 5000);
        object5.setLastSpeed(new Point3D(-0.001, 0.0015, 0.0035));
        return Arrays.asList(object1, object2, object3, object4, object5);
    }

    public List<SpaceObject> createManyObjects() {
        List<SpaceObject> spaceObjects = new ArrayList<>();
        int range = 100;
        for (int i = -range; i <= range; i += 40) {
            for (int j = -range; j <= range; j += 40) {
                for (int k = -range; k <= range; k += 40) {
                    SpaceObject spaceObject1 = new SpaceObject(1);
                    spaceObject1.setLastPosition(new Point3D(i, j, k));
                    spaceObject1.setMass(10E7);
                    spaceObject1.setLastSpeed(new Point3D(Math.random() * 0.1 - 0.05, Math.random() * 0.1 - 0.05, Math.random() * 0.1 - 0.05));
                    spaceObjects.add(spaceObject1);
                }
            }
        }
        SpaceObject spaceObject1 = createObject(new Point3D(50, 0, -50), 10E9, new Point3D(0.2, 0.175,-0.12),2);
        SpaceObject spaceObject2 = createObject(new Point3D(-50, -0.1, 50), 10E9, new Point3D(-0.3, -0.2, 0.13),2);
        SpaceObject spaceObject3 = createObject(new Point3D(50, -0.1, 50), 10E8, new Point3D(-0.15, 0.2, 0.13), 1.5);
        spaceObjects.add(spaceObject1);
        spaceObjects.add(spaceObject2);
        spaceObjects.add(spaceObject3);
        return spaceObjects;
    }


    private SpaceObject createObject(int inX, int inY, int inZ, int inMass) {
        SpaceObject spaceObject = new SpaceObject();
        spaceObject.setLastPosition(new Point3D(inX, inY, inZ));
        spaceObject.setMass(inMass);
        return spaceObject;
    }

    private SpaceObject createObject(Point3D inLastPosition, double inMass, Point3D inLastSpeed) {
        return createObject(inLastPosition, inMass, inLastSpeed, 1);
    }

    private SpaceObject createObject(Point3D inLastPosition, double inMass, Point3D inLastSpeed, double radius) {
        SpaceObject spaceObject1 = new SpaceObject(radius);
        spaceObject1.setLastPosition(inLastPosition);
        spaceObject1.setMass(inMass);
        spaceObject1.setLastSpeed(inLastSpeed);
        return spaceObject1;
    }
}
