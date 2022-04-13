package org.jcorp6.japp.ui;

import org.jcorp6.japp.models.Product;
import org.jcorp6.japp.utils.CustomTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class ProductsForm extends BaseForm {
    private JTable table;
    private JPanel panel;
    private JButton createButton;
    private JButton deleteButton;
    private JComboBox<Predicate<Product>> costComboBox;
    private JComboBox<String> typeComboBox;

    private CustomTableModel<Product> tableModel;

    public ProductsForm() {
        super("Продукты", 600, 700);

        setContentPane(panel);

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
    }

    private void initComboBoxes() {
        BiFunction<Dimension, Supplier<String>, Predicate<Product>> makePredicate =
            (Dimension d, Supplier<String> supplier) ->
            new Predicate<Product>() {
                @Override public boolean test(Product product) {
                    return d.width <= product.getMinCostForAgent()
                        && product.getMinCostForAgent() < d.height;
                }
                @Override public String toString() { return supplier.get(); }
            };

        costComboBox.addItem(makePredicate.apply(new Dimension(0, 999_999_999), () -> "Все"));

        IntStream.range(1, 4).boxed()
            .map(i -> i * 10_000)
            .map(i -> makePredicate.apply(
                new Dimension(i - 10_000, i),
                () -> "от %d до %d".formatted(i - 10_000, i)
            ))
            .forEach(costComboBox::addItem);

        costComboBox.addItemListener(
            a -> {
                if (a.getStateChange() == ItemEvent.SELECTED) {
                    tableModel.getFilters().put("cost", (Predicate<Product>) costComboBox.getSelectedItem());
                    tableModel.update();
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
                    tableModel.update();
                }
            }
        );
    }

    private void initButtons() {
    }
}
