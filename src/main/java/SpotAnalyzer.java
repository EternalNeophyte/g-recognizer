import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class SpotAnalyzer {

    private Mat img;
    private String resultPath;
    private static final Size KERNEL_SIZE = new Size(3, 3);

    public SpotAnalyzer(String initialPath, String resultPath) {
        img = Imgcodecs.imread(initialPath);
        this.resultPath = resultPath;
        run();
    }

    private void run() {
        // Загрузка изображения
        Mat erodedImg = img.clone(), blurrImg = new Mat(), monochromeImg = new Mat(), edges = new Mat();
        // Наложение фильтров
        Imgproc.erode(img, erodedImg, Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, KERNEL_SIZE, new Point(-1, -1)));
        Imgproc.GaussianBlur(erodedImg, blurrImg, KERNEL_SIZE, 0);
        Imgproc.cvtColor(blurrImg, monochromeImg, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(monochromeImg, edges, 240, 255);
        // Поиск контуров
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(edges, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_TC89_L1);
        Imgproc.cvtColor(edges, img, Imgproc.COLOR_GRAY2BGR);
        // Финальная отрисовка
        makeImgNegative(img);
        ArrayList<RotatedRect> rects = getRects(img, contours);
        sortRects(rects);
        printResults(img, rects);
        // Сохранение
        Imgcodecs.imwrite(resultPath, img);
    }

    private static boolean radiusHasValidSize(Mat img, MatOfPoint contour) {
        /*
            Если радиус окружности, описанной вокруг пятна, слишком мал,
            то по его контурам уже невозможно построить эллипс.
            Такое пятно придется считать кругом.
        */
        float[] radius = new float[1];
        Point center = new Point();
        Imgproc.minEnclosingCircle(new MatOfPoint2f(contour.toArray()), center, radius);
        if (radius[0] > 2.5) {
            return true;
        }
        else {
            Imgproc.putText(img, String.format("R = %.6f x 10^-5 m", radius[0] / 3.16),
                    new Point(center.x, center.y - 8),
                    Imgproc.FONT_HERSHEY_DUPLEX, 0.4, new Scalar(0, 0, 255), 1);
            return false;
        }
    }

    private static ArrayList<RotatedRect> getRects(Mat img, ArrayList<MatOfPoint> contours) {
        ArrayList<RotatedRect> rects = new ArrayList<>();
        for (MatOfPoint contour : contours) {
            if(radiusHasValidSize(img, contour)) {
                rects.add(Imgproc.fitEllipse(new MatOfPoint2f(contour.toArray())));
            }
        }
        return rects;
    }

    private static void sortRects(ArrayList<RotatedRect> rects) {
        rects.sort((o1, o2) -> (int) (o1.size.width + o1.size.height - o2.size.width - o2.size.height));
    }

    private static void printResults(Mat img, ArrayList<RotatedRect> rects) {
        for(int i = 0; i < rects.size(); i++) {
            Imgproc.putText(img, i + 1 + " ", rects.get(i).center, Imgproc.FONT_HERSHEY_DUPLEX,
                    0.5, new Scalar(255, 60, 10), 1);
            Imgproc.putText(img, getRectInfo(rects.get(i), i + 1), new Point(620, 40 + i * 22),
                    Imgproc.FONT_HERSHEY_DUPLEX, 0.65, new Scalar(0, 0, 0), 1);
        }
    }

    private static String getRectInfo(RotatedRect rect, int index) {
        double[] parameters = convertPixelsToMeters(rect);
        String type = isCircle(rect) ? "Circle " : "Ellipse ";
        StringBuilder sb = new StringBuilder(type);
        sb.append(index).append(", min radius = ").append(String.format("%.8f", parameters[1]));
        sb.append(" x 10^-5 m, max radius = ").append(String.format("%.8f", parameters[0])).append(" x 10^-5 m");
        return sb.toString();
    }

    private static boolean isCircle(RotatedRect rect) {
        /*
            Допустим, что пятно имеет круглую форму,
            если разница диаметров не превышает 1 пиксель
        */
        return Math.abs(rect.size.height - rect.size.width) < 1;
    }

    private static double[] convertPixelsToMeters(RotatedRect rect) {
        /*
            0.001 м = 158 пикселей -> 1 м = 158К пикселей
            Кроме того, большой и малый радиус в 2 раза меньше высоты и ширины прямоугольника
            158К * 2 = 316К = 3.16 * 10⁵
        */
        return new double[] { rect.size.height / 3.16, rect.size.width / 3.16 };
    }

    private static void makeImgNegative(Mat img) {
        Scalar scalar;
        for(int i = 0; i < img.rows(); i++) {
            for(int j = 0; j < img.cols(); j++) {
                scalar = new Scalar(img.get(i, j));
                img.put(i, j, 255 - scalar.val[0], 255 - scalar.val[1], 255 - scalar.val[2]);
            }
        }
    }
}
