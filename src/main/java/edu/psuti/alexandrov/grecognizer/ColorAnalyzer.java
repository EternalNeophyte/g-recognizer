package edu.psuti.alexandrov.grecognizer;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ColorAnalyzer {

    public static final MathContext DEFAULT_MATH_CONTEXT = new MathContext(10);
    public static final AtomicInteger DEFAULT_COLOR_RESTRICTION = new AtomicInteger(1000);

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

    public ResultsTable<?, ?> analyze() {
        return analyze(DEFAULT_COLOR_RESTRICTION, DEFAULT_MATH_CONTEXT);
    }

    public ResultsTable<?, ?> analyze(AtomicInteger colorsToCount) {
        return analyze(colorsToCount, DEFAULT_MATH_CONTEXT);
    }

    public ResultsTable<?, ?> analyze(AtomicInteger colorsToCount, MathContext mathContext) {
        final BigDecimal percentDivisor = BigDecimal.valueOf(colors.size())
                .divide(BigDecimal.valueOf(100), mathContext);
        Map<Color, VisualMetrics> results = colors
                .parallelStream()
                .collect(Collectors.groupingByConcurrent(Color::getRgbAsString))
                .values()
                .parallelStream()
                .sorted(Comparator.comparing(List::size, Comparator.reverseOrder()))
                .limit(colorsToCount.get())
                .collect(Collectors.toConcurrentMap(
                        listToKey -> listToKey.get(0),
                        listToValue -> VisualMetrics.builder()
                                .percentage(BigDecimal.valueOf(listToValue.size())
                                        .divide(percentDivisor, mathContext))
                                .pixelCountFromInt(listToValue.size())
                                .build()
                ));

        return ResultsTable.<Color, VisualMetrics>with()
                .header("Results of color analysis")
                .column("Color as RGB")
                .column("Percentage, %")
                .column("Pixels count")
                .columnWidth(20)
                .columnFormatter((c, v) -> new Object[] {
                        c.getRgbAsString(),
                        v.getPercentage(),
                        v.getPixelCount()
                })
                .including(results);
    }
}
