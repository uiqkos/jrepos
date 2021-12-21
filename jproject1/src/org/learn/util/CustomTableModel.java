package org.learn.util;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class CustomTableModel<T> extends AbstractTableModel {
    private Class<?> cls;
    private List<String> columns;

    @Setter @Getter
    private List<T> rows;
    private List<T> filteredRows;

    @Getter @Setter
    private List<Predicate<T>> filters = new ArrayList<>();
    @Getter @Setter
    private Comparator<T> sorter;

    public CustomTableModel(Class<?> cls, List<String> columns) {
        this.cls = cls;
        this.columns = columns;
    }

    public void updateFilteredRows() {
        filteredRows = rows
            .stream()
            .filter(
                filters
                    .stream()
                    .reduce(Predicate::and)
                    .orElse(t -> true)
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
        return columns.size();
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

    @Override
    public String getColumnName(int column) {
        return columns.get(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return cls.getDeclaredFields()[columnIndex].getType();
    }
}
