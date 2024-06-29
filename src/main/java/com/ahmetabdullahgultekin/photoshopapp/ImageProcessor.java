package com.ahmetabdullahgultekin.photoshopapp;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static com.ahmetabdullahgultekin.photoshopapp.ImageProcess.*;

public class ImageProcessor {

    private ImageProcess imageProcess;
    private boolean isCompleted;
    private double totalPixels;
    private final DoubleProperty processedPixelsRate;
    private List<ImageProcess> imageProcesses;
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

    public WritableImage startProcess(String sourceFile, String destinationFile) throws IOException, ExecutionException, InterruptedException {

        System.out.println(numberOfThreads);
        long startTime, endTime;
        startTime = System.currentTimeMillis();

        // Read and assign image
        initializeImages(sourceFile);

        endTime = System.currentTimeMillis();

        assignThreads(originalImage, resultImage, numberOfThreads);

        extractImage(destinationFile);

        duration = endTime - startTime;
        System.out.println(duration);

        return resultImage;
    }

    private void initializeImages(String sourceFile) throws IOException {

        long startTime, endTime;

        File file = new File(sourceFile);
        //Image image = new Image(file.toURI().toString());
        Image image = new InputImage(file.toURI().toString());
        startTime = System.currentTimeMillis();
        //TODO High time complexity
        //originalImage = ImageIO.read(file);
        originalImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());

        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.printf("initialize image %dms%n", duration);

        resultImage = new WritableImage((int) originalImage.getWidth(), (int) originalImage.getHeight());
    }

    private void extractImage(String destinationFile) throws IOException {

        destinationFile += String.format("%s.jpg", LocalDate.now());
        //ImageIO.write(resultImage, "jpg", new File(destinationFile));
        System.out.println(resultImage.getPixelWriter().getPixelFormat().getType());
    }

    public void assignThreads(WritableImage originalImage, WritableImage resultImage, int numberOfThreads) throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        //List<Thread> threads = new ArrayList<>();
        int width = (int) originalImage.getWidth();
        int height = (int) (originalImage.getHeight() / numberOfThreads);

        for(int i = 0; i < numberOfThreads ; i++) {
            int xOrigin = 0 ;
            int yOrigin = height * i;

            Task<Void> task = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    recolourImage(originalImage, resultImage, xOrigin, yOrigin, width, height);
                    return null;
                }
            };

            executorService.submit(task);

            task.setOnSucceeded(event -> {
                isCompleted = executorService.isTerminated();
            });

            /*
            Thread thread = new Thread(task);
                    /*() -> {
                //int xOrigin = 0 ;
                //int yOrigin = height * threadMultiplier;

                this.recolourImage(originalImage, resultImage, xOrigin, yOrigin, width, height);
            });

            threads.add(thread);
            */
        }

        executorService.shutdown();


        /*
        for(Thread thread : threads) {
            thread.start();
        }

        for(Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

         */
    }

    public void recolourImage(WritableImage originalImage, WritableImage resultImage, int leftCorner,
                              int topCorner, int width, int height) {
        totalPixels = originalImage.getWidth() * originalImage.getHeight();
        for(int x = leftCorner ; x < leftCorner + width && x < originalImage.getWidth() ; x++) {
            for(int y = topCorner ; y < topCorner + height && y < originalImage.getHeight() ; y++) {

                recolourPixel(originalImage, resultImage, x , y);

                double result = (x * originalImage.getHeight() + y) / totalPixels;
                if(processedPixelsRate.getValue() < result)
                    processedPixelsRate.setValue(result);
            }
        }
    }

    public void recolourPixel(WritableImage originalImage, WritableImage resultImage, int x, int y) {

        ImagePixel imagePixel = new ImagePixel(x, y, originalImage.getPixelReader().getArgb(x, y));

        int[] RGBValues = switch (imageProcess.getName()) {
            case "Paint White to Pink" -> paintWhiteToPink(imagePixel);
            case "Traverse Image" -> traverseImage(imagePixel);
            case "Increase Brightness" -> increaseBrightness(imagePixel);
            case "Decrease Brightness" -> decreaseBrightness(imagePixel);
            case "Increase Contrast" -> increaseContrast(imagePixel);
            case "Decrease Contrast" -> decreaseContrast(imagePixel);
            case "Grayscale Image" -> grayscaleImage(imagePixel);
            case "Increase Saturation" -> increaseSaturation(imagePixel);
            case "Decrease Saturation" -> decreaseSaturation(imagePixel);
            case "Increase Hue" -> increaseHue(imagePixel);
            case "Apply Sepia Filter" -> applySepiaFilter(imagePixel);
            case "Filter To Sunset" -> filterToSunset(imagePixel);
            case "Filter To Night" -> filterToNight(imagePixel);
            case "Crop Image" -> cropImage(imagePixel);
            //case "Rotate Image" -> rotateImage(imagePixel);

            default -> throw new IllegalStateException(STR."Unexpected value: \{imageProcess.getName()}");
        };

        int newRGB = createRGB(RGBValues[0], RGBValues[1], RGBValues[2]);

        setRGB(resultImage, x, y, newRGB);
    }

    public void setRGB(WritableImage image, int x, int y, int rgb) {
        //image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
        image.getPixelWriter().setArgb(x, y, rgb);
    }

    public int createRGB(int red, int green, int blue) {
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

    public void setImageProcesses(List<ImageProcess> imageProcesses) {
        this.imageProcesses = imageProcesses;
    }

    public WritableImage getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(WritableImage originalImage) {
        this.originalImage = originalImage;
    }

    public WritableImage getResultImage() {
        return resultImage;
    }

    public void setResultImage(WritableImage resultImage) {
        this.resultImage = resultImage;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public double getTotalPixels() {
        return totalPixels;
    }

    public void setTotalPixels(double totalPixels) {
        this.totalPixels = totalPixels;
    }

    public double getProcessedPixelsRate() {
        return processedPixelsRate.get();
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
