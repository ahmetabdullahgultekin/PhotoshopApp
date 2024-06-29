package com.ahmetabdullahgultekin.photoshopapp;

public class ImagePixel {

    //Properties
    private final int X;
    private final int Y;
    private int RGB;
    private final int redValue;
    private final int greenValue;
    private final int blueValue;
    private final double alphaValue;

    //Constructor
    public ImagePixel(int x, int y, int RGB) {

        this.X = x;
        this.Y = y;
        this.RGB = RGB;
        this.redValue = (RGB & 0x00FF0000) >> 16;
        this.greenValue = (RGB & 0x0000FF00) >> 8;
        this.blueValue = RGB & 0x000000FF;
        this.alphaValue = (RGB & 0xFF000000) >>> 24;
//        this.hueValue = RGBtoHSB(redValue, greenValue, blueValue)[0];
//        this.saturationValue = RGBtoHSB(redValue, greenValue, blueValue)[1] * 100;
//        this.brightnessValue = RGBtoHSB(redValue, greenValue, blueValue)[2] * 100;
//        this.luminanceValue = (0.2126 * redValue + 0.7152 * greenValue + 0.0722 * blueValue);
//        this.intensityValue = (redValue + greenValue + blueValue) / 3.0;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public int getGreenValue() {
        return greenValue;
    }

    public void setRGB(int RGB) {
        this.RGB = RGB;
    }

    public int getRedValue() {
        return redValue;
    }

    public int getBlueValue() {
        return blueValue;
    }
}
