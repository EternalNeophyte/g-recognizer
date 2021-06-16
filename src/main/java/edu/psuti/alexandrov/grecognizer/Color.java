package edu.psuti.alexandrov.grecognizer;

import org.opencv.core.Scalar;

public class Color {

    private Scalar rgb;
    private String rgbAsString;

    public Color(Scalar rgb) {
        setRgb(rgb);
    }

    public Scalar getRgb() {
        return rgb;
    }

    public void setRgb(Scalar rgb) {
        this.rgb = rgb;
        this.rgbAsString = toString();
    }

    public String getRgbAsString() {
        return rgbAsString;
    }

    @Override
    public String toString() {
        return String.format("R = %d, G = %d, B = %d",
                                (int) rgb.val[0],
                                (int) rgb.val[1],
                                (int) rgb.val[2]);
    }
}
