package com.komdab.imagefilter;

/**
 * Class to collect image path
 */
public class ImagePath {
    private String pathIn;
    private String pathOut;
    private String fileName;

    public ImagePath(String pathIn, String pathOut, String fileName) {
        this.pathIn = pathIn;
        this.pathOut = pathOut;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getImagePathIn() {
        return this.pathIn + "/" + this.fileName;
    }

    public String getImagePathOut() {
        return this.pathOut + "/" + this.fileName;
    }
}