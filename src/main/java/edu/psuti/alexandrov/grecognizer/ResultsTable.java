package edu.psuti.alexandrov.grecognizer;

import java.util.Map;

public class ResultsTable {

    private Map<?, ?> results;

    private ResultsTable(Map<?, ?> results) {
        this.results = results;
    }

    public static ResultsTable of(Map<?, ?> results) {
        return new ResultsTable(results);
    }

    public Map<?, ?> getResults() {
        return results;
    }

    public void setResults(Map<?, ?> results) {
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
