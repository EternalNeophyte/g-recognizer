package edu.psuti.alexandrov.grecognizer;

import java.math.BigDecimal;
import java.util.Map;

public class ResultTable {

    private Map<Color, BigDecimal> results;

    private ResultTable(Map<Color, BigDecimal> results) {
        this.results = results;
    }

    public static ResultTable of(Map<Color, BigDecimal> results) {
        return new ResultTable(results);
    }

    public Map<Color, BigDecimal> getResults() {
        return results;
    }

    public void setResults(Map<Color, BigDecimal> results) {
        this.results = results;
    }

    private String toRow(String inLeftColumn, String inRightColumn) {
        return String.format("|\t%-20s|\t%-50s|", inLeftColumn, inRightColumn);
    }

    public void printResults() {
        System.out.println(toRow("Percentage, %", "Color as RGB"));
        results.forEach((k, v) -> System.out.println(toRow(v.toString(), k.toString())));
    }
}
