package com.ahmetabdullahgultekin.photoshopapp;

import static com.sun.javafx.util.Utils.RGBtoHSB;

public class ImagePixel {

    //Properties
    private int X;
    private int Y;
    private int RGB;
    private int redValue;
    private int greenValue;
    private int blueValue;
    private double alphaValue;
    private double hueValue;
    private double saturationValue;
    private double brightnessValue;
    private double luminanceValue;
    private double intensityValue;

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

    public void setX(int x) {
        X = x;
    }

    public double getIntensityValue() {
        return intensityValue;
    }

    public void setIntensityValue(double intensityValue) {
        this.intensityValue = intensityValue;
    }

    public double getLuminanceValue() {
        return luminanceValue;
    }

    public void setLuminanceValue(double luminanceValue) {
        this.luminanceValue = luminanceValue;
    }

    public double getBrightnessValue() {
        return brightnessValue;
    }

    public void setBrightnessValue(double brightnessValue) {
        this.brightnessValue = brightnessValue;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public int getGreenValue() {
        return greenValue;
    }

    public void setGreenValue(int greenValue) {
        this.greenValue = greenValue;
    }

    public int getRGB() {
        return RGB;
    }

    public void setRGB(int RGB) {
        this.RGB = RGB;
    }

    public int getRedValue() {
        return redValue;
    }

    public void setRedValue(int redValue) {
        this.redValue = redValue;
    }

    public int getBlueValue() {
        return blueValue;
    }

    public void setBlueValue(int blueValue) {
        this.blueValue = blueValue;
    }

    public double getAlphaValue() {
        return alphaValue;
    }

    public void setAlphaValue(double alphaValue) {
        this.alphaValue = alphaValue;
    }

    public double getHueValue() {
        return hueValue;
    }

    public void setHueValue(double hueValue) {
        this.hueValue = hueValue;
    }

    public double getSaturationValue() {
        return saturationValue;
    }

    public void setSaturationValue(double saturationValue) {
        this.saturationValue = saturationValue;
    }
}
