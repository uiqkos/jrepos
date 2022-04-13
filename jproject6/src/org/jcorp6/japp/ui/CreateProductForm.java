package org.jcorp6.japp.ui;

import org.jcorp6.japp.models.Product;
import org.jcorp6.japp.utils.DialogUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class CreateProductForm extends BaseForm {
    private JButton createButton;
    private JTextField titleTextField;
    private JTextField articleTextField;
    private JSpinner costSpinner;
    private JTextField imgTextField;
    private JComboBox typeComboBox;
    private JTextArea descTextArea;
    private JSpinner personSpinner;
    private JSpinner workshopSpinner;
    private JPanel panel;

    public CreateProductForm() throws HeadlessException {
        super("Добавить продукт", 600, 700);

        setContentPane(panel);

        initFields();
        initButton();
    }

    private void initFields() {
        costSpinner.setModel(new SpinnerNumberModel(0.0, 0.0, 999_999_999, 0.1));
        costSpinner.addChangeListener(
            a -> {
                costSpinner.setValue(Math.floor(100 * (Double) costSpinner.getValue()) / 100);
            }
        );
        personSpinner.setModel(new SpinnerNumberModel(0, 0, 999_999_999, 1));
        workshopSpinner.setModel(new SpinnerNumberModel(0, 0, 999_999_999, 1));
        Product.all().stream().map(Product::getProductType).distinct().forEach(typeComboBox::addItem);
    }

    private void initButton() {
        createButton.addActionListener(
            a -> {
                var title = titleTextField.getText();
                if (title.isBlank()) {
                    DialogUtils.showError(this, "Наименование не может быть пустым");
                    return;
                }

                var article = articleTextField.getText();
                if (article.isBlank()) {
                    DialogUtils.showError(this, "Артикул не может быть пустым");
                    return;
                }

                var type = (String) typeComboBox.getSelectedItem();
                var cost = (Double) costSpinner.getValue();
                var personCount = (Integer) personSpinner.getValue();
                var imagePath = imgTextField.getText();
                var desc = descTextArea.getText();
                var workshop = (Integer) workshopSpinner.getValue();

                try {
                    new Product(
                        title,
                        type,
                        article,
                        desc,
                        personCount,
                        workshop,
                        cost,
                        imagePath
                    ).save();

                } catch (SQLException e) {
                    e.printStackTrace();
                    DialogUtils.showError(this, "Ошибка сохранения");
                    return;
                }

                dispose();
                new ProductsForm().setVisible(true);
            }
        );
    }
}
