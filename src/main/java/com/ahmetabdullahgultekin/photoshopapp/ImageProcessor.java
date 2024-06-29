package com.ahmetabdullahgultekin.photoshopapp;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ImageProcessor {

    private ImageProcess imageProcess;
    private boolean isCompleted;
    private final DoubleProperty processedPixelsRate;
    private final List<ImageProcess> imageProcesses;
    //private BufferedImage originalImage, resultImage;
    private WritableImage originalImage, resultImage;
    private long duration;
    private final int numberOfThreads = Runtime.getRuntime().availableProcessors();
            //8 : (Runtime.getRuntime().availableProcessors() * 2);

    public ImageProcessor() {
        processedPixelsRate = new SimpleDoubleProperty(0);

        imageProcess = new ImageProcess("Paint White to Pink");

        imageProcesses = new ArrayList<>();
        imageProcesses.add(new ImageProcess("Paint White to Pink"));
        imageProcesses.add(new ImageProcess("Traverse Image"));
        imageProcesses.add(new ImageProcess("Increase Brightness"));
        imageProcesses.add(new ImageProcess("Decrease Brightness"));
        imageProcesses.add(new ImageProcess("Increase Contrast"));
        imageProcesses.add(new ImageProcess("Decrease Contrast"));
        imageProcesses.add(new ImageProcess("Grayscale Image"));
        imageProcesses.add(new ImageProcess("Increase Saturation"));
        imageProcesses.add(new ImageProcess("Decrease Saturation"));
        imageProcesses.add(new ImageProcess("Increase Hue"));
        imageProcesses.add(new ImageProcess("Apply Sepia Filter"));
        imageProcesses.add(new ImageProcess("Filter To Sunset"));
        imageProcesses.add(new ImageProcess("Filter To Night"));
        imageProcesses.add(new ImageProcess("Crop Image"));
        imageProcesses.add(new ImageProcess("Rotate Image"));
    }

    WritableImage startProcess(String sourceFile, String destinationFile) throws IOException {

        System.out.println(numberOfThreads);
        long startTime, endTime;
        startTime = System.currentTimeMillis();

        // Read and assign image
        initializeImages(sourceFile);

        endTime = System.currentTimeMillis();

        startThreads();

        //extractImage(destinationFile);

        duration = endTime - startTime;
        System.out.println(duration);

        return resultImage;
    }

    private void initializeImages(String sourceFile) {

        long startTime, endTime;

        File file = new File(sourceFile);
        Image image = new InputImage(file.toURI().toString());
        startTime = System.currentTimeMillis();
        //originalImage = ImageIO.read(file);
        originalImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());

        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.printf("initialize image %dms%n", duration);

        resultImage = new WritableImage((int) originalImage.getWidth(), (int) originalImage.getHeight());
    }

    void extractImage(String destination) throws IOException {

        String newDestination = destination + String.format("%s.png", LocalDate.now());

        //BufferedImage bufferedImage = SwingFXUtils.fromFXImage(resultImage, null);
        //BufferedImage bufferedImage = new BufferedImage((int)resultImage.getWidth(), (int)resultImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        File outputFile = new File(newDestination);
        BufferedImage bImage = SwingFXUtils.fromFXImage(resultImage, null);
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

            Task<Void> task = new Task<Void>() {

                @Override
                protected Void call() {
                    recolourImage(xOrigin, yOrigin, width, height);
                    return null;
                }
            };

            executorService.submit(task);

            task.setOnSucceeded(_ -> {
                isCompleted = executorService.isTerminated();
            });
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
