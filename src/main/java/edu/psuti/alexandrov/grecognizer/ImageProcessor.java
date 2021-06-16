package edu.psuti.alexandrov.grecognizer;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ImageProcessor {

    public static final int AREA_SIZE = 50;

    private final String path;
    private final Mat image;
    private boolean processingNotDone;

    private ImageProcessor(String path) {
        this.path = path;
        this.image = new Mat();
        this.processingNotDone = true;
    }

    public static ImageProcessor fromPath(String imagePath) {
        return new ImageProcessor(imagePath);
    }

    public Mat processedImage() {
        Mat primaryImage = Imgcodecs.imread(path);
        Imgproc.bilateralFilter(primaryImage, image,
                AREA_SIZE, AREA_SIZE * 2, AREA_SIZE * 2);
        processingNotDone = false;
        return image;
    }

    public List<Color> colorsList() {
        if(processingNotDone) {
            processedImage();
        }
        Scalar rgb;
        List<Color> colors = new ArrayList<>();
        for(int i = 0; i < image.rows(); i++) {
            for (int j = 0; j < image.cols(); j++) {
                rgb = new Scalar(image.get(i, j));
                colors.add(new Color(rgb));
            }
        }
        return colors;
    }
}
