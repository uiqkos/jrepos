package org.jcorp6.japp.ui;

import org.jcorp6.japp.models.MaterialProduct;
import org.jcorp6.japp.models.Product;
import org.jcorp6.japp.utils.CustomTableModel;
import org.jcorp6.japp.utils.DialogUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class EditProductForm extends BaseForm {
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
    private JLabel idLabel;
    private JTable materialProductTable;
    private JButton deleteButton;
    private Product product;
    private CustomTableModel<MaterialProduct> tableModel;

    public EditProductForm(Product product) throws HeadlessException {
        super("Добавить продукт", 600, 700);

        this.product = product;

        setContentPane(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initModel();
        initTable();
        initFields();
        initButton();
    }

    private void initModel() {
        tableModel = new CustomTableModel<>(
            MaterialProduct.class,
            List.of(
                "Наименование продукта",
                "Тип продукта",
                "Наименование материала",
                "Тип материала",
                "Стоимость материала",
                "Количество"
            )
        );
        tableModel.setRows(
            MaterialProduct
                .all(product.getId())
        );
        tableModel.setSorter(Comparator.comparing(pm -> pm.getCount() * pm.getMaterialCost()));
        tableModel.setSorter(tableModel.getSorter().reversed());
        tableModel.update();
    }

    private void initTable() {
        materialProductTable.setModel(tableModel);
        materialProductTable.setRowHeight(30);
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

        costSpinner.setValue(product.getMinCostForAgent());
        personSpinner.setValue(product.getProductionPersonCount());
        workshopSpinner.setValue(product.getProductionWorkshopNumber());
        titleTextField.setText(product.getTitle());
        articleTextField.setText(product.getArticleNumber());
        idLabel.setText(product.getId().toString());
        imgTextField.setText(product.getImagePath());
        descTextArea.setText(product.getDescription());
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

                product.setTitle(title);
                product.setArticleNumber(article);
                product.setProductType(type);
                product.setMinCostForAgent(cost);
                product.setProductionPersonCount(personCount);
                product.setImagePath(imagePath);
                product.setDescription(desc);
                product.setProductionWorkshopNumber(workshop);

                try {
                    product.save();
                } catch (SQLException e) {
                    e.printStackTrace();
                    DialogUtils.showError(this, "Ошибка сохранения");
                    return;
                }

                if (product.getId() == null) {
                    DialogUtils.showError(this, "Ошибка сохранения");
                    return;
                }

                new ProductsForm().setVisible(true);
                dispose();
            }
        );

        deleteButton.addActionListener(
            a -> {
                try {
                    Product.delete(product.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                    DialogUtils.showError(this, "Ошибка удаления");
                    return;
                }
                dispose();
                new ProductsForm().setVisible(true);
            }
        );
    }
}
