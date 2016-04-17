// 
// Xform.java is a class that extends the Group class. It is used in the 
// MoleculeSampleApp application that is built using the Getting Started with JavaFX
// 3D Graphics tutorial. The method allows you to add your own transforms and rotation.
// 

package ch.fabian.gravitySimulation.view;

import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;


public class Xform extends Group {

    public enum RotateOrder {
        XYZ, XZY, YXZ, YZX, ZXY, ZYX
    }

    public Translate t = new Translate();
    public Translate p = new Translate();
    public Translate ip = new Translate();
    public Rotate rotateX = new Rotate();
    public Rotate rotateY = new Rotate();
    public Rotate rotateZ = new Rotate();
    public Scale scale = new Scale();

    {
        rotateX.setAxis(Rotate.X_AXIS);
        rotateY.setAxis(Rotate.Y_AXIS);
        rotateZ.setAxis(Rotate.Z_AXIS);
    }
    public Xform() {
        super();
        getTransforms().addAll(t, rotateZ, rotateY, rotateX, scale);
    }

    public Xform(RotateOrder rotateOrder) {
        super();
        // choose the order of rotations based on the rotateOrder
        switch (rotateOrder) {
            case XYZ:
                getTransforms().addAll(t, p, rotateZ, rotateY, rotateX, scale, ip);
                break;
            case XZY:
                getTransforms().addAll(t, p, rotateY, rotateZ, rotateX, scale, ip);
                break;
            case YXZ:
                getTransforms().addAll(t, p, rotateZ, rotateX, rotateY, scale, ip);
                break;
            case YZX:
                getTransforms().addAll(t, p, rotateX, rotateZ, rotateY, scale, ip);  // For Camera
                break;
            case ZXY:
                getTransforms().addAll(t, p, rotateY, rotateX, rotateZ, scale, ip);
                break;
            case ZYX:
                getTransforms().addAll(t, p, rotateX, rotateY, rotateZ, scale, ip);
                break;
        }
    }

    public void setTranslate(double x, double y, double z) {
        t.setX(x);
        t.setY(y);
        t.setZ(z);
    }

    public void setTranslate(double x, double y) {
        t.setX(x);
        t.setY(y);
    }

    // Cannot override these methods as they are final:
    // public void setTranslateX(double x) { t.setX(x); }
    // public void setTranslateY(double y) { t.setY(y); }
    // public void setTranslateZ(double z) { t.setZ(z); }
    // Use these methods instead:
    public void setTx(double x) {
        t.setX(x);
    }

    public void setTy(double y) {
        t.setY(y);
    }

    public void setTz(double z) {
        t.setZ(z);
    }

    public void setRotate(double x, double y, double z) {
        rotateX.setAngle(x);
        rotateY.setAngle(y);
        rotateZ.setAngle(z);
    }

    public void setRotateX(double x) {
        rotateX.setAngle(x);
    }

    public void setRotateY(double y) {
        rotateY.setAngle(y);
    }

    public void setRotateZ(double z) {
        rotateZ.setAngle(z);
    }

    public void setRx(double x) {
        rotateX.setAngle(x);
    }

    public void setRy(double y) {
        rotateY.setAngle(y);
    }

    public void setRz(double z) {
        rotateZ.setAngle(z);
    }

    public void setScale(double scaleFactor) {
        scale.setX(scaleFactor);
        scale.setY(scaleFactor);
        scale.setZ(scaleFactor);
    }

    public void setScale(double x, double y, double z) {
        scale.setX(x);
        scale.setY(y);
        scale.setZ(z);
    }

    // Cannot override these methods as they are final:
    // public void setScaleX(double x) { s.setX(x); }
    // public void setScaleY(double y) { s.setY(y); }
    // public void setScaleZ(double z) { s.setZ(z); }
    // Use these methods instead:
    public void setSx(double x) {
        scale.setX(x);
    }

    public void setSy(double y) {
        scale.setY(y);
    }

    public void setSz(double z) {
        scale.setZ(z);
    }

    public void setPivot(double x, double y, double z) {
        p.setX(x);
        p.setY(y);
        p.setZ(z);
        ip.setX(-x);
        ip.setY(-y);
        ip.setZ(-z);
    }

    public void reset() {
        t.setX(0.0);
        t.setY(0.0);
        t.setZ(0.0);
        rotateX.setAngle(0.0);
        rotateY.setAngle(0.0);
        rotateZ.setAngle(0.0);
        scale.setX(1.0);
        scale.setY(1.0);
        scale.setZ(1.0);
        p.setX(0.0);
        p.setY(0.0);
        p.setZ(0.0);
        ip.setX(0.0);
        ip.setY(0.0);
        ip.setZ(0.0);
    }

    public void resetTSP() {
        t.setX(0.0);
        t.setY(0.0);
        t.setZ(0.0);
        scale.setX(1.0);
        scale.setY(1.0);
        scale.setZ(1.0);
        p.setX(0.0);
        p.setY(0.0);
        p.setZ(0.0);
        ip.setX(0.0);
        ip.setY(0.0);
        ip.setZ(0.0);
    }

    public void debug() {
        System.out.println("t = (" +
                t.getX() + ", " +
                t.getY() + ", " +
                t.getZ() + ")  " +
                "r = (" +
                rotateX.getAngle() + ", " +
                rotateY.getAngle() + ", " +
                rotateZ.getAngle() + ")  " +
                "s = (" +
                scale.getX() + ", " +
                scale.getY() + ", " +
                scale.getZ() + ")  " +
                "p = (" +
                p.getX() + ", " +
                p.getY() + ", " +
                p.getZ() + ")  " +
                "ip = (" +
                ip.getX() + ", " +
                ip.getY() + ", " +
                ip.getZ() + ")");
    }
}
