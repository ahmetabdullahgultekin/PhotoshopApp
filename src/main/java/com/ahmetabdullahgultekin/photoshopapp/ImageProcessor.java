package com.ahmetabdullahgultekin.photoshopapp;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ImageProcessor {

    private final Logger logger;
    private ImageProcess imageProcess;
    private boolean isCompleted;
    private final DoubleProperty processedPixelsRate;
    private final List<ImageProcess> imageProcesses;
    private WritableImage originalImage, resultImage;
    private String sourceFile;
    private long duration;
    private final int numberOfThreads = Runtime.getRuntime().availableProcessors();
            //8 : (Runtime.getRuntime().availableProcessors() * 2);

    public ImageProcessor() {

        logger = LogManager.getLogger(Controller.class);
        logger.info(STR."\{Controller.class.toString()} Logger instance created!");

        //Initial value of process
        processedPixelsRate = new SimpleDoubleProperty(0);

        //Initial value of process
        imageProcess = new ImageProcess("Paint White to Pink");

        imageProcesses = createAndListImageProcesses();
    }

    WritableImage startProcess(String source) {

        logger.info(STR."Process started with \{numberOfThreads} threads.");
        sourceFile = source;

        long startTime, endTime;
        startTime = System.currentTimeMillis();

        // Read and assign image
        initializeImages(sourceFile);

        endTime = System.currentTimeMillis();

        startThreads();

        //extractImage(destinationFile);

        duration = endTime - startTime;

        logger.info(STR."Process began in \{duration}ms");

        return resultImage;
    }

    private void initializeImages(String sourceFile) {

        long startTime, endTime;

        File file = new File(sourceFile);
        Image image = new InputImage(file.toURI().toString());
        startTime = System.currentTimeMillis();
        originalImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());

        endTime = System.currentTimeMillis();
        duration = endTime - startTime;

        logger.info("Image initialization took %dms%n", duration);

        resultImage = new WritableImage((int) originalImage.getWidth(), (int) originalImage.getHeight());
    }

    void extractImage(String destination) {

        String newDestination = destination + String.format("%s.png", LocalDate.now());

        File outputFile = new File(newDestination);
        Image image = resultImage;
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void startThreads() {

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        int width = (int) originalImage.getWidth();
        int height = (int) (originalImage.getHeight() / numberOfThreads);

        for(int i = 0; i < numberOfThreads ; i++) {
            int xOrigin = 0 ;
            int yOrigin = height * i;

            Task<Void> task = new Task<>() {

                @Override
                protected Void call() {
                    recolourImage(xOrigin, yOrigin, width, height);
                    return null;
                }
            };

            executorService.submit(task);

            task.setOnSucceeded(_ -> isCompleted = executorService.isTerminated());
        }

        executorService.shutdown();
    }

    private void recolourImage(int leftCorner, int topCorner, int width, int height) {

        double doubleWidth = originalImage.getWidth();
        double doubleHeight = originalImage.getHeight();
        double totalPixels = doubleWidth * doubleHeight;

        for(int x = leftCorner ; x < leftCorner + width && x < doubleWidth ; x++) {
            for(int y = topCorner ; y < topCorner + height && y < doubleHeight ; y++) {

                recolourPixel(x , y);

                double result = (x * doubleHeight + y) / totalPixels;
                if(processedPixelsRate.getValue() < result)
                    processedPixelsRate.setValue(result);
            }
        }
    }

    private void recolourPixel(int x, int y) {

        ImagePixel imagePixel = new ImagePixel(x, y, originalImage.getPixelReader().getArgb(x, y));

        int[] RGBValues = switch (imageProcess.getName()) {
            case "Paint White to Pink" -> ImageProcess.paintWhiteToPink(imagePixel);
            case "Traverse Image" -> ImageProcess.traverseImage(imagePixel);
            case "Increase Brightness" -> ImageProcess.increaseBrightness(imagePixel);
            case "Decrease Brightness" -> ImageProcess.decreaseBrightness(imagePixel);
            case "Increase Contrast" -> ImageProcess.increaseContrast(imagePixel);
            case "Decrease Contrast" -> ImageProcess.decreaseContrast(imagePixel);
            case "Grayscale Image" -> ImageProcess.grayscaleImage(imagePixel);
            case "Increase Saturation" -> ImageProcess.increaseSaturation(imagePixel);
            case "Decrease Saturation" -> ImageProcess.decreaseSaturation(imagePixel);
            case "Increase Hue" -> ImageProcess.increaseHue(imagePixel);
            case "Apply Sepia Filter" -> ImageProcess.applySepiaFilter(imagePixel);
            case "Filter To Sunset" -> ImageProcess.filterToSunset(imagePixel);
            case "Filter To Night" -> ImageProcess.filterToNight(imagePixel);
            case "Crop Image" -> ImageProcess.cropImage(imagePixel);
            //case "Rotate Image" -> rotateImage(imagePixel);

            default -> throw new IllegalStateException(STR."Unexpected value: \{imageProcess.getName()}");
        };

        int newRGB = createRGB(RGBValues[0], RGBValues[1], RGBValues[2]);

        setRGB(resultImage, x, y, newRGB);
    }

    private void setRGB(WritableImage image, int x, int y, int rgb) {
        //image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
        image.getPixelWriter().setArgb(x, y, rgb);
    }

    private int createRGB(int red, int green, int blue) {
        int rgb = 0;

        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;
        rgb |= 0xFF000000;

        return rgb;
    }


    private List<ImageProcess> createAndListImageProcesses() {

        List<ImageProcess> listOfImageProcesses = new ArrayList<>();

        listOfImageProcesses.add(new ImageProcess("Paint White to Pink"));
        listOfImageProcesses.add(new ImageProcess("Traverse Image"));
        listOfImageProcesses.add(new ImageProcess("Increase Brightness"));
        listOfImageProcesses.add(new ImageProcess("Decrease Brightness"));
        listOfImageProcesses.add(new ImageProcess("Increase Contrast"));
        listOfImageProcesses.add(new ImageProcess("Decrease Contrast"));
        listOfImageProcesses.add(new ImageProcess("Grayscale Image"));
        listOfImageProcesses.add(new ImageProcess("Increase Saturation"));
        listOfImageProcesses.add(new ImageProcess("Decrease Saturation"));
        listOfImageProcesses.add(new ImageProcess("Increase Hue"));
        listOfImageProcesses.add(new ImageProcess("Apply Sepia Filter"));
        listOfImageProcesses.add(new ImageProcess("Filter To Sunset"));
        listOfImageProcesses.add(new ImageProcess("Filter To Night"));
        listOfImageProcesses.add(new ImageProcess("Crop Image"));
        listOfImageProcesses.add(new ImageProcess("Rotate Image"));

        return listOfImageProcesses;
    }

    public ImageProcess getImageProcess() {
        return imageProcess;
    }

    public void setImageProcess(ImageProcess imageProcess) {
        this.imageProcess = imageProcess;
    }

    public List<ImageProcess> getImageProcesses() {
        return imageProcesses;
    }

    public DoubleProperty processedPixelsRateProperty() {
        return processedPixelsRate;
    }

    public void setProcessedPixelsRate(double processedPixelsRate) {
        this.processedPixelsRate.set(processedPixelsRate);
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
