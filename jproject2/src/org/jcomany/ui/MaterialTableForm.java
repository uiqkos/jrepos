package org.jcomany.ui;

import org.jcomany.models.Material;
import org.jcomany.util.CustomTableModel;

import javax.swing.*;
import java.util.List;
import java.io.IOException;

public class MaterialTableForm extends BaseForm {
    private JPanel panel;
    private JTable table;
    private JButton createButton;
    private JComboBox sortComboBox;
    private JComboBox productTypeComboBox;
    private JTextField textField1;
    private JButton поискПоНазваниюButton;
    private CustomTableModel<Material> model;

    public MaterialTableForm() throws IOException {
        super("Список продуктов");

        setContentPane(panel);

        initModel();
        initTable();
        initButtons();
    }

    private void initButtons() {
    }

    private void initModel() {
        model = new CustomTableModel<>(
            Material.class,
            List.of(
                "Id",
                "Название",
                "Количество в упаковке",
                "Единица измерения",
                "Количество на складе",
                "Минимальное количество",
                "Описание",
                "Цена",
                "Изображение",
                "Тип материала"
            )
        );

        model.setRows(Material.all());

    }

    public void initTable() {
        table.setModel(model);

    }


}
