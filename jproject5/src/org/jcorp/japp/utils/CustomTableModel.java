package org.jcorp.japp.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.swing.table.AbstractTableModel;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CustomTableModel<T> extends AbstractTableModel {
    private final Class<T> cls;
    private final List<String> columns;

    @Setter
    private List<T> rows;
    private List<T> filteredRows;

    @Setter @Getter
    private Map<String, Predicate<T>> filters = new HashMap<>();
    @Setter @Getter
    private Comparator<T> comparator;

    public CustomTableModel(Class<T> cls, List<String> columns) {
        this.cls = cls;
        this.columns = columns;
    }

    public void update() {
        filteredRows = rows
            .stream()
            .filter(
                filters
                    .values()
                    .stream()
                    .reduce(Predicate::and)
                    .orElseGet(() -> o -> true)
            )
            .collect(Collectors.toList());

        if (comparator != null) {
            filteredRows.sort(comparator);
        }

        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return filteredRows.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @SneakyThrows
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var field = cls.getDeclaredFields()[columnIndex];
        field.setAccessible(true);
        return field.get(filteredRows.get(rowIndex));
    }

    public List<String> getColumns() {
        return columns;
    }

    @Override
    public String getColumnName(int column) {
        return columns.get(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return cls.getDeclaredFields()[columnIndex].getType();
    }

    public T getRowAt(int row) {
        return filteredRows.get(row);
    }
}
