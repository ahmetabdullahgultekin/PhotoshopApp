package com.ahmetabdullahgultekin.photoshopapp;

import java.awt.image.BufferedImage;

import static java.awt.Color.HSBtoRGB;
import static java.awt.Color.RGBtoHSB;

public class ImageProcess {

    private String name;

    //Constructor
    public ImageProcess(String imageProcess) {
        this.name = imageProcess;
    }

    public static int[] paintWhiteToPink(ImagePixel imagePixel) {

        int red = imagePixel.getRedValue(), green = imagePixel.getGreenValue(), blue = imagePixel.getBlueValue();

        int newRed = red, newGreen = green, newBlue = blue;

        if (isShadeOfGray(imagePixel.getRedValue(), imagePixel.getGreenValue(), imagePixel.getBlueValue())) {
            newRed = Math.min(255, red + 10); //default +10
            newGreen = Math.max(0, green - 80); //default -80
            newBlue = Math.max(0, blue - 20); //default -20
        }

        return new int[]{newRed, newGreen, newBlue};
    }

    //Traverse the Colours
    public static int[] traverseImage(ImagePixel imagePixel) {

        int red = imagePixel.getRedValue(), green = imagePixel.getGreenValue(), blue = imagePixel.getBlueValue();

        int newRed = 255 - red;
        int newGreen = 255 - green;
        int newBlue = 255 - blue;

        return new int[]{newRed, newGreen, newBlue};
    }

    public static int[] increaseBrightness(ImagePixel imagePixel) {

        int red = imagePixel.getRedValue(), green = imagePixel.getGreenValue(), blue = imagePixel.getBlueValue();

        int newRed = Math.min(255, red + 10);
        int newGreen = Math.min(255, green + 10);
        int newBlue = Math.min(255, blue + 10);

        return new int[]{newRed, newGreen, newBlue};
    }

    public static int[] decreaseBrightness(ImagePixel imagePixel) {

        int red = imagePixel.getRedValue(), green = imagePixel.getGreenValue(), blue = imagePixel.getBlueValue();

        int newRed = Math.max(0, red - 10);
        int newGreen = Math.max(0, green - 10);
        int newBlue = Math.max(0, blue - 10);

        return new int[]{newRed, newGreen, newBlue};
    }

    public static int[] increaseContrast(ImagePixel imagePixel) {

        int red = imagePixel.getRedValue(), green = imagePixel.getGreenValue(), blue = imagePixel.getBlueValue();

        int newRed = Math.min(255, red + 50);
        int newGreen = Math.min(255, green + 50);
        int newBlue = Math.min(255, blue + 50);

        return new int[]{newRed, newGreen, newBlue};
    }

    public static int[] decreaseContrast(ImagePixel imagePixel) {

        int red = imagePixel.getRedValue(), green = imagePixel.getGreenValue(), blue = imagePixel.getBlueValue();

        int newRed = Math.max(0, red - 50);
        int newGreen = Math.max(0, green - 50);
        int newBlue = Math.max(0, blue - 50);

        return new int[]{newRed, newGreen, newBlue};
    }

    public static double[] grayscaleImage(ImagePixel imagePixel) {

        int red = imagePixel.getRedValue(), green = imagePixel.getGreenValue(), blue = imagePixel.getBlueValue();

        double luminosity = (0.299 * red + 0.587 * green + 0.114 * blue);

        return new double[]{luminosity, luminosity, luminosity};
    }

    public static double[] reduceSaturation(ImagePixel imagePixel) {

        int red = imagePixel.getRedValue(), green = imagePixel.getGreenValue(), blue = imagePixel.getBlueValue();

        double average = (red + green + blue) / 3.0;

        return new double[]{average, average, average};
    }

    public static double[] increaseSaturation(ImagePixel imagePixel) {

        int red = imagePixel.getRedValue(), green = imagePixel.getGreenValue(), blue = imagePixel.getBlueValue();

        int max = Math.max(red, Math.max(green, blue));
        int min = Math.min(red, Math.min(green, blue));
        double average = (red + green + blue) / 3.0;

        double newRed = average + (max - average) * 2,
                newGreen = average + (max - average) * 2,
                newBlue = average + (max - average) * 2;

        return new double[]{newRed, newGreen, newBlue};
    }

    public static int[] adjustHue(ImagePixel imagePixel) {

        int red = imagePixel.getRedValue(), green = imagePixel.getGreenValue(), blue = imagePixel.getBlueValue();

        float[] hsb = new float[3];
        RGBtoHSB(red, green, blue, hsb);
        hsb[0] = (hsb[0] + 0.1f) % 1.0f;
        int newRGB = HSBtoRGB(hsb[0], hsb[1], hsb[2]);

        imagePixel.setRGB(newRGB);

        int newRed = imagePixel.getRedValue(),
                newGreen = imagePixel.getGreenValue(),
                newBlue = imagePixel.getBlueValue();

        return new int[]{newRed, newGreen, newBlue};
    }

    public static double[] applySepiaFilter(ImagePixel imagePixel) {

        int red = imagePixel.getRedValue(), green = imagePixel.getGreenValue(), blue = imagePixel.getBlueValue();

        double sepiaRed = (0.393 * red + 0.769 * green + 0.189 * blue),
                sepiaGreen = (0.349 * red + 0.686 * green + 0.168 * blue),
                sepiaBlue = (0.272 * red + 0.534 * green + 0.131 * blue);

        double newRed = Math.min(255, sepiaRed + 50),
                newGreen = Math.min(255, sepiaGreen + 50),
                newBlue = Math.min(255, sepiaBlue + 50);

        return new double[]{newRed, newGreen, newBlue};
    }

    public static double[] filterToSunset(ImagePixel imagePixel) {

        int red = imagePixel.getRedValue(), green = imagePixel.getGreenValue(), blue = imagePixel.getBlueValue();

        double newRed = (1.0 * red + 0.5 * green + 0.1 * blue),
                newGreen = (0.5 * red + 1.0 * green + 0.1 * blue),
                newBlue = (0.1 * red + 0.5 * green + 1.0 * blue);

        return new double[]{newRed, newGreen, newBlue};
    }

    public static int[] cropImage(ImagePixel imagePixel) {

        int red = imagePixel.getRedValue(), green = imagePixel.getGreenValue(), blue = imagePixel.getBlueValue();

        int newRed, newGreen, newBlue;

        if (imagePixel.getX() < 100 || imagePixel.getX() > 500 || imagePixel.getY() < 100 || imagePixel.getY() > 500) {
            newRed = 0;
            newGreen = 0;
            newBlue = 0;
        } else {
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }

        return new int[]{newRed, newGreen, newBlue};
    }

    public static double[] filterToNight(ImagePixel imagePixel) {

        int red = imagePixel.getRedValue(), green = imagePixel.getGreenValue(), blue = imagePixel.getBlueValue();

        double newRed = (0.1 * red + 0.1 * green + 1.0 * blue),
                newGreen = (0.1 * red + 0.1 * green + 1.0 * blue),
                newBlue = (0.1 * red + 0.1 * green + 1.0 * blue);

        return new double[]{newRed, newGreen, newBlue};
    }

    private static boolean isShadeOfGray(int red, int green, int blue) {
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
