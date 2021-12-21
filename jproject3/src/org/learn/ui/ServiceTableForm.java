package org.learn.ui;

import org.learn.models.ClientServiceEntry;
import org.learn.models.Service;
import org.learn.util.CustomTableModel;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.List;
import static org.learn.util.MathUtils.*;
import java.util.Map;
import java.util.function.Predicate;

public class ServiceTableForm extends BaseForm {
    private JTable table;
    private JPanel panel;
    private JButton addButton;
    private JComboBox discountComboBox;
    private JTextField searchTextField;
    private JLabel countLabel;
    private JButton costSortButton;
    private JButton filterResetButton;
    private JButton addClientService;
    private CustomTableModel<Service> tableModel;
    private ComboBoxModel<String> discountComboBoxModel;
    private boolean descending = true;

    public ServiceTableForm() {
        super("Список услуг", 800, 800);

        setContentPane(panel);

        initModel();
        initTable();
        initButtons();

    }

    private void initModel() {
        // Table model
        tableModel = new CustomTableModel<>(
            Service.class,
            List.of(
                "ID",
                "Название",
                "Стоимость",
                "Период",
                "Описание",
                "Скидка",
                "Изображение"
            )
        );

        tableModel.setRows(Service.all());
        updateTable();


        // Discount ComboBox model

        Map<String, Predicate<Service>> discountFilters = Map.of(
            "Все", s -> true,
            "от 0% до 5%", (Service s) -> inRange(0, 5, s.getDiscount()),
            "от 5% до 15%", (Service s) -> inRange(5, 15, s.getDiscount()),
            "от 15% до 30%", (Service s) -> inRange(15, 30, s.getDiscount()),
            "от 30% до 70%", (Service s) -> inRange(30, 70, s.getDiscount()),
            "от 70% до 100%", (Service s) -> inRange(70, 100, s.getDiscount())
        );

        discountComboBoxModel = new DefaultComboBoxModel<>(
            new String[]{
                "Все",
                "от 0% до 5%",
                "от 5% до 15%",
                "от 15% до 30%",
                "от 30% до 70%",
                "от 70% до 100%",
            }
        );

        discountComboBox.setModel(discountComboBoxModel);

        discountComboBox.addItemListener(l -> {
            var selected = (String) discountComboBox.getSelectedItem();

            tableModel.getFilters().put("discount", discountFilters.get(selected));
            updateTable();
        });
    }

    private void initTable() {
        table.setModel(tableModel);
        table.setRowHeight(30);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    var row = table.getSelectedRow();
                    dispose();
                    new UpdateServiceForm(tableModel.getRowAt(row)).setVisible(true);
                }
            }
        });

    }

    private void initButtons() {

        addButton.addActionListener(
            e -> {
                dispose();
                new CreateServiceForm().setVisible(true);
            }
        );

        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                tableModel.getFilters().put("search", o -> (o.getTitle() + o.getDesc()).contains(searchTextField.getText()));
                updateTable();
            }
        });

        costSortButton.addActionListener(
            e -> {
                descending = !descending;
                tableModel.setSorter(Comparator.comparing(Service::getCost));
                if (descending) tableModel.setSorter(tableModel.getSorter().reversed());
            }
        );

        filterResetButton.addActionListener(
            e -> {

                discountComboBox.setSelectedItem(discountComboBox.getItemAt(0));
                searchTextField.setText("");

                tableModel.getFilters().clear();
                updateTable();
            }
        );

        addClientService.addActionListener(
            e -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    dispose();
                    new ClientServiceCreateForm(tableModel.getRowAt(row)).setVisible(true);
                }
            }
        );

    }

    public void updateTable() {
        tableModel.updateFilteredRows();
        countLabel.setText(tableModel.getRowCount() + " / " + tableModel.getRows().size());
    }
}
