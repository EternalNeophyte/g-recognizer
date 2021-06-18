package edu.psuti.alexandrov.grecognizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class ResultsTable<K, V> {

    private String header;
    private List<String> columns;
    private int columnWidth;
    private BiFunction<K, V, Object[]> columnFormatter;
    private Map<K, V> results;

    public ResultsTable() {
        this.columns = new ArrayList<>();
    }

    public static <K, V> ResultsTable<K, V> with() {
        return new ResultsTable<>();
    }

    public ResultsTable<K, V> header(String header) {
        this.header = header;
        return this;
    }

    public ResultsTable<K, V> column(String column) {
        columns.add(column);
        return this;
    }

    public ResultsTable<K, V> columnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
        return this;
    }

    public ResultsTable<K, V> columnFormatter(BiFunction<K, V, Object[]> columnFormatter) {
        this.columnFormatter = columnFormatter;
        return this;
    }

    public ResultsTable<K, V> including(Map<K, V> results) {
        this.results = results;
        return this;
    }

    public String getHeader() {
        return header;
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public List<String> getColumns() {
        return columns;
    }

    public Map<K, V> getResults() {
        return results;
    }

    public void printAll() {
        printHeader();
        results.forEach((k, v) -> printRow(columnFormatter.apply(k, v)));
    }

    public void printHeader() {
        System.out.printf("[\t%" + columnWidth + "s ]%n", header);
        printRow(columns.toArray());
    }

    private void printRow(Object... columns) {
        System.out.println(formatToRow(columns));
    }

    private String formatToRow(Object... columns) {
        return String.format(buildPattern(), columns);
    }

    private String buildPattern() {
        StringBuilder sb = new StringBuilder("|");
        columns.forEach(c -> sb.append("\t%")
                .append(columnWidth)
                .append("s |"));
        return sb.toString();
    }
}
