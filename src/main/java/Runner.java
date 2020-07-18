import org.opencv.core.*;

public class Runner {

    public static void main(String[] args) {
        runColorPercentageTask();
    }

    private static void runColorPercentageTask() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        ImageManager manager = new ImageManager(50, Runtime.getRuntime().availableProcessors(),
                ".\\src\\main\\java\\assets\\cat.png");
        manager.printResults();
    }
}
