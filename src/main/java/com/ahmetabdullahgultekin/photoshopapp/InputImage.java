package com.ahmetabdullahgultekin.photoshopapp;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class InputImage extends Image {

    Image image;
    String sourceFile;
    String url;
    int pixelCount;
    ImageView imageView;

    public InputImage(String url) {
        super(url);
    }

    public InputImage(String url, boolean backgroundLoading) {
        super(url, backgroundLoading);
    }

    public InputImage(String url, double requestedWidth, double requestedHeight, boolean preserveRatio, boolean smooth, boolean backgroundLoading) {
        super(url, requestedWidth, requestedHeight, preserveRatio, smooth, backgroundLoading);
    }
}
