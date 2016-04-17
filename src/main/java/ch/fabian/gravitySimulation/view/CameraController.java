package ch.fabian.gravitySimulation.view;

import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;

public class CameraController {

    private static final int ZOOM_MULTIPLIER = 10;
    private static final double CAMERA_DISTANCE = 150;
    private static final double ALT_MULTIPLIER = 0.5;
    private static final double SHIFT_MULTIPLIER = 0.2;
    private static final double CONTROL_MULTIPLIER = 0.2;

    private final Xform cameraXform = new Xform();
    private final Xform cameraXform2 = new Xform();
    private final Xform cameraXform3 = new Xform();

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;

    public Camera createCamera() {
        Camera camera = new PerspectiveCamera(true);
        configureNavigation(camera);
        return camera;
    }

    public void handleSceneMouseEvents(SubScene scene, Camera camera) {
        handleKeyboard(scene, camera);
        handleMouse(scene, camera);
    }


    private Group configureNavigation(Camera inCamera) {
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(inCamera);

        cameraXform.rotateX.setAngle(40);
        cameraXform.rotateY.setAngle(320.0);
        cameraXform3.setRotateZ(180.0);

        inCamera.setNearClip(0.1);
        inCamera.setFarClip(1000000);
        inCamera.setTranslateZ(-CAMERA_DISTANCE);
        return cameraXform;
    }

    private void handleMouse(SubScene scene, Camera camera) {
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

    private void handleKeyboard(SubScene scene, Camera camera) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    if (event.isControlDown() && event.isShiftDown()) {
                        cameraXform2.t.setY(cameraXform2.t.getY() - 10.0 * CONTROL_MULTIPLIER);
                    } else if (event.isAltDown() && event.isShiftDown()) {
                        cameraXform.rotateX.setAngle(cameraXform.rotateX.getAngle() - 10.0 * ALT_MULTIPLIER);
                    } else if (event.isControlDown()) {
                        cameraXform2.t.setY(cameraXform2.t.getY() - 1.0 * CONTROL_MULTIPLIER);
                    } else if (event.isAltDown()) {
                        cameraXform.rotateX.setAngle(cameraXform.rotateX.getAngle() - 2.0 * ALT_MULTIPLIER);
                    } else if (event.isShiftDown()) {
                        double z = camera.getTranslateZ();
                        double newZ = z + 5.0 * SHIFT_MULTIPLIER;
                        camera.setTranslateZ(newZ);
                    }
                    break;
                case DOWN:
                    if (event.isControlDown() && event.isShiftDown()) {
                        cameraXform2.t.setY(cameraXform2.t.getY() + 10.0 * CONTROL_MULTIPLIER);
                    } else if (event.isAltDown() && event.isShiftDown()) {
                        cameraXform.rotateX.setAngle(cameraXform.rotateX.getAngle() + 10.0 * ALT_MULTIPLIER);
                    } else if (event.isControlDown()) {
                        cameraXform2.t.setY(cameraXform2.t.getY() + 1.0 * CONTROL_MULTIPLIER);
                    } else if (event.isAltDown()) {
                        cameraXform.rotateX.setAngle(cameraXform.rotateX.getAngle() + 2.0 * ALT_MULTIPLIER);
                    } else if (event.isShiftDown()) {
                        double z = camera.getTranslateZ();
                        double newZ = z - 5.0 * SHIFT_MULTIPLIER;
                        camera.setTranslateZ(newZ);
                    }
                    break;
                case RIGHT:
                    if (event.isControlDown() && event.isShiftDown()) {
                        cameraXform2.t.setX(cameraXform2.t.getX() + 10.0 * CONTROL_MULTIPLIER);
                    } else if (event.isAltDown() && event.isShiftDown()) {
                        cameraXform.rotateY.setAngle(cameraXform.rotateY.getAngle() - 10.0 * ALT_MULTIPLIER);
                    } else if (event.isControlDown()) {
                        cameraXform2.t.setX(cameraXform2.t.getX() + 1.0 * CONTROL_MULTIPLIER);
                    } else if (event.isAltDown()) {
                        cameraXform.rotateY.setAngle(cameraXform.rotateY.getAngle() - 2.0 * ALT_MULTIPLIER);
                    }
                    break;
                case LEFT:
                    if (event.isControlDown() && event.isShiftDown()) {
                        cameraXform2.t.setX(cameraXform2.t.getX() - 10.0 * CONTROL_MULTIPLIER);
                    } else if (event.isAltDown() && event.isShiftDown()) {
                        cameraXform.rotateY.setAngle(cameraXform.rotateY.getAngle() + 10.0 * ALT_MULTIPLIER);  // -
                    } else if (event.isControlDown()) {
                        cameraXform2.t.setX(cameraXform2.t.getX() - 1.0 * CONTROL_MULTIPLIER);
                    } else if (event.isAltDown()) {
                        cameraXform.rotateY.setAngle(cameraXform.rotateY.getAngle() + 2.0 * ALT_MULTIPLIER);  // -
                    }
                    break;
            }
        });
    }
}
