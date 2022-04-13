package org.siz.ui;

import org.siz.models.Material;
import org.siz.models.MaterialSupplier;
import org.siz.util.CustomTableModel;
import org.siz.util.DialogUtils;

import javax.swing.*;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EditMaterialForm extends BaseForm {
    private final Material material;
    private JPanel panel;
    private JTextField titleTextField;
    private JComboBox<String> typeComboBox;
    private JSpinner stockCountSpinner;
    private JTextField unitTextField;
    private JSpinner packCountSpinner;
    private JSpinner minCountSpinner;
    private JTextField costTextField;
    private JTextArea descTextArea;
    private JButton saveButton;
    private JTextField imgPathTextField;
    private JLabel idLabel;
    private JButton delButton;
    private JTable supplierTable;
    private JButton addSupplierButton;
    private CustomTableModel<MaterialSupplier> tableModel;

    public EditMaterialForm(Material material) {
        super("Добавить материал", 800, 1000);

        setContentPane(panel);
        setVisible(true);

        this.material = material;

        initModels();
        initFields();
        initButtons();
        initTable();

    }

    private void initTable() {
        tableModel = new CustomTableModel<>(
            MaterialSupplier.class,
            List.of(
                "Наименование",
                "Тип",
                "Рейтинг",
                "Комментарий"
            )
        );
        tableModel.setRows(
            MaterialSupplier.all()
                .stream()
                .filter(ms -> Objects.equals(
                    ms.getMaterialId(), material.getId())
                ).collect(Collectors.toList())
        );
        tableModel.setSorter(Comparator.comparing(MaterialSupplier::getRating).reversed());
        tableModel.updateFilteredRows();

        supplierTable.setModel(tableModel);
    }

    private void initFields() {
        idLabel.setText(String.valueOf(material.getId()));
        titleTextField.setText(material.getTitle());
        typeComboBox.setSelectedItem(material.getType());
        stockCountSpinner.setValue(material.getCountInStock());
        unitTextField.setText(material.getUnit());
        packCountSpinner.setValue(material.getCountInPack());
        minCountSpinner.setValue(material.getMinCount());
        costTextField.setText(String.valueOf(material.getCost()));
        descTextArea.setText(material.getDescription());
    }

    private void initModels() {
        var types = Material.all()
            .stream()
            .map(Material::getType)
            .collect(Collectors.toSet());

        typeComboBox.setModel(new DefaultComboBoxModel<String>(types.toArray(String[]::new)));



    }

    private void initButtons() {
        saveButton.addActionListener(
            l -> {
                var title = titleTextField.getText();

                if (title.isEmpty()) {
                    DialogUtils.showError(this, "Наименование не может быть пустым");
                    return;
                }
                if (title.length() > 100) {
                    DialogUtils.showError(this, "Наименование не может быть более 100 символов");
                    return;
                }

                var type = (String) typeComboBox.getSelectedItem();
                var stockCount = (Integer) stockCountSpinner.getValue();
                if (stockCount < 0) {
                    DialogUtils.showError(this, "Кол-во на складе не может быть меньше 0");
                    return;
                }

                var unit = unitTextField.getText();
                var packCount = (Integer) packCountSpinner.getValue();

                if (packCount < 0) {
                    DialogUtils.showError(this, "Кол-во в упаковке не может быть меньше 0");
                    return;
                }

                var minCount = (Integer) minCountSpinner.getValue();
                if (minCount < 0) {
                    DialogUtils.showError(this, "Минимальное кол-во не может быть меньше 0");
                    return;
                }

                var cost = 0.0;

                try {
                    cost = Double.parseDouble(costTextField.getText());
                } catch (Exception e) {
                    DialogUtils.showError(this, "Неверно введена стоимость");
                    return;
                }

                var desc = descTextArea.getText();
                var imgPath = imgPathTextField.getText();

                var res = Material.update(new Material(
                    material.getId(), title, packCount, unit, stockCount, minCount, desc, cost, type, imgPath
                ));

                if (res) {
                    dispose();
                    new MaterialTableForm();
                }
            }
        );

        delButton.addActionListener(
            l -> {
                var res = Material.remove(material.getId());

                if (res) {
                    dispose();
                    new MaterialTableForm();
                }
            }
        );

        addSupplierButton.addActionListener(
            l -> {
                new CreateMaterialSupplierForm(material);
            }
        );
    }
}
