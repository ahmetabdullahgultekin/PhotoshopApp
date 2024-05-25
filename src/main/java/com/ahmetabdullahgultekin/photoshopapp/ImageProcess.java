package com.ahmetabdullahgultekin.photoshopapp;

import java.awt.image.BufferedImage;

public class ImageProcess {

    private String name;

    //Constructor
    public ImageProcess(String imageProcess) {
        this.name = imageProcess;
    }

    public static int[] paintWhiteToPink(ImagePixel imagePixel) {

        int red = imagePixel.getRedValue(), green = imagePixel.getGreenValue(), blue = imagePixel.getBlueValue();

        int newRed = red, newGreen = green, newBlue = blue;

        if(isShadeOfGray(imagePixel.getRedValue(), imagePixel.getGreenValue(), imagePixel.getBlueValue())) {
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


    public void newFeatures() {
        System.out.println("This is the new feature");
        // Traverse the image
        /*
        newRed = 255 - red;
        newGreen = 255 - green;
        newBlue = 255 - blue;
        */

        /*
        // Increase the brightness of the image
        newRed = Math.min(255, red + 10);
        newGreen = Math.min(255, green + 10);
        newBlue = Math.min(255, blue + 10);
        */

        /*
        // Decrease the brightness of the image
        newRed = Math.max(0, red - 10);
        newGreen = Math.max(0, green - 10);
        newBlue = Math.max(0, blue - 10);
        */

        /*
        // Increase the contrast of the image
        newRed = Math.min(255, red + 50);
        newGreen = Math.min(255, green + 50);
        newBlue = Math.min(255, blue + 50);
        */

        /*
        // Decrease the contrast of the image
        newRed = Math.max(0, red - 50);
        newGreen = Math.max(0, green - 50);
        newBlue = Math.max(0, blue - 50);
        */

        /*
        // Grayscale the image
        int luminosity = (int) (0.299 * red + 0.587 * green + 0.114 * blue);
        newRed = luminosity;
        newGreen = luminosity;
        newBlue = luminosity;
        */

        /*
        // Reduce the saturation of the image
        int average = (red + green + blue) / 3;
        newRed = average;
        newGreen = average;
        newBlue = average;
        */


        /*
        // Increase the saturation of the image
        int max = Math.max(red, Math.max(green, blue));
        int min = Math.min(red, Math.min(green, blue));
        int average = (red + green + blue) / 3;
        newRed = average + (max - average) * 2;
        newGreen = average + (max - average) * 2;
        newBlue = average + (max - average) * 2;
        */

        /*
        // Adjust the hue of the image
        float[] hsb = new float[3];
        java.awt.Color.RGBtoHSB(red, green, blue, hsb);
        hsb[0] = (hsb[0] + 0.1f) % 1.0f;
        int newRGBx = java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
        newRed = getRed(newRGBx);
        newGreen = getGreen(newRGBx);
        newBlue = getBlue(newRGBx);
        */

        /*
        // apply a sepia filter
        int sepiaRed = (int) (0.393 * red + 0.769 * green + 0.189 * blue);
        int sepiaGreen = (int) (0.349 * red + 0.686 * green + 0.168 * blue);
        int sepiaBlue = (int) (0.272 * red + 0.534 * green + 0.131 * blue);
        newRed = Math.min(255, sepiaRed + 50);
        newGreen = Math.min(255, sepiaGreen + 50);
        newBlue = Math.min(255, sepiaBlue + 50);
        */

        /*
        // filter to make the image look like a cartoon
        newRed = (int) (0.393 * red + 0.769 * green + 0.189 * blue);
        newGreen = (int) (0.349 * red + 0.686 * green + 0.168 * blue);
        newBlue = (int) (0.272 * red + 0.534 * green + 0.131 * blue);
        */

        /*
        // filter to make the image look like a sunset
        newRed = (int) (1.0 * red + 0.5 * green + 0.1 * blue);
        newGreen = (int) (0.5 * red + 1.0 * green + 0.1 * blue);
        newBlue = (int) (0.1 * red + 0.5 * green + 1.0 * blue);
*/

        /*
        // filter to make the image look like a sunrise
        newRed = (int) (1.0 * red + 0.5 * green + 0.1 * blue);
        newGreen = (int) (0.5 * red + 1.0 * green + 0.1 * blue);
        newBlue = (int) (0.1 * red + 0.5 * green + 1.0 * blue);
*/
/*
        // crop the image
        if (x < 100 || x > 500 || y < 100 || y > 500) {
            newRed = 0;
            newGreen = 0;
            newBlue = 0;
        } else {
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }
*/
    }
    private static boolean isShadeOfGray(int red, int green, int blue) {
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs( green - blue) < 30;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
