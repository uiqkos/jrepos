package org.jcomany.ui;

import org.jcomany.models.Product;
import org.jcomany.util.CustomTableModel;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductTableForm extends BaseForm {
    private JPanel panel;
    private JTable table;
    private JButton searchByNameButton;
    private JTextField searchTextField;
    private JComboBox<String> productTypeComboBox;
    private JComboBox sortComboBox;
    private JButton createButton;
    private JComboBox sortTypeComboBox;
    private CustomTableModel<Product> tableModel;
    private ComboBoxModel<String> productTypeComboBoxModel;
    private ComboBoxModel<String> sortComboBoxModel;
    private Boolean descending = false;

    public ProductTableForm() {
        super("Список продуктов");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panel);

        initModel();
        initTable();
        initButtons();
    }

    private void initButtons() {
        createButton.addActionListener(
            e -> {
                dispose();
                new CreateProductForm().setVisible(true);
            }
        );

        productTypeComboBox.addItemListener(
            l -> {
                if (l.getStateChange() == ItemEvent.SELECTED) {
                    var productType = (String) productTypeComboBox.getSelectedItem();

                    if (Objects.equals(productType, "Все")) {
                        tableModel.getFilters().remove("productType");
                    } else {
                        tableModel.getFilters().put("productType", product -> Objects.equals(product.getProductType(), productType));
                    }

                    tableModel.updateFilteredRows();
                }
            }
        );

        sortComboBox.addItemListener(
            l -> {
                if (l.getStateChange() == ItemEvent.SELECTED) {
                    var selected = Objects.requireNonNullElse((String) sortComboBox.getSelectedItem(), "названию");

                    switch (selected) {
                        case "названию":
                            tableModel.setSorter(Comparator.comparing(Product::getTitle));
                            break;

                        case "цеху":
                            tableModel.setSorter(Comparator.comparing(Product::getProductionWorkshopNumber));
                            break;

                        case "минимальной стоимости":
                            tableModel.setSorter(Comparator.comparing(Product::getMinCostForAgent));

                        default:

                    }
                    if (descending)
                        tableModel.setSorter(tableModel.getSorter().reversed());
                    tableModel.updateFilteredRows();
                }
            }
        );

        sortTypeComboBox.addItemListener(
            e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (Objects.equals(sortTypeComboBox.getSelectedItem(), "убыванию") != descending) {
                        tableModel.setSorter(tableModel.getSorter().reversed());
                        descending = !descending;
                    }
                }
            }
        );

        searchByNameButton.addActionListener(
            l -> {
                tableModel.getFilters().put("search", p -> p.getTitle().contains(searchTextField.getText()));
                tableModel.updateFilteredRows();
            }
        );

        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                tableModel.getFilters().put("search", p -> (p.getTitle() + p.getDescription()).contains(searchTextField.getText()));
                tableModel.updateFilteredRows();
            }
        });

    }

    private void initModel() {
        tableModel = new CustomTableModel<>(
            Product.class,
            List.of(
                "Id",
                "Название",
                "Тип продукта",
                "Артикул",
                "Описание",
                "productionPersonCount",
                "productionWorkshopNumber",
                "minCostForAgent",
                "Изображение",
                "Стоимость"
            )
        );

        var products = Product.all();
//        products.forEach(Product::updateCost);

        tableModel.setRows(products);
        tableModel.setSorter(Comparator.comparing(Product::getTitle));


        // product type filter
        var productTypes = new ArrayList<String>();
        productTypes.add("Все");
        productTypes.addAll(allProductTypes());

        productTypeComboBoxModel = new DefaultComboBoxModel<String>(productTypes.toArray(String[]::new));

        productTypeComboBox.setModel(productTypeComboBoxModel);


        // sort comboBox
        sortComboBoxModel = new DefaultComboBoxModel<String>(new String[] {
            "названию",
            "цеху",
            "минимальной стоимости"
        });

        sortComboBox.setModel(sortComboBoxModel);

    }

    public void initTable() {
        table.setModel(tableModel);
        table.setRowHeight(30);

        table.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = table.getSelectedRow();
//                        dispose();
//                        setVisible(false);
                        new UpdateProductForm(tableModel.getRowAt(row)).setVisible(true);
                    }
                }
            }
        );

//        var imgColumn = table.getColumn("Изображение");
//
    }

    public List<String> allProductTypes() {
        return tableModel.getRows().stream().map(Product::getProductType).collect(Collectors.toList());
    }


}
