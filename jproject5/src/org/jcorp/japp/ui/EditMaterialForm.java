package org.jcorp.japp.ui;

import org.jcorp.japp.models.Material;
import org.jcorp.japp.utils.DialogUtils;

import javax.swing.*;
import java.util.stream.Collectors;

public class EditMaterialForm extends BaseForm {
    private JPanel panel;
    private JTextField titleTextField;
    private JComboBox<String> typeComboBox;
    private JSpinner stockCountSpinner;
    private JTextField unitTextField;
    private JSpinner packCountSpinner;
    private JSpinner minCountSpinner;
    private JTextField costTextField;
    private JTextField imagePathTextField;
    private JTextArea descriptionTextArea;
    private JButton createButton;
    private JLabel idLabel;
    String[] types;
    Material material;

    public EditMaterialForm(Material material) {
        super("Создать материал", 500, 400);

        this.material = material;

        setContentPane(panel);

        initLabels();
        initComboBoxes();
        initButtons();
    }

    private void initLabels() {
        idLabel.setText(material.getId().toString());
    }

    private void initComboBoxes() {
        this.types = Material
            .all()
            .stream()
            .map(Material::getMaterialType)
            .collect(Collectors.toSet())
            .toArray(String[]::new);

        typeComboBox.setModel(new DefaultComboBoxModel<>(this.types));
        typeComboBox.setSelectedIndex(0);
    }

    private void initButtons() {
        createButton.addActionListener(
            a -> {
                var title = titleTextField.getText();
                if (title.isBlank()) {
                    DialogUtils.showError(this, "Название не может быть пустым");
                    return;
                }

                var stockCount = (Integer) stockCountSpinner.getValue();
                if (stockCount < 0) {
                    DialogUtils.showError(this, "Количество на складе не может быть меньше 0");
                    return;
                }

                var unit = unitTextField.getText();
                if (unit.isBlank()) {
                    DialogUtils.showError(this, "Название не может быть пустым");
                    return;
                }

                var packCount = (Integer) packCountSpinner.getValue();
                if (packCount < 0) {
                    DialogUtils.showError(this, "Количество в упаковке не может быть меньше 0");
                    return;
                }

                var minCount = (Integer) minCountSpinner.getValue();
                if (minCount < 0) {
                    DialogUtils.showError(this, "Минимальное количество не может быть меньше 0");
                    return;
                }

                Double cost;

                try {
                    cost = Double.parseDouble(costTextField.getText());
                } catch (NumberFormatException e) {
                    DialogUtils.showError(this, "Минимальное количество не может быть меньше 0");
                    return;
                }

                var materialType = types[typeComboBox.getSelectedIndex()];
                var path = imagePathTextField.getText();
                var desc = descriptionTextArea.getText();

                material = new Material(
                    material.getId(),
                    title, packCount, unit, stockCount,
                    minCount.doubleValue(), desc, cost,
                    materialType, path
                );

                if (material.save().getId() == null) {
                    DialogUtils.showError(this, "Ошибка сохранения");
                    return;
                }

                dispose();

                new MaterialListForm().setVisible(true);
            }
        );
    }
}
