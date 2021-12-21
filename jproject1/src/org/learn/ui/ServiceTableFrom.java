package org.learn.ui;

import org.learn.models.Service;
import org.learn.util.CustomTableModel;

import javax.swing.*;
import java.util.Comparator;
import java.util.List;

public class ServiceTableFrom extends BaseForm {
    
    private CustomTableModel<Service> model;
    private JTable table;
    private JPanel panel;
    private JButton createServiceButton;
    private JButton sortByNameButton;
    private JButton sortByCostButton;

    private JButton lastSortClickedButton;

    public ServiceTableFrom(){
        super("Services");

        setContentPane(panel);

        initModel();
        initTable();
        initButtons();
        
    }

    private void initButtons() {
        createServiceButton.addActionListener(
            e -> {
                dispose();
                var f = new ServiceCreateForm();
                f.setVisible(true);
            }
        );

        sortByCostButton.addActionListener(
            e -> {
                if (lastSortClickedButton == sortByCostButton) {
                    model.setSorter(model.getSorter().reversed());
                } else {
                    model.setSorter(Comparator.comparing(Service::getCost));
                }
                lastSortClickedButton = sortByCostButton;
                model.updateFilteredRows();
            }
        );

        sortByNameButton.addActionListener(
            e -> {
                if (lastSortClickedButton == sortByNameButton) {
                    model.setSorter(model.getSorter().reversed());
                } else {
                    model.setSorter(Comparator.comparing(Service::getTitle));
                }
                lastSortClickedButton = sortByNameButton;
                model.updateFilteredRows();
            }
        );

    }

    private void initModel() {
        model = new CustomTableModel<>(
            Service.class, 
            List.of(
                "Заголовок",
                "Стоимость",
                "Длительность",
                "Описание",
                "Скидка",
                "Путь до картинки",
                "Картинка"
            )
        );
        
        model.setRows(Service.all());
        model.updateFilteredRows();
    }


    private void initTable() {
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(50);
        table.setModel(model);
    }
    
}
