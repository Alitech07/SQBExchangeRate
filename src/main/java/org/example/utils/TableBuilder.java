package org.example.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TableBuilder {
    private List<String[]> rows;

    public TableBuilder() {
        rows = new ArrayList<>();
    }

    public void addRow(String... columns) {
        rows.add(columns);
    }

    private int[] getColumnWidths() {
        int numColumns = rows.get(0).length;
        int[] columnWidths = new int[numColumns];

        for (String[] row : rows) {
            for (int i = 0; i < numColumns; i++) {
                int cellWidth = row[i].length();
                if (cellWidth > columnWidths[i]) {
                    columnWidths[i] = cellWidth;
                }
            }
        }

        return columnWidths;
    }

    private String createSeparator(int[] columnWidths) {
        StringBuilder separator = new StringBuilder();
        for (int width : columnWidths) {
            separator.append("+").append(StringUtils.repeat("-",width+2));
        }
        separator.append("+");
        return separator.toString();
    }

    @Override
    public String toString() {
        StringBuilder table = new StringBuilder();
        int[] columnWidths = getColumnWidths();
        String separator = createSeparator(columnWidths);

        table.append(separator).append("\n");

        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                table.append("| ").append(String.format("%-" + columnWidths[i] + "s", row[i])).append(" ");
            }
            table.append("|\n");
            table.append(separator).append("\n");
        }

        return table.toString();
    }
}
