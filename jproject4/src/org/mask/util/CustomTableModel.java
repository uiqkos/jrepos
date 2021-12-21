package org.mask.util;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.swing.table.AbstractTableModel;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CustomTableModel<T> extends AbstractTableModel {

    private final Class<T> cls;
    private final List<String> columnNames;

    @Getter
    private List<T> rows;
    private List<T> filteredRows;

    @Getter
    private Map<String, Predicate<T>> filters;
    @Getter @Setter
    private Comparator<T> sorted;

    public CustomTableModel(Class<T> cls, List<String> columnNames) {
        this.cls = cls;
        this.columnNames = columnNames;

    }

    public void updateFilteredRows() {
        filteredRows = rows
            .stream().filter(
                filters
                    .values()
                    .stream()
                    .reduce(Predicate::and)
                    .orElse(o -> true)
            ).collect(Collectors.toList());

        if (sorted != null) {
            filteredRows.sort(sorted);
        }

        fireTableDataChanged();
    }


    @Override
    public int getRowCount() {
        return filteredRows.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    public T getRowAt(int row) {
        return filteredRows.get(row);
    }

    @SneakyThrows
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var field = cls.getDeclaredFields()[columnIndex];
        field.setAccessible(true);
        return field.get(filteredRows.get(rowIndex));
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return cls.getDeclaredFields()[columnIndex].getType();
    }
}
