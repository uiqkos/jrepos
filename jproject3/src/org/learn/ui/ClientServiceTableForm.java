package org.learn.ui;

import org.learn.models.Client;
import org.learn.models.ClientService;
import org.learn.models.ClientServiceEntry;
import org.learn.util.CustomTableModel;

import javax.swing.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClientServiceTableForm extends BaseForm {
    private JTable table;
    private JPanel panel;
    private CustomTableModel<ClientServiceEntry> tableModel;

    public ClientServiceTableForm() {
        super("Ближайшие записи", 800, 800);

        setContentPane(panel);

        initModel();
        initTable();
    }

    private void initModel() {
        tableModel = new CustomTableModel<>(
            ClientServiceEntry.class,
            List.of(
                "Название",
                "ФИО Клиента",
                "Email",
                "Телефон",
                "Дата"
            )
        );

        tableModel.setRows(
            ClientService.all().stream().map(ClientServiceEntry::new)
                .collect(Collectors.toList())
        );

        tableModel.getFilters().put(
            "default",
            clientServiceEntry ->
                clientServiceEntry.getDate().toLocalDate().isBefore(LocalDate.now().plusDays(1))
                && clientServiceEntry.getDate().toLocalDate().isAfter(LocalDate.now())
        );

        tableModel.setSorter(Comparator.comparing(ClientServiceEntry::getDate));

        tableModel.updateFilteredRows();

    }

    private void initTable() {
        table.setModel(tableModel);
    }

}
