import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

public class FragmentAnalyzer implements Runnable {

    private int countOfFragments;
    private Thread thread;
    private Mat fragment;
    private ColorAnalyzer colorAnalyzer;
    private List<ColorInfo> colorsList;

    public FragmentAnalyzer(int countOfFragments, String threadName, Mat fragment) {
        this.countOfFragments = countOfFragments;
        thread = new Thread(this, threadName);
        this.fragment = fragment;
        colorAnalyzer = new ColorAnalyzer(fragment);
        colorsList = new ArrayList<>();
        thread.start();
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Mat getFragment() {
        return fragment;
    }

    public void setFragment(Mat fragment) {
        this.fragment = fragment;
    }

    public ColorAnalyzer getColorAnalyzer() {
        return colorAnalyzer;
    }

    public int getCountOfFragments() {
        return countOfFragments;
    }

    public void setCountOfFragments(int countOfFragments) {
        this.countOfFragments = countOfFragments;
    }

    public List<ColorInfo> getColorsList() {
        return colorsList;
    }

    public void setColorsList(List<ColorInfo> colorsList) {
        this.colorsList = colorsList;
    }

    public void setColorAnalyzer(ColorAnalyzer colorAnalyzer) {
        this.colorAnalyzer = colorAnalyzer;
    }

    public void setResults(int colorsLimit) {
        setColorsList(colorAnalyzer.getColorsList(colorsLimit));
    }

    @Override
    public void run() {
        colorAnalyzer.defineColors(countOfFragments);
        colorAnalyzer.calculateColorPercentage(colorAnalyzer.getPixels(), thread.getName());
    }
}
