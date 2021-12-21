package org.learn.util;

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

    private Class<T> cls;
    private List<String> columnNames;

    @Getter
    private List<T> rows;
    private List<T> filteredRows;
    @Getter
    private final Map<String, Predicate<T>> filters = new HashMap<>();
    @Getter
    private Comparator<T> sorter;

    public CustomTableModel(Class<T> cls, List<String> columnNames) {
        this.cls = cls;
        this.columnNames = columnNames;
    }

    public void updateFilteredRows() {
        filteredRows = rows
            .stream().filter(
                filters.values().stream().reduce(Predicate::and).orElse(o -> true)
            ).collect(Collectors.toList());

        if (sorter != null)
            filteredRows.sort(sorter);

        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return filteredRows.size();
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    public T getRowAt(int rowIndex) {
        return filteredRows.get(rowIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return cls.getDeclaredFields()[columnIndex].getType();
    }

    @SneakyThrows
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var field = cls.getDeclaredFields()[columnIndex];
        field.setAccessible(true);
        return field.get(getRowAt(rowIndex));
    }

    public CustomTableModel<T> setRows(List<T> rows) {
        this.rows = rows;
        updateFilteredRows();
        return this;
    }

    public CustomTableModel<T> setSorter(Comparator<T> sorter) {
        this.sorter = sorter;
        updateFilteredRows();
        return this;
    }
}

