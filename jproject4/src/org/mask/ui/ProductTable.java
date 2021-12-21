package org.mask.ui;

import org.mask.models.Product;
import org.mask.util.CustomTableModel;

import javax.swing.*;
import java.util.stream.IntStream;

public class ProductTable extends BaseForm {

    private JTable table;
    private JPanel panel;
    private CustomTableModel<Product> tableModel;

    public ProductTable() {
        super("Список продуктов", 800, 800);

        setContentPane(panel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        initModels();
        initTable();
        initButtons();

    }

    private void initModels() {

    }

    private void initTable() {
    }

    private void initButtons() {
    }
}
