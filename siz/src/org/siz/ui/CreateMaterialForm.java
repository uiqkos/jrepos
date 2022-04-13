package org.siz.ui;

import org.siz.models.Material;
import org.siz.util.DialogUtils;

import javax.swing.*;
import java.util.stream.Collectors;

public class CreateMaterialForm extends BaseForm {
    private JPanel panel;
    private JTextField titleTextField;
    private JComboBox<String> typeComboBox;
    private JSpinner stockCountSpinner;
    private JTextField unitTextField;
    private JSpinner packCountSpinner;
    private JSpinner minCountSpinner;
    private JTextField costTextField;
    private JTextArea descTextArea;
    private JButton createButton;
    private JTextField imgPathTextField;

    public CreateMaterialForm() {
        super("Добавить материал", 800, 1000);

        setContentPane(panel);
        setVisible(true);

        initModels();
        initButtons();

    }

    private void initModels() {
        var types = Material.all()
            .stream()
            .map(Material::getType)
            .collect(Collectors.toSet());

        typeComboBox.setModel(new DefaultComboBoxModel<String>(types.toArray(String[]::new)));
    }

    private void initButtons() {
        createButton.addActionListener(
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

                var res = new Material(
                    null, title, packCount, unit, stockCount, minCount, desc, cost, type, imgPath
                ).save();

                if (res) {
                    dispose();
                    new MaterialTableForm();
                }
            }
        );
    }
}
