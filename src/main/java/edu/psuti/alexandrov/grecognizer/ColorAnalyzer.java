package edu.psuti.alexandrov.grecognizer;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ColorAnalyzer {

    public static final MathContext DEFAULT_MATH_CONTEXT = new MathContext(10);
    public static final int DEFAULT_COLOR_RESTRICTION = 1000;

    private List<Color> colors;

    private ColorAnalyzer(List<Color> colors) {
        this.colors = colors;
    }

    public static ColorAnalyzer of(List<Color> colors) {
        return new ColorAnalyzer(colors);
    }

    public List<Color> getColors() {
        return colors;
    }

    public void setColors(List<Color> colors) {
        this.colors = colors;
    }

    public ResultsTable analyze() {
        return analyze(DEFAULT_COLOR_RESTRICTION, DEFAULT_MATH_CONTEXT);
    }

    public ResultsTable analyze(int colorsToCount) {
        return analyze(colorsToCount, DEFAULT_MATH_CONTEXT);
    }

    public ResultsTable analyze(int colorsToCount, MathContext mathContext) {
        final BigDecimal percentDivisor = BigDecimal
                .valueOf(colors.size())
                .divide(BigDecimal.valueOf(100), mathContext);
        Map<Color, BigDecimal> results = colors
                .parallelStream()
                .collect(Collectors.groupingByConcurrent(Color::getRgbAsString))
                .values()
                .parallelStream()
                .sorted(Comparator.comparing(List::size, Comparator.reverseOrder()))
                .limit(colorsToCount)
                .collect(Collectors.toConcurrentMap(
                        list -> list.get(0),
                        list -> BigDecimal
                                .valueOf(list.size())
                                .divide(percentDivisor, mathContext)
                ));
        return ResultsTable.of(results);
    }
}
