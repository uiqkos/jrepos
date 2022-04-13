package org.siz.util;

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
    private final List<String> columnNames;

    @Getter
    private final Map<String, Predicate<T>> filters = new HashMap<>();
    @Getter
    private Comparator<T> sorter;

    @Getter
    private List<T> rows;
    @Getter
    private List<T> filteredRows;

    public CustomTableModel(Class<T> cls, List<String> columnNames) {
        this.cls = cls;
        this.columnNames = columnNames;
    }

    public void updateFilteredRows() {
        filteredRows = rows
            .stream()
            .filter(
                filters
                    .values()
                    .stream()
                    .reduce(Predicate::and)
                    .orElse(o -> true)
            ).collect(Collectors.toList());

        if (sorter != null) {
            filteredRows.sort(sorter);
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

    public T getRowAt(int rowIndex) {
        return filteredRows.get(rowIndex);
    }

    @SneakyThrows
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var field = cls.getDeclaredFields()[columnIndex];
        field.setAccessible(true);
        return field.get(getRowAt(rowIndex));
    }

    public void setSorter(Comparator<T> sorter) {
        this.sorter = sorter;
        updateFilteredRows();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return cls.getDeclaredFields()[columnIndex].getType();
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
        updateFilteredRows();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }
}
