package org.jcorp6.japp.ui;

import org.jcorp6.japp.models.Product;
import org.jcorp6.japp.utils.CustomTableModel;
import org.jcorp6.japp.utils.DialogUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class ProductsForm extends BaseForm {
    private JTable table;
    private JPanel panel;
    private JButton createButton;
    private JComboBox<Predicate<Product>> costComboBox;
    private JComboBox<String> typeComboBox;
    private JLabel countLabel;
    private JButton resetButton;
    private JTextField searchTextField;
    private JButton sortButton;

    private Boolean descending;

    private CustomTableModel<Product> tableModel;

    public ProductsForm() {
        super("Продукты", 600, 700);

        setContentPane(panel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initModels();
        initTable();
        initComboBoxes();
        initButtons();
    }

    private void initModels() {
        tableModel = new CustomTableModel<>(
            Product.class,
            List.of(
                "Наименование",
                "Артикул",
                "Минимальная стоимость",
                "Изображение",
                "Тип продукта",
                "Описание",
                "Количесво человек",
                "Номер цеха"
            )
        );

        tableModel.setRows(Product.all());
    }

    private void initTable() {
        table.setModel(tableModel);
        table.setRowHeight(30);
        table.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        var row = table.getSelectedRow();
                        var product = tableModel.getRowAt(row);
                        dispose();
                        new EditProductForm(product).setVisible(true);
                    }
                }
            }
        );
    }

    private void initComboBoxes() {
        BiFunction<Dimension, String, Predicate<Product>> makePredicate =
            (Dimension d, String string) ->
            new Predicate<Product>() {
                @Override public boolean test(Product product) {
                    return d.width <= product.getMinCostForAgent()
                        && product.getMinCostForAgent() < d.height;
                }
                @Override public String toString() { return string; }
            };

        costComboBox.addItem(makePredicate.apply(new Dimension(0, 999_999_999), "Все"));

        var max = tableModel
            .getRows()
            .stream()
            .mapToDouble(Product::getMinCostForAgent)
            .max().getAsDouble();

        IntStream.iterate(0, i -> i < max, i -> i + 10_000).boxed()
            .map(i -> makePredicate.apply(
                new Dimension(i, i + 10_000),
                "от %d до %d".formatted(i, i + 10_000)
            ))
            .forEach(costComboBox::addItem);

        costComboBox.addItemListener(
            a -> {
                if (a.getStateChange() == ItemEvent.SELECTED) {
                    tableModel.getFilters().put("cost", (Predicate<Product>) costComboBox.getSelectedItem());
                    updateRows();
                }
            }
        );

        typeComboBox.addItem("Все");
        Product
            .all()
            .stream()
            .map(Product::getProductType)
            .distinct()
            .forEach(typeComboBox::addItem);

        typeComboBox.addItemListener(
            a -> {
                if (a.getStateChange() == ItemEvent.SELECTED) {
                    var type = (String) typeComboBox.getSelectedItem();
                    if (type.equals("Все")) {
                        tableModel.getFilters().remove("type");
                    } else {
                        tableModel.getFilters().put("type", p -> p.getProductType().equals(type));
                    }
                    updateRows();
                }
            }
        );

        searchTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                tableModel.getFilters().put("search",
                    p -> p.getTitle().contains(searchTextField.getText().toLowerCase()));
                updateRows();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                tableModel.getFilters().put("search",
                    p -> p.getTitle().contains(searchTextField.getText().toLowerCase()));
                updateRows();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                tableModel.getFilters().put("search",
                    p -> p.getTitle().contains(searchTextField.getText().toLowerCase()));
                updateRows();
            }
        });
    }

    private void initButtons() {
        resetButton.addActionListener(
            a -> {
                searchTextField.setText("");
                typeComboBox.setSelectedIndex(0);
                costComboBox.setSelectedIndex(0);
                descending = null;
                tableModel.getFilters().clear();
                tableModel.setSorter(null);
            }
        );

        createButton.addActionListener(
            a -> {
                new CreateProductForm().setVisible(true);
                dispose();
            }
        );

        sortButton.addActionListener(
            a -> {
                if (descending == null) {
                    descending = true;
                    tableModel.setSorter(Comparator.comparing(Product::getMinCostForAgent));
                }
                descending = !descending;
                tableModel.setSorter(tableModel.getSorter().reversed());
                updateRows();
            }
        );
    }

    private void updateRows() {
        tableModel.update();
        countLabel.setText(tableModel.getRowCount() + "/" + tableModel.getRows().size());
    }
}
