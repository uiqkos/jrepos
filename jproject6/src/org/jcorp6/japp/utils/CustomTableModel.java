package org.jcorp6.japp.utils;

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
    private List<String> columns;

    @Getter
    private Map<String, Predicate<T>> filters = new HashMap<>();

    @Setter @Getter
    private Comparator<T> sorter;

    private List<T> rows;
    private List<T> filteredRows;

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
                    .orElse(o -> true)
            )
            .collect(Collectors.toList());

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

    public T getRowAt(int row) {
        return filteredRows.get(row);
    }

    @SneakyThrows
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var field = cls.getDeclaredFields()[columnIndex];
        field.setAccessible(true);
        return field.get(getRowAt(rowIndex));
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
        update();
    }
}
