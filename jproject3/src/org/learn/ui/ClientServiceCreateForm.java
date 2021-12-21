package org.learn.ui;

import org.learn.models.Client;
import org.learn.models.ClientService;
import org.learn.models.ClientServiceEntry;
import org.learn.models.Service;
import org.learn.util.DialogUtils;

import javax.swing.*;
import java.sql.Date;

public class ClientServiceCreateForm extends BaseForm {
    private final Service service;
    private JPanel panel;
    private JLabel titleLabel;
    private JLabel durationLabel;
    private JComboBox<Object> clientsComboBox;
    private JTextField dateTextField;
    private JButton saveButton;

    public ClientServiceCreateForm(Service service) {
        super("Записать клиента на услугу", 800, 800);

        setContentPane(panel);
        this.service = service;

        initFields();
        initButtons();

    }

    private void initFields() {
        titleLabel.setText(service.getTitle());
        durationLabel.setText(String.valueOf(service.getDuration()));

        clientsComboBox.setModel(new DefaultComboBoxModel<>(ClientServiceEntry.allFIOs().toArray(String[]::new)));
    }

    private void initButtons() {
        saveButton.addActionListener(
            e -> {
                Date date = null;
                try {
                    date = Date.valueOf(dateTextField.getText());
                } catch (Exception ex) {
                    DialogUtils.showError(this, "Неверный формат даты");
                    return;
                }

                new ClientService(
                    null,
                    new Client(Client.clientIdByFIO((String) clientsComboBox.getSelectedItem()), null, null, null, null, null, null, null, null, null),
                    service,
                    date,
                    ""
                ).save();

            }
        );
    }


}
