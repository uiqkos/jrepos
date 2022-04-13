package org.siz.ui;

import org.siz.models.Material;
import org.siz.util.CustomTableModel;
import static org.siz.util.RangeUtils.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MaterialTableForm extends BaseForm {
    private JPanel panel;
    private JTable table;
    private JButton addButton;
    private JPanel buttonPanel;
    private JButton costSortButton;
    private JComboBox<Object> typeComboBox;
    private JComboBox<Object> costComboBox;
    private JTextField searchTextField;
    private JButton resetButton;
    private JLabel rowCountLabel;
    private CustomTableModel<Material> tableModel;

    private Boolean descending = null;

    public MaterialTableForm() {
        super("Список материалов", 800, 800);

        setContentPane(panel);
        setVisible(true);

        initModel();
        initTable();
        initFields();
        initButtons();
    }

    private void initModel() {
        tableModel = new CustomTableModel<>(
            Material.class,
            List.of(
                "Наименование",
                "Тип",
                "Изображение",
                "Цена",
                "Кол-во на складе",
                "Минимальное количество",
                "Кол-во в упаковке",
                "Единица измерения"
            )
        );

        tableModel.setRows(Material.all());

        var types = new ArrayList<String>();
        types.add("Все");
        types.addAll(Material.all().stream().map(Material::getType).collect(Collectors.toSet()));
        typeComboBox.setModel(new DefaultComboBoxModel<>(types.toArray(String[]::new)));

        typeComboBox.addItemListener(
            l -> {
                if (l.getStateChange() == ItemEvent.SELECTED) {
                    var selected = (String) typeComboBox.getSelectedItem();
                    if (!Objects.equals(selected, "Все"))
                        tableModel.getFilters().put("type", m -> Objects.equals(m.getType(), selected));
                    else
                        tableModel.getFilters().remove("type");
                    updateRows();
                }
            }
        );

        Map<String, Predicate<Material>> costFilters = Map.of(
            "Все", o -> true,
            "до 5 000", (Material m) -> inRange(m.getCost(), 0, 5000),
            "от 5 000 до 15 000", (Material m) -> inRange(m.getCost(), 5_000, 15_000),
            "от 15 000 до 50 000", (Material m) -> inRange(m.getCost(), 15_000, 50_000),
            "от 50 000 до 100 000", (Material m) -> inRange(m.getCost(), 50_000, 100_000),
            "больше 100 000", (Material m) -> m.getCost() > 100_000
        );

        costComboBox.setModel(new DefaultComboBoxModel<>(
            new String[] {
                "Все",
                "до 5 000",
                "от 5 000 до 15 000",
                "от 15 000 до 50 000",
                "от 50 000 до 100 000",
                "больше 100 000"
            }
        ));
        costComboBox.addItemListener(
            l -> {
                if (l.getStateChange() == ItemEvent.SELECTED) {
                    var selected = (String) costComboBox.getSelectedItem();
                    if (!Objects.equals(selected, "Все"))
                        tableModel.getFilters().put("cost", costFilters.get(selected));
                    else
                        tableModel.getFilters().remove("cost");
                    updateRows();
                }
            }
        );
    }

    private void initTable() {
        table.setModel(tableModel);
        table.setRowHeight(30);
        table.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount() == 2) {
                        int row = table.getSelectedRow();
                        if (row != -1) {
                            dispose();
                            new EditMaterialForm(tableModel.getRowAt(row));
                        }
                    }
                }
            }
        );
    }

    private void initFields() {

        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                tableModel.getFilters().put("search", m -> m.getTitle().contains(searchTextField.getText()));
                updateRows();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                tableModel.getFilters().put("search", m -> m.getTitle().contains(searchTextField.getText()));
                updateRows();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                tableModel.getFilters().put("search", m -> m.getTitle().contains(searchTextField.getText()));
                updateRows();
            }
        });
    }

    private void initButtons() {
        costSortButton.addActionListener(
            l -> {
                if (descending == null) {
                    tableModel.setSorter(Comparator.comparing(Material::getCost));
                    descending = false;
                } else {
                    descending = !descending;
                    tableModel.setSorter(tableModel.getSorter().reversed());
                }
                updateRows();
            }
        );

        addButton.addActionListener(
            l -> {
                dispose();
                new CreateMaterialForm();
            }
        );

        resetButton.addActionListener(
            l -> {
                tableModel.getFilters().clear();
                searchTextField.setText("");
                costComboBox.setSelectedItem(0);
                typeComboBox.setSelectedItem(0);
                descending = null;
                tableModel.setSorter(null);
                updateRows();
            }
        );
    }
    
    private void updateRows() {
        tableModel.updateFilteredRows();
        rowCountLabel.setText(tableModel.getRowCount() + " / " + tableModel.getRows().size());
    }
}
