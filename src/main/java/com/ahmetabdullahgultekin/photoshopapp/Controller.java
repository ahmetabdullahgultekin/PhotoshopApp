package com.ahmetabdullahgultekin.photoshopapp;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Controller {

    //ImageProcessor class instance
    ImageProcessor imageProcessor;
    long duration, startTime, endTime;

    //Directory of the source and destination files
    public final String SOURCE_FILE = "src/main/resources/com/ahmetabdullahgultekin/photoshopapp/pexels-ingo.jpg";
    public final String DESTINATION_FILE = "src/main/out/GeneratedImage";

    //GUI components
    Stage stage;
    Scene scene;
    BorderPane mainPane;
    Button processButton;
    ComboBox<String> comboBox;
    ProgressBar progressBar;
    Label durationLabel;
    Image inputImage, outputImage;
    ImageView inputImageView, outputImageView;
    HBox hBoxTop, hBoxCenter;


    //Constructor
    public Controller(Stage stage) {

        imageProcessor = new ImageProcessor();

        fillTheStage();

        this.stage = stage;
        this.stage.setTitle("Photoshop App");
        this.stage.setScene(this.scene);
        this.stage.show();
    }

    //Fill the scene with GUI components
    private void fillTheStage() {

        double height, width, heightAndWidth;
        heightAndWidth = 700;
        height = heightAndWidth;
        width = heightAndWidth;

        // Create the MenuBar
        MenuBar menuBar = new MenuBar();

        // Create Menus
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu helpMenu = new Menu("Help");

        processButton = new Button("Process");
        processButton.setOnMouseClicked(_ -> processImage());

        comboBox = new ComboBox<>();
        comboBox.setValue(imageProcessor.getImageProcess().getName());
        //comboBox.getEditor().setEditable(false);
        //comboBox.setMinWidth(300);
        comboBox.getItems().addAll(imageProcessor.getImageProcesses().stream().map(ImageProcess::getName).toList());

        durationLabel = new Label("Process took: 0 ms");
        durationLabel.getStyleClass().add("durationlabel");

        progressBar = new ProgressBar();
        progressBar.setProgress(0);
        //progressBar.setPrefWidth(200);
        //progressBar.setPrefHeight(40);
        //progressBar.progressProperty().bind(imageProcessor.processedPixelsRateProperty());

        // Create MenuItems
        MenuItem newFile = new MenuItem("New");
        MenuItem openFile = new MenuItem("Open");
        MenuItem saveFile = new MenuItem("Save");

        saveFile.setOnAction(_ -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Saving Image...");
            Platform.runLater(alert::show);

            try {
                if(outputImage == null) {
                    throw new IOException("Output image is null!");
                }
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                Task<Void> task = new Task<>() {
                    @Override
                    protected Void call() throws IOException {
                        imageProcessor.extractImage(DESTINATION_FILE);
                        System.out.println("Image saved successfully!");
                        return null;
                    }
                };
                imageProcessor.processedPixelsRateProperty().bind(task.progressProperty());
                task.setOnSucceeded(_ -> {
                    Platform.runLater(() -> {
                        alert.setHeaderText("Image saved!");
                        alert.setContentText(STR."Saved to the destination \{DESTINATION_FILE}.");
                        imageProcessor.processedPixelsRateProperty().unbind();
                        imageProcessor.setProcessedPixelsRate(0);
                    });
                });
                executorService.submit(task);
                executorService.shutdown();
                //imageProcessor.extractImage(DESTINATION_FILE);
                //System.out.println("Image saved successfully!");

            } catch (Exception exception) {

                exception.printStackTrace();
                System.out.println("Image not saved!");

                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle("Alert");
                alert.setHeaderText("Image not saved!");
                alert.setContentText("Please process the image first!");
            }
        });
        MenuItem exitFile = new MenuItem("Exit");
        // Create MenuItems
        MenuItem cropFile = new MenuItem("Crop");
        // Create MenuItems
        MenuItem aboutFile = new MenuItem("About");

        // Add MenuItems to the File Menu
        fileMenu.getItems().addAll(newFile, openFile, saveFile, exitFile);
        editMenu.getItems().addAll(cropFile);
        helpMenu.getItems().addAll(aboutFile);

        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

        //inputImage = new Image(new FileInputStream(SOURCE_FILE));
        File file = new File(SOURCE_FILE);
        inputImage = new InputImage(file.toURI().toString());
        inputImageView = new ImageView(inputImage);

        outputImageView = new ImageView();

        inputImageView.setFitHeight(height);
        inputImageView.setFitWidth(width);
        inputImageView.setPreserveRatio(true);
        inputImageView.getStyleClass().add("inputimageview");

        outputImageView.setFitHeight(height);
        outputImageView.setFitWidth(width);
        outputImageView.setPreserveRatio(true);
        outputImageView.getStyleClass().add("outputimageview");


        imageProcessor.processedPixelsRateProperty().addListener((_, _, newValue) -> {

            progressBar.setProgress(newValue.doubleValue());

            //If the progress is 100% then set the endTime and calculate the duration
            if (newValue.doubleValue() >= 0.99999995) {
                endTime = System.currentTimeMillis();
                duration = endTime - startTime;

                Platform.runLater(() -> {
                    imageProcessor.setCompleted(true);
                    outputImageView.setImage(outputImage);
                    durationLabel.setText(String.format("Process took: %d ms", duration));
                    processButton.setDisable(false);
                    imageProcessor.setProcessedPixelsRate(0);
                });
            }
        });

        mainPane = new BorderPane();

        hBoxTop = new HBox();
        hBoxTop.getChildren().addAll(menuBar, comboBox, processButton, progressBar, durationLabel);
        //hBoxTop.setStyle("-fx-background-color: #0078d7;");

        hBoxCenter = new HBox();
        hBoxCenter.getChildren().addAll(inputImageView, outputImageView);
        hBoxTop.getStyleClass().add("hboxtop");
        hBoxCenter.getStyleClass().add("hboxcenter");

        mainPane.setTop(hBoxTop);
        mainPane.setCenter(hBoxCenter);

        this.scene = new Scene(mainPane, 1600, 900);
        scene.getStylesheets().add("com/ahmetabdullahgultekin/photoshopapp/stylesheet.css");
    }

    private void processImage() {

        //Set the imageProcessor to not completed and disable the processButton
        imageProcessor.setCompleted(false);
        processButton.setDisable(true);

        //Start the processing animation
        startProcessingAnimation();

        //Set the start time and processed pixels rate to 0
        duration = 0;
        startTime = System.currentTimeMillis();
        imageProcessor.setProcessedPixelsRate(0);
        try {

            imageProcessor.setImageProcess(new ImageProcess(comboBox.getValue()));

            long startTime = System.currentTimeMillis();

//                outputImage = SwingFXUtils.toFXImage(
//                        imageProcessor.startProcess(SOURCE_FILE, DESTINATION_FILE), null);
            try {
                outputImage = imageProcessor.startProcess(SOURCE_FILE, DESTINATION_FILE);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            long endTime = System.currentTimeMillis();

            //outputImage = new Image(new FileInputStream(DESTINATION_FILE + STR."_\{LocalDate.now()}.jpg"));

            long duration = endTime - startTime;
            System.out.printf("Time taken: %d ms%n", duration);

        } catch (NullPointerException ex) {
            throw new NullPointerException("!!!!!!!!!!!!");
        } catch (IndexOutOfBoundsException ex) {
            throw new IndexOutOfBoundsException("??????????????");
        }
    }

    private void startProcessingAnimation() {

        String[] text = {"Processing", "Processing .", "Processing . .", "Processing . . ."};

        Thread thread = new Thread(() -> {
            while (!imageProcessor.isCompleted()) {
                method(text[0]);
                if (imageProcessor.isCompleted())
                    break;
                method(text[1]);
                if (imageProcessor.isCompleted())
                    break;
                method(text[2]);
                if (imageProcessor.isCompleted())
                    break;
                method(text[3]);
            }
        });

        thread.start();
    }

    private void method(String text) {
        Platform.runLater(() -> durationLabel.setText(text));

        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
