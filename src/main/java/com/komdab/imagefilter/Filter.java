package com.komdab.imagefilter;

import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Class to apply filter
 */
public class Filter {
    /**
     * Method to apply blur filter
     * @param imagePath path Input, path Output and file name
     * @param size filter parameter
     * @throws FilterException return exception if number is odd, is negative and image not found
     */
    public static void blur(ImagePath imagePath, int size) throws FilterException {
        if (size % 2 == 0) // si n pair
            throw new FilterException("Size argument must be an odd number !");
        if (size < 1)
            throw new FilterException("Size argument must be a positive number !");

        Mat image = imread(imagePath.getImagePathIn());
        if (image == null)
            throw new FilterException("Image not found !");

        GaussianBlur(image, image, new Size(size, size), 0);
        imwrite(imagePath.getImagePathOut(), image);
    }

    /**
     * Method to apply dilate filter
     * @param imagePath path Input, path Output and file name
     * @param size filter parameter
     * @throws FilterException return exception if number is negative and image not found
     */
    public static void dilate(ImagePath imagePath, int size) throws FilterException {

        if (size < 0)
            throw new FilterException("Size argument must be a positive number !");

        Mat image = imread(imagePath.getImagePathIn());
        if (image == null)
            throw new FilterException("Image not found !");
        Mat element = getStructuringElement(Imgproc.MORPH_RECT, new Size(2 * size + 1, 2 * size + 1));
        opencv_imgproc.dilate(image, image, element);
        imwrite(imagePath.getImagePathOut(), image);
    }

    /**
     * Method to apply grayscale filter
     * @param imagePath path Input, path Output and file name
     * @throws FilterException return exception if image not found
     */
    public static void grayscale(ImagePath imagePath) throws FilterException {
        Mat image = imread(imagePath.getImagePathIn());
        if (image == null)
            throw new FilterException("Image not found !");
        cvtColor(image, image, Imgproc.COLOR_RGB2GRAY);
        imwrite(imagePath.getImagePathOut(), image);
    }

    /**
     * Method to apply zeteam filter
     * @param imagePath path Input, path Output and file name
     * @throws FilterException return exception if image not found
     */
    public static void zeTeam(ImagePath imagePath) throws FilterException {
        Mat image = imread(imagePath.getImagePathIn());
        if (image == null)
            throw new FilterException("Image not found !");
        opencv_imgproc.putText(image, "Team KomDab",new Point(5,25), FONT_HERSHEY_SCRIPT_COMPLEX, 1.01,
                new Scalar(0, 0, 0, 2.0));
        imwrite(imagePath.getImagePathOut(), image);
    }
}