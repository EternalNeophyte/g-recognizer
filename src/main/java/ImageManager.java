import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageManager {

    private List<FragmentAnalyzer> analyzers;
    private List<ColorInfo> results;
    private Mat img;
    private final static int AREA_SIZE = 50;

    public ImageManager(int colorsLimit, int nCores, String initialPath) {
        analyzers = new ArrayList<>();
        results = new ArrayList<>();
        img = new Mat();
        Imgproc.bilateralFilter(Imgcodecs.imread(initialPath), img,
                AREA_SIZE, AREA_SIZE * 2, AREA_SIZE * 2);
        FragmentSpreader spreader = new FragmentSpreader(nCores);
        spreader.spreadAllValues(img.cols(), img.rows());
        startThreads(spreader);
        waitAllThreads();
        addResults(colorsLimit);
        correctResults();
    }

    public List<FragmentAnalyzer> getAnalyzers() {
        return analyzers;
    }

    public void setAnalyzers(List<FragmentAnalyzer> analyzers) {
        this.analyzers = analyzers;
    }

    public Mat getImg() {
        return img;
    }

    public void setImg(Mat img) {
        this.img = img;
    }

    private void startThreads(FragmentSpreader spreader) {
        int threadId = 0;
        for(int i = 0; i < spreader.getValuesAtX().size() - 1; i++) {
            for(int j = 0; j < spreader.getValuesAtY().size() - 1; j++) {
                analyzers.add(new FragmentAnalyzer(spreader.getnCores(),"Поток#" + ++threadId,
                        img.submat(spreader.getValuesAtY().get(j), spreader.getValuesAtY().get(j + 1),
                                spreader.getValuesAtX().get(i), spreader.getValuesAtX().get(i + 1))));
            }
        }

    }

    private void waitAllThreads() {
        analyzers.forEach(analyzer -> {
            try {
                analyzer.getThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void addResults(int colorsLimit) {
        analyzers.forEach(analyzer -> {
            analyzer.setResults(colorsLimit);
            results.addAll(analyzer.getColorsList());
        });
    }

    private void correctResults() {
        new ColorAnalyzer().calculateColorPercentage(results, "Основной поток");
        results.sort(ColorInfo::compareTo);
    }

    public void printResults() {
        results.forEach(colorInfo -> System.out.println("R " + colorInfo.getRgb().val[2] + "; G " + colorInfo.getRgb().val[1] +
                "; B " + colorInfo.getRgb().val[0] + "; Perc - " + String.format("%.5f", colorInfo.getPercentage()) + "%"));
        System.out.println("Other colors percentage: " + String.format("%.5f", getOtherColorsPercentage()) + "%");
    }

    private BigDecimal getOtherColorsPercentage() {
        /*
              ПРЕДУПРЕЖДЕНИЕ: при слишком большом значении colorsLimit результат работы
              метода может быть некорректным!
        */
        BigDecimal totalPercentage = BigDecimal.ZERO;
        for(ColorInfo colorInfo : results) {
            totalPercentage = totalPercentage.add(colorInfo.getPercentage());
        }
        return BigDecimal.valueOf(100).subtract(totalPercentage);
    }

    public void saveFragments() {
        AtomicInteger counter = new AtomicInteger(1);
        analyzers.forEach(analyzer -> Imgcodecs.imwrite(".\\src\\main\\java\\assets\\cut" +
                        counter.getAndIncrement() + ".png", analyzer.getFragment()));
    }
}
