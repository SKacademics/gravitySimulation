package ch.fadre.gravitySimulation.view;

import javafx.beans.value.ChangeListener;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Shape3D;

class CameraController implements FollowListener {

    private static final int ZOOM_MULTIPLIER = 10;
    private static final double CAMERA_DISTANCE = 150;
    private static final double SHIFT_MULTIPLIER = 10;

    private final Xform cameraXform = new Xform();
    private final Xform cameraXform2 = new Xform();
    private final Xform cameraXform3 = new Xform();

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;
    private Camera camera;

    private ChangeListener<Number> currentListener;
    private Shape3D currentShape;


    Camera createCamera() {
        camera = new PerspectiveCamera(true);
        configureNavigation(camera);
        return camera;
    }

    void handleSceneEvents(Scene scene, Camera camera) {
        handleKeyboard(scene, camera);
        handleMouse(scene, camera);
    }

    private Group configureNavigation(Camera inCamera) {
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(inCamera);

        setInitialPosition(inCamera);

        inCamera.setNearClip(0.1);
        inCamera.setFarClip(1000000);
        return cameraXform;
    }

    private void setInitialPosition(Camera inCamera) {
        cameraXform.t.setX(0.0);
        cameraXform.t.setY(0.0);
        cameraXform.t.setZ(0.0);
        cameraXform.rotateX.setAngle(40);
        cameraXform.rotateY.setAngle(320.0);
        cameraXform3.setRotateZ(180.0);
        inCamera.setTranslateZ(-CAMERA_DISTANCE);
    }

    private void handleMouse(Scene scene, Camera camera) {
        scene.setOnMousePressed(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged(me -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);

            double modifier = 1.0;
            double modifierFactor = 0.1;

            if (me.isControlDown()) {
                modifier = 0.1;
            }
            if (me.isShiftDown()) {
                modifier = 10.0;
            }
            if (me.isPrimaryButtonDown()) {
                cameraXform.rotateY.setAngle(cameraXform.rotateY.getAngle() - mouseDeltaX * modifierFactor * modifier * 2.0);  // +
                cameraXform.rotateX.setAngle(cameraXform.rotateX.getAngle() + mouseDeltaY * modifierFactor * modifier * 2.0);  // -
            } else if (me.isSecondaryButtonDown()) {
                double z = camera.getTranslateZ();
                double newZ = z + mouseDeltaX * modifierFactor * modifier;
                camera.setTranslateZ(newZ);
            } else if (me.isMiddleButtonDown()) {
                cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX * modifierFactor * modifier * 0.3);  // -
                cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY * modifierFactor * modifier * 0.3);  // -
            }
        });
        scene.setOnScroll(e -> camera.setTranslateX(Math.signum(e.getDeltaX()) * ZOOM_MULTIPLIER + camera.getTranslateX()));
        scene.setOnScroll(e -> camera.setTranslateY(Math.signum(e.getDeltaX()) * ZOOM_MULTIPLIER + camera.getTranslateY()));
        scene.setOnScroll(e -> camera.setTranslateZ(Math.signum(e.getDeltaX()) * ZOOM_MULTIPLIER + camera.getTranslateZ()));
    }

    private void handleKeyboard(Scene scene, Camera camera) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                case W:
                    if (event.isAltDown()) {
                        rotateX(event, true);
                    } else {
                        moveZ(event, camera, true);
                    }
                    break;
                case DOWN:
                case S:
                    if (event.isAltDown()) {
                        rotateX(event, false);
                    } else {
                        moveZ(event, camera, false);
                    }
                    break;
                case RIGHT:
                case D:
                    if (event.isAltDown()) {
                        rotateY(event, true);
                    } else {
                        moveX(event, false);
                    }
                    break;
                case LEFT:
                case A:
                    if (event.isAltDown()) {
                        rotateY(event, false);
                    } else {
                        moveX(event, true);
                    }
                    break;
            }
        });
    }

    private void moveX(KeyEvent event, boolean positive) {
        cameraXform2.t.setX(cameraXform2.t.getX() + getShiftMultiplier(event, positive));
    }

    private void moveZ(KeyEvent event, Camera camera, boolean positive) {
        if (event.isControlDown()) {
            cameraXform2.t.setY(cameraXform2.t.getY() + getShiftMultiplier(event, positive));
        } else {
            camera.setTranslateZ(camera.getTranslateZ() + getShiftMultiplier(event, positive));
        }

    }

    private void rotateX(KeyEvent event, boolean positive) {
        cameraXform.rotateX.setAngle(cameraXform.rotateX.getAngle() + getShiftMultiplier(event, positive));
    }

    private void rotateY(KeyEvent event, boolean positive) {
        cameraXform.rotateY.setAngle(cameraXform.rotateY.getAngle() + getShiftMultiplier(event, positive));
    }

    private double getShiftMultiplier(KeyEvent event, boolean positive) {
        double sign = positive ? 1.0 : -1.0;
        if (event.isShiftDown()) {
            return SHIFT_MULTIPLIER * sign;
        }
        return sign;
    }

    public void resetCamera() {
        setInitialPosition(camera);
    }

    @Override
    public void followWithCamera(ViewObject viewObject) {
        if (viewObject != null) {
            currentShape = viewObject.getShape();

            currentListener = createListener(currentShape);
            currentShape.translateXProperty().addListener(currentListener);
            currentShape.translateYProperty().addListener(currentListener);
            currentShape.translateZProperty().addListener(currentListener);
        }
    }

    @Override
    public void unFollow() {
        currentShape.translateXProperty().removeListener(currentListener);
        currentShape.translateYProperty().removeListener(currentListener);
        currentShape.translateZProperty().removeListener(currentListener);
    }

    private ChangeListener<Number> createListener(Shape3D shape) {
        return (observable, oldValue, newValue) -> {
            cameraXform.t.xProperty().setValue(shape.getTranslateX());
            cameraXform.t.yProperty().setValue(shape.getTranslateY());
            cameraXform.t.zProperty().setValue(shape.getTranslateZ());
        };
    }
}
