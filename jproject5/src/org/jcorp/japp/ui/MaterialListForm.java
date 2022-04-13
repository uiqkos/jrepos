package org.jcorp.japp.ui;

import org.jcorp.japp.utils.CustomTableModel;
import org.jcorp.japp.models.Material;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.jcorp.japp.utils.Utils.inRange;

public class MaterialListForm extends BaseForm {
    private JPanel panel;
    private JTable table;
    private JButton createButton;
    private JButton deleteButton;
    private JComboBox<Object> materialTypeComboBox;
    private JComboBox<String> costComboBox;
    private CustomTableModel<Material> tableModel;

    public MaterialListForm() {
        super("Материалы", 500, 400);

        setContentPane(panel);

        initModel();
        initTable();
        initComboBoxes();
        initButtons();
    }

    private void initComboBoxes() {
        var types = new ArrayList<String>();

        types.add("Все");
        types.addAll(
            Material
                .all()
                .stream()
                .map(Material::getMaterialType)
                .collect(Collectors.toSet())
        );

        materialTypeComboBox.setModel(
            new DefaultComboBoxModel<>(
                types.toArray(String[]::new)
            )
        );

        materialTypeComboBox.addItemListener(
            a -> {
                if (a.getStateChange() == ItemEvent.SELECTED) {
                    var materialType = (String) materialTypeComboBox.getSelectedItem();
                    if (!Objects.equals(materialType, "Все")) {
                        tableModel.getFilters().put("type", m -> Objects.equals(m.getMaterialType(), materialType));
                    } else {
                        tableModel.getFilters().remove("type");
                    }
                    tableModel.update();
                }
            }
        );

        Map<String, Predicate<Material>> costFilters = Map.of(
            "Все", o -> true,
            "до 5 000", (Material m) -> inRange(m.getCost(), 0, 5_000),
            "от 5 000 до 15 000", (Material m) -> inRange(m.getCost(), 5_000, 15_000),
            "от 15 000 до 50 000", (Material m) -> inRange(m.getCost(), 15_000, 50_000),
            "от 50 000 до 100 000", (Material m) -> inRange(m.getCost(), 50_000, 100_000),
            "больше 100 000", (Material m) -> inRange(m.getCost(), 100_000, Double.MAX_VALUE)
        );

        costComboBox.setModel(
            new DefaultComboBoxModel<>(
                new String[]{
                    "Все",
                    "до 5 000",
                    "от 5 000 до 15 000",
                    "от 15 000 до 50 000",
                    "от 50 000 до 100 000",
                    "больше 100 000"
                }
            )
        );

        costComboBox.addItemListener(
            a -> {
                if (a.getStateChange() == ItemEvent.SELECTED) {
                    var cost = (String) costComboBox.getSelectedItem();
                    if (!Objects.equals(cost, "Все")) {
                        tableModel.getFilters().put("cost", costFilters.get(cost));
                    } else {
                        tableModel.getFilters().remove("cost");
                    }
                    tableModel.update();
                }
            }
        );
    }

    private void initModel() {
        tableModel = new CustomTableModel<>(
            Material.class,
            List.of(
                "Наименование",
                "Тип",
                "Изображение",
                "Цена",
                "Количество на складе",
                "Минимальное количество",
                "Количество в упаковке",
                "Единица измерения"
            )
        );

        tableModel.setRows(Material.all());
        tableModel.update();

    }

    private void initTable() {
        table.setModel(tableModel);
    }

    private void initButtons() {
        createButton.addActionListener(
            a -> {
                new CreateMaterialForm().setVisible(true);
                dispose();
            }
        );

        deleteButton.addActionListener(
            a -> {
                var index = table.getSelectedRow();
                var material = tableModel.getRowAt(index);
                Material.deleteById(material.getId());
                tableModel.setRows(Material.all());
                tableModel.update();
            }
        );
    }
}
