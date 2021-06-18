import edu.psuti.alexandrov.grecognizer.*;
import nu.pattern.OpenCV;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Runner {

    public static void main(String[] args) {
        try {
            runGRecognizer();
        } catch (InterruptedException | ExecutionException | ShedulerNotReadyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runGRecognizer() throws ExecutionException, InterruptedException {
        OpenCV.loadLocally();
        List<Color> colors = ImageProcessor
                .fromPath(".\\src\\main\\resources\\assets\\cat.png")
                .colorsList();
        ResultsTable<?, ?> resultsTable = AnalysisSheduler
                .builder()
                .serviceByDefault()
                .task(() -> ColorAnalyzer.of(colors).analyze())
                .build()
                .call()
                .get();
        resultsTable.printAll();
    }
}