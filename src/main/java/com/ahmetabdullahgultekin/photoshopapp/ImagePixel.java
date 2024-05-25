package com.ahmetabdullahgultekin.photoshopapp;

public class ImagePixel {

    private int RGB;
    private int redValue;
    private int greenValue;
    private int blueValue;

    //Constructor
    public ImagePixel(int RGB) {
        this.RGB = RGB;
        this.redValue = (RGB & 0x00FF0000) >> 16;
        this.greenValue = (RGB & 0x0000FF00) >> 8;
        this.blueValue = RGB & 0x000000FF;
    }

    public int getRedValue() {
        return redValue;
    }

    public void setRedValue(int redValue) {
        this.redValue = redValue;
    }

    public int getGreenValue() {
        return greenValue;
    }

    public void setGreenValue(int greenValue) {
        this.greenValue = greenValue;
    }

    public int getBlueValue() {
        return blueValue;
    }

    public void setBlueValue(int blueValue) {
        this.blueValue = blueValue;
    }

    public int getRGB() {
        return RGB;
    }

    public void setRGB(int RGB) {
        this.RGB = RGB;
    }
}
