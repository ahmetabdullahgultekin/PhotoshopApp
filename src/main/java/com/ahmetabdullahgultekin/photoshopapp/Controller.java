package com.ahmetabdullahgultekin.photoshopapp;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;

public class AppScene {

    Stage stage;
    Scene scene;
    Pane pane;
    Button button;
    Image inputImage;
    Image outputImage;


    public AppScene(Stage stage) {

        fillScene();
        this.scene = new Scene(pane, 1600, 900);
        this.stage = stage;

        this.stage.setTitle("Photoshop App");
        this.stage.setScene(this.scene);
        this.stage.show();
    }

    private void fillScene() {

        this.pane = new Pane();
        button = new Button("Click me");
        button.setLayoutX(700);
        button.setLayoutY(400);
        button.setOnMouseClicked(e -> {

            System.out.println(STR."Button clicked\{e.getEventType()}\{e.getClickCount()}");
        });
        pane.getChildren().add(button);
    }


}
