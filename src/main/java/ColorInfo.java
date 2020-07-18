import org.opencv.core.Scalar;

import java.math.BigDecimal;

public class ColorInfo implements Comparable<ColorInfo> {

    private Scalar rgb;
    private BigDecimal percentage;

    public ColorInfo(Scalar rgb, BigDecimal percentage) {
        this.rgb = rgb;
        this.percentage = percentage;
    }

    public Scalar getRgb() {
        return rgb;
    }

    public void setRgb(Scalar rgb) {
        this.rgb = rgb;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    @Override
    public int compareTo(ColorInfo o) {
        return o.getPercentage().compareTo(getPercentage());
    }
}
