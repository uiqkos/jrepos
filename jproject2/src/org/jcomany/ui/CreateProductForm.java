package org.jcomany.ui;

import org.jcomany.models.Product;
import org.jcomany.util.DialogUtils;

import javax.swing.*;

public class CreateProductForm extends BaseForm {
    private JTextField articleTextField;
    private JTextField titleTextField;
    private JComboBox<Object> productTypeComboBox;
    private JTextField imgPathTextField;
    private JSpinner workersSpinner;
    private JSpinner workshopNumber;
    private JSpinner minCostSpinner;
    private JTextArea descArea;
    private JPanel panel;
    private JButton addButton;
    private JSpinner articleSpinner;

    public CreateProductForm() {
        super("Создать продукт");

        setContentPane(panel);

        initModels();
        initButtons();

    }

    private void initModels() {
        productTypeComboBox.setModel(new DefaultComboBoxModel<>(Product.allProductTypes().toArray(String[]::new)));
    }

    private void initButtons() {
        addButton.addActionListener(
            e -> {

                var article = (Integer) articleSpinner.getValue();

                if (article < 0) {
                    DialogUtils.showError(this, "Артикль не может быть меньше 0");
                    return;
                }

                var title = titleTextField.getText();

                if (title.isEmpty()) {
                    DialogUtils.showError(this, "Название не может быть пустым");
                    return;
                }

                var type = (String) productTypeComboBox.getSelectedItem();
                var imgPath = imgPathTextField.getText();

                if (imgPath.isEmpty()) {
                    DialogUtils.showError(this, "Путь до изображения не может быть пустым");
                    return;
                }

                var workers = (Integer) workersSpinner.getValue();
                if (workers < 0) {
                    DialogUtils.showError(this, "Количество работников не может быть меньше 0");
                    return;
                }


                var workshop = (Integer) workshopNumber.getValue();
                if (workshop < 0) {
                    DialogUtils.showError(this, "Номер цеха не может быть меньше 0");
                    return;
                }

                var minCost = (Integer) minCostSpinner.getValue();
                if (minCost < 0) {
                    DialogUtils.showError(this, "Минимальная цена не может быть меньше 0");
                    return;
                }

                var desc = descArea.getText();

                if (new Product(
                    null,
                    title,
                    type,
                    article,
                    desc,
                    workers,
                    workshop,
                    minCost,
                    imgPath
                ).save()) {
                    dispose();
                    new ProductTableForm().setVisible(true);
                }

            }
        );
    }
}
