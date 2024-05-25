package com.ahmetabdullahgultekin.photoshopapp;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;

public class Controller {

    //ImageProcessor class instance
    ImageProcessor imageProcessor;

    //Directory of the source and destination files
    public final String SOURCE_FILE = "src/main/resources/com/ahmetabdullahgultekin/photoshopapp/pexels-ingo.jpg";
    public final String DESTINATION_FILE = "src/main/out/GeneratedImage";

    //GUI components
    Stage stage;
    Scene scene;
    BorderPane mainPane;
    Button button;
    ComboBox<String> comboBox;
    Label label;
    Image inputImage, outputImage;
    ImageView inputImageView, outputImageView;


    //Constructor
    public Controller(Stage stage) throws IOException {

        imageProcessor = new ImageProcessor();

        fillTheStage();

        this.stage = stage;
        this.stage.setTitle("Photoshop App");
        this.stage.setScene(this.scene);
        this.stage.show();
    }

    //Fill the scene with GUI components
    private void fillTheStage() throws FileNotFoundException {

        double height, width, heightAndWidth;

        inputImage = new Image(new FileInputStream(SOURCE_FILE));
        inputImageView = new ImageView(inputImage);

        outputImageView = new ImageView();

        comboBox = new ComboBox<>();
        comboBox.setValue(imageProcessor.getImageProcess().getName());
        comboBox.getEditor().setEditable(false);
        comboBox.setMinWidth(300);
        comboBox.getItems().addAll(imageProcessor.getImageProcesses().stream().map(ImageProcess::getName).toList());

        button = new Button("Process");
        button.setOnMouseClicked(_ -> {



            try {

                imageProcessor.setImageProcess(new ImageProcess(comboBox.getValue()));

                long startTime = System.currentTimeMillis();

//                outputImage = SwingFXUtils.toFXImage(
//                        imageProcessor.startProcess(SOURCE_FILE, DESTINATION_FILE), null);

                outputImage = imageProcessor.startProcess(SOURCE_FILE, DESTINATION_FILE);

                long endTime = System.currentTimeMillis();

                //outputImage = new Image(new FileInputStream(DESTINATION_FILE + STR."_\{LocalDate.now()}.jpg"));

                long duration = endTime - startTime;
                System.out.println(STR."Time taken: \{duration} ms");

                outputImageView.setImage(outputImage);
                label.setText(STR."Process took: \{duration} ms");

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (NullPointerException ex) {
                throw new RuntimeException("Please select a process");
            }
        });

        heightAndWidth = 700;
        height = heightAndWidth;
        width = heightAndWidth;

        label = new Label("Process took: ms");

        inputImageView.setFitHeight(height);
        inputImageView.setFitWidth(width);
        inputImageView.setPreserveRatio(true);

        outputImageView.setFitHeight(height);
        outputImageView.setFitWidth(width);
        outputImageView.setPreserveRatio(true);

        mainPane = new BorderPane();

        mainPane.setLeft(inputImageView);
        mainPane.setRight(outputImageView);
        mainPane.setBottom(button);
        mainPane.setTop(comboBox);
        mainPane.setCenter(label);

        BorderPane.setAlignment(inputImageView, Pos.CENTER);
        BorderPane.setAlignment(outputImageView, Pos.CENTER);
        BorderPane.setAlignment(button, Pos.CENTER);
        BorderPane.setAlignment(comboBox, Pos.TOP_CENTER);
        BorderPane.setAlignment(label, Pos.CENTER);

        this.scene = new Scene(mainPane, 1600, 900);
        scene.getStylesheets().add("com/ahmetabdullahgultekin/photoshopapp/stylesheet.css");
    }


}
