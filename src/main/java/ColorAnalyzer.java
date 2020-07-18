import org.opencv.core.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ColorAnalyzer {

    private Mat mat;
    private List<ColorInfo> pixels;
    private final static MathContext PRECISION = new MathContext(10);

    public ColorAnalyzer(Mat mat) {
        this.mat = mat;
        pixels = new ArrayList<>();
    }

    public ColorAnalyzer() {
        this(new Mat());
    }

    public Mat getMat() {
        return mat;
    }

    public void setMat(Mat mat) {
        this.mat = mat;
    }

    public List<ColorInfo> getPixels() {
        return pixels;
    }

    public void setPixels(List<ColorInfo> pixels) {
        this.pixels = pixels;
    }

    public void defineColors(int countOfFragments) {
        pixels = new ArrayList<>();
        BigDecimal initialPercentage = BigDecimal.valueOf(100)
                .divide(BigDecimal.valueOf(mat.rows() * mat.cols() * countOfFragments), PRECISION);
        for(int i = 0; i < mat.rows(); i++) {
            for (int j = 0; j < mat.cols(); j++) {
                pixels.add(new ColorInfo(new Scalar(mat.get(i, j)), initialPercentage));
            }
        }
    }

    public void calculateColorPercentage(List<ColorInfo> pixels, String threadName) {
        LocalDateTime start = LocalDateTime.now();
        for(int i = 0; i < pixels.size(); i++) {
            for(int j = 0; j < pixels.size(); j++) {
                if(pixels.get(i).getRgb().equals(pixels.get(j).getRgb()) && i != j) {
                    pixels.get(i).setPercentage(pixels.get(i).getPercentage().add(pixels.get(j).getPercentage()));
                    pixels.remove(pixels.get(j));
                }
            }
            printStatus(i, pixels.size(), threadName + ": подсчет пикселей завершен на ");
        }
        printTimeOfCompleting(threadName, start, LocalDateTime.now());
    }

    public List<ColorInfo> getColorsList(int colorsLimit) {
        List<ColorInfo> list = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(0);
        pixels.stream()
                .sorted(ColorInfo::compareTo)
                .forEach(entry -> {
                    if(counter.get() < colorsLimit) {
                        list.add(entry);
                        counter.getAndIncrement();
                    }
                });
        return list;
    }

    private BigDecimal getPixelPercentage(int countOfFragments, int countOfPixels) {
        return BigDecimal.valueOf(countOfPixels)
                .divide(BigDecimal.valueOf(mat.rows() * mat.cols() * countOfFragments), PRECISION)
                .multiply(BigDecimal.valueOf(100));
    }

    private void printStatus(int counter, int maxValue, String status) {
        BigDecimal percentage = BigDecimal.valueOf(counter)
                .divide(BigDecimal.valueOf(maxValue), PRECISION)
                .multiply(BigDecimal.valueOf(100));
        System.out.println(status + String.format("%.2f", percentage) + "%");
    }

    private void printTimeOfCompleting(String threadName, LocalDateTime start, LocalDateTime stop) {
        long minutes = ChronoUnit.MINUTES.between(start, stop);
        System.out.println(threadName + " завершил работу. Время выполнения: " + minutes + " мин " +
                (ChronoUnit.SECONDS.between(start, stop) - minutes * 60)  + " c");
    }
}
