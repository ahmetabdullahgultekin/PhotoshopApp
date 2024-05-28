package com.ahmetabdullahgultekin.photoshopapp;


import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.ahmetabdullahgultekin.photoshopapp.ImageProcess.paintWhiteToPink;
import static com.ahmetabdullahgultekin.photoshopapp.ImageProcess.traverseImage;
import static java.lang.StringTemplate.STR;

public class ImageProcessor {

    private ImageProcess imageProcess;
    private List<ImageProcess> imageProcesses;
    //private BufferedImage originalImage, resultImage;
    private WritableImage originalImage, resultImage;
    private long duration;
    private final int numberOfThreads = Runtime.getRuntime().availableProcessors() * 2;
            //8 : (Runtime.getRuntime().availableProcessors() * 2);

    public ImageProcessor() {
        imageProcess = new ImageProcess("Paint White to Pink");

        imageProcesses = new ArrayList<>();
        imageProcesses.add(new ImageProcess("Paint White to Pink"));
        imageProcesses.add(new ImageProcess("Traverse Image"));
    }

    public WritableImage startProcess(String sourceFile, String destinationFile) throws IOException {

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
        Image image = new Image(file.toURI().toString());
        startTime = System.currentTimeMillis();
        //TODO High time complexity
        //originalImage = ImageIO.read(file);
        originalImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());

        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println(String.format("initialize image %dms", duration));

        resultImage = new WritableImage((int) originalImage.getWidth(), (int) originalImage.getHeight());
    }

    private void extractImage(String destinationFile) throws IOException {

        destinationFile += String.format("%s.jpg", LocalDate.now());
        //ImageIO.write(resultImage, "jpg", new File(destinationFile));
        System.out.println(resultImage.getPixelWriter().getPixelFormat().getType());
    }

    public void assignThreads(WritableImage originalImage, WritableImage resultImage, int numberOfThreads) {
        List<Thread> threads = new ArrayList<>();
        int width = (int) originalImage.getWidth();
        int height = (int) (originalImage.getHeight() / numberOfThreads);

        for(int i = 0; i < numberOfThreads ; i++) {
            final int threadMultiplier = i;

            Thread thread = new Thread(() -> {
                int xOrigin = 0 ;
                int yOrigin = height * threadMultiplier;

                this.recolourImage(originalImage, resultImage, xOrigin, yOrigin, width, height);
            });

            threads.add(thread);
        }

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
    }

    public void recolourImage(WritableImage originalImage, WritableImage resultImage, int leftCorner,
                              int topCorner, int width, int height) {
        for(int x = leftCorner ; x < leftCorner + width && x < originalImage.getWidth() ; x++) {
            for(int y = topCorner ; y < topCorner + height && y < originalImage.getHeight() ; y++) {
                recolourPixel(originalImage, resultImage, x , y);
            }
        }
    }

    public void recolourPixel(WritableImage originalImage, WritableImage resultImage, int x, int y) {

        ImagePixel imagePixel = new ImagePixel(originalImage.getPixelReader().getArgb(x, y));

        int[] RGBValues = switch (imageProcess.getName()) {
            case "Paint White to Pink" -> paintWhiteToPink(imagePixel);
            case "Traverse Image" -> traverseImage(imagePixel);
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
}
