package org.aoc2021.Day22;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class CuboidVisualizer extends Application {
    private static final double CAMERA_INITIAL_DISTANCE = -200;
    private static final double CAMERA_INITIAL_X_ANGLE = 0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static final double ROTATION_SPEED = 0.2;
    private static final double SCROLL_SPEED = 2;
    private static final double TRANSLATION_SPEED = 10.0;
    private static final double SCALE_MULT = 1000;
    private PerspectiveCamera camera;
    private final Group root = new Group();
    private final Group boxGroup = new Group();
    private final Rotate rotateX = new Rotate(CAMERA_INITIAL_X_ANGLE, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(CAMERA_INITIAL_Y_ANGLE, Rotate.Y_AXIS);
    private final Translate translate = new Translate(0, 0, CAMERA_INITIAL_DISTANCE);
    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final Rotate rotationX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotationY = new Rotate(0, Rotate.Y_AXIS);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        for(Cuboid cuboid : Day22.cuboidStatic){
            Box box = new Box(cuboid.deltaX / SCALE_MULT, cuboid.deltaY/SCALE_MULT, cuboid.deltaZ/SCALE_MULT);
            box.setTranslateX(cuboid.origin.getX() / SCALE_MULT);
            box.setTranslateY(cuboid.origin.getY() / SCALE_MULT);
            box.setTranslateZ(cuboid.origin.getZ() / SCALE_MULT);
            box.setMaterial(cuboid.GetMaterial());
            boxGroup.getChildren().add(box);
        }
        root.getChildren().add(boxGroup);

        // Create a SubScene and add it to the Scene.
        SubScene subScene = new SubScene(root, 1200, 800, false, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.TRANSPARENT);
        subScene.setCamera(camera);

        Scene scene = new Scene(new Group(subScene), 1200, 800);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = rotationX.getAngle();
            anchorAngleY = rotationY.getAngle();
        });

        scene.setOnMouseDragged(event -> {
            double angleX = anchorAngleX - (event.getSceneY() - anchorY) / 10.0;
            double angleY = anchorAngleY + (event.getSceneX() - anchorX) / 10.0;
            rotationX.setAngle(angleX);
            rotationY.setAngle(angleY);
        });

        // Set up camera.
        camera = new PerspectiveCamera(true);
        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.getTransforms().addAll(rotateX, rotateY, translate);
        subScene.setCamera(camera);


        //Zoom in or out when scrolling
        subScene.setOnScroll(event -> {
            double delta = event.getDeltaY();
            camera.setTranslateZ(camera.getTranslateZ() + delta * SCROLL_SPEED);
        });

        // Handle arrow key events to move camera.
        subScene.setOnKeyPressed((event) -> {
            switch (event.getCode()) {
                case UP:
                    translate.setY(translate.getY() - TRANSLATION_SPEED);
                    break;
                case DOWN:
                    translate.setY(translate.getY() + TRANSLATION_SPEED);
                    break;
                case LEFT:
                    translate.setX(translate.getX() - TRANSLATION_SPEED);
                    break;
                case RIGHT:
                    translate.setX(translate.getX() + TRANSLATION_SPEED);
                    break;
                default:
                    break;
            }
        });

        boxGroup.getTransforms().addAll(rotationX, rotationY);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Focus the SubScene to enable arrow key events.
        subScene.requestFocus();
    }
}
