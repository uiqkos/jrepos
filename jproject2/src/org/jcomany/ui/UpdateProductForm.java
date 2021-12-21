package org.jcomany.ui;

import org.jcomany.models.Product;
import org.jcomany.ui.BaseForm;
import org.jcomany.ui.ProductTableForm;
import org.jcomany.util.DialogUtils;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UpdateProductForm extends BaseForm {
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

    private Product product;

    public UpdateProductForm(Product product) {
        super("Создать продукт");

        this.product = product;

        setContentPane(panel);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("closing");
                e.getWindow().dispose();
                new ProductTableForm().setVisible(true);
            }
        });

        initValues();
        initModels();
        initButtons();

    }

    private void initValues() {
        articleSpinner.setValue(product.getArticleNumber());
        titleTextField.setText(product.getTitle());
        productTypeComboBox.setSelectedItem(product.getProductType());
        imgPathTextField.setText(product.getImagePath());
        workersSpinner.setValue(product.getProductionPersonCount());
        workshopNumber.setValue(product.getProductionWorkshopNumber());
        minCostSpinner.setValue(product.getMinCostForAgent());
        descArea.setText(product.getDescription());
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
                    product.getId(),
                    title,
                    type,
                    article,
                    desc,
                    workers,
                    workshop,
                    minCost,
                    imgPath
                ).update()) {
                    dispose();
                    new ProductTableForm().setVisible(true);
                }

            }
        );
    }
}
