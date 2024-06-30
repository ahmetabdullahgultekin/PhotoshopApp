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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Controller {

    //ImageProcessor class instance
    ImageProcessor imageProcessor;
    long duration, startTime, endTime;
    double imageHeight, imageWidth, imageHeightAndWidth;
    int stageWidth, stageHeight;
    File inputFile;
    private final Logger logger;

    //Directory of the source and destination files
    public final String SOURCE_FILE = "src/main/resources/com/ahmetabdullahgultekin/photoshopapp/pexels-ingo.jpg";
    public final String DESTINATION_FILE = "src/main/out/GeneratedImage";

    //GUI components
    Stage stage;
    Scene scene;
    BorderPane mainPane, inputImagePane, outputImagePane;
    Button processButton;
    ComboBox<String> comboBox;
    ProgressBar progressBar;
    Label durationLabel;
    Image inputImage, outputImage;
    ImageView inputImageView, outputImageView;
    HBox hBoxTop, hBoxCenter;
    MenuBar menuBar;
    Menu fileMenu, editMenu, helpMenu;
    MenuItem newFile, openFile, saveFile, exitFile, cropFile, aboutFile;


    //Constructor
    public Controller(Stage stage) {

        logger = LogManager.getLogger(Controller.class);
        logger.info(STR."\{Controller.class.toString()} Logger instance created!");

        imageProcessor = new ImageProcessor();
        logger.info("ImageProcessor instance created!");

        fillTheStage();
        logger.info("Stage elements get filled!");

        this.stage = stage;
        this.stage.setTitle("Photoshop App");
        this.stage.setScene(this.scene);
        this.stage.show();
        logger.info("Application started!");
    }

    //Fill the scene with GUI components
    private void fillTheStage() {

        stageWidth = 1600;
        stageHeight = 900;

        imageHeightAndWidth = 800;
        imageHeight = imageHeightAndWidth;
        imageWidth = imageHeightAndWidth;

        // Create the MenuBar
        menuBar = new MenuBar();

        // Create Menus
        fileMenu = new Menu("File");
        editMenu = new Menu("Edit");
        helpMenu = new Menu("Help");

        processButton = new Button("Process");
        processButton.setOnMouseClicked(_ -> processImage());

        comboBox = new ComboBox<>();
        comboBox.setValue(imageProcessor.getImageProcess().getName());
        comboBox.getItems().addAll(imageProcessor.getImageProcesses().stream().map(ImageProcess::getName).toList());

        durationLabel = new Label("Process took: 0 ms");
        durationLabel.getStyleClass().add("durationlabel");

        progressBar = new ProgressBar();
        progressBar.setProgress(0);

        // Create MenuItems
        newFile = new MenuItem("New");
        openFile = new MenuItem("Open");
        saveFile = getSaveFile();
        exitFile = new MenuItem("Exit");
        // Create MenuItems
        cropFile = new MenuItem("Crop");
        // Create MenuItems
        aboutFile = new MenuItem("About");

        // Add MenuItems to the File Menu
        fileMenu.getItems().addAll(newFile, openFile, saveFile, exitFile);
        editMenu.getItems().addAll(cropFile);
        helpMenu.getItems().addAll(aboutFile);

        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

        //inputImage = new Image(new FileInputStream(SOURCE_FILE));
        inputFile = new File(SOURCE_FILE);
        inputImage = new InputImage(inputFile.toURI().toString());
        inputImageView = new ImageView(inputImage);

        outputImageView = new ImageView();

        inputImageView.setFitHeight(imageHeight);
        inputImageView.setFitWidth(imageWidth);
        inputImageView.setPreserveRatio(true);

        outputImageView.setFitHeight(imageHeight);
        outputImageView.setFitWidth(imageWidth);
        outputImageView.setPreserveRatio(true);

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

        hBoxCenter = new HBox();
        hBoxCenter.getChildren().addAll(inputImageView, outputImageView);
        hBoxTop.getStyleClass().add("hboxtop");
        hBoxCenter.getStyleClass().add("hboxcenter");

        mainPane.setTop(hBoxTop);
        mainPane.setCenter(hBoxCenter);

        this.scene = new Scene(mainPane, stageWidth, stageHeight);
        scene.getStylesheets().add("com/ahmetabdullahgultekin/photoshopapp/stylesheet.css");
    }

    private MenuItem getSaveFile() {
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
                    protected Void call() {
                        imageProcessor.extractImage(DESTINATION_FILE);
                        logger.info("Image saved successfully!");
                        return null;
                    }
                };
                imageProcessor.processedPixelsRateProperty().bind(task.progressProperty());
                task.setOnSucceeded(_ -> Platform.runLater(() -> {
                    alert.setHeaderText("Image saved!");
                    alert.setContentText(STR."Saved to the destination \{DESTINATION_FILE}.");
                    imageProcessor.processedPixelsRateProperty().unbind();
                    imageProcessor.setProcessedPixelsRate(0);
                }));
                executorService.submit(task);
                executorService.shutdown();

            } catch (Exception exception) {

                logger.error("Image not saved!");
                logger.error(exception.getMessage());

                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle("Alert");
                alert.setHeaderText("Image not saved!");
                alert.setContentText("Please process the image first!");
            }
        });
        return saveFile;
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
            try {
                outputImage = imageProcessor.startProcess(SOURCE_FILE);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            long endTime = System.currentTimeMillis();

            //outputImage = new Image(new FileInputStream(DESTINATION_FILE + STR."_\{LocalDate.now()}.jpg"));

            long duration = endTime - startTime;
            logger.info("Time taken for starting process: %d ms%n", duration);

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
            logger.error(e.getMessage());
        }
    }
}
