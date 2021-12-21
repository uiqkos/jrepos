package org.learn.ui;

import org.learn.models.Service;
import org.learn.ui.BaseForm;
import org.learn.ui.ServiceTableForm;
import org.learn.util.DialogUtils;

import javax.swing.*;
import java.util.Objects;

public class UpdateServiceForm extends BaseForm {
    private final Service service;
    private JTextField titleTextField;
    private JSpinner durationSpinner;
    private JTextArea descTextArea;
    private JTextField imgPathTextField;
    private JPanel panel;
    private JButton addButton;
    private JTextField discountField;
    private JTextField costField;
    private JLabel Id;
    private JLabel idLabel;

    public UpdateServiceForm(Service service) {
        super("Изменить услугу", 800, 800);

        this.service = service;

        setContentPane(panel);

        initFields();
        initButtons();

    }

    private void initFields() {
        idLabel.setText(String.valueOf(service.getId()));
        titleTextField.setText(service.getTitle());
        durationSpinner.setValue(service.getDuration());
        descTextArea.setText(service.getDesc());
        imgPathTextField.setText(Objects.requireNonNullElse(service.getImgPath(), ""));
        discountField.setText(String.valueOf(service.getDiscount()));
        costField.setText(String.valueOf(service.getCost()));
    }

    private void initButtons() {
        addButton.addActionListener(
            e -> {
                var title = titleTextField.getText();

                if (title.isEmpty()) {
                    DialogUtils.showError(this, "Название не может быть пустым");
                    return;
                }

                var cost = 0.0;
                try {
                    cost = Double.parseDouble(costField.getText());
                } catch (Exception ex) {
                    DialogUtils.showError(this, "Неверный формат стоимости " + ex.getMessage());
                    return;
                }

                if (cost < 0 ) {
                    DialogUtils.showError(this, "Стоимость не может быть меньше 0");
                    return;
                }

                var duration = (Integer) durationSpinner.getValue();
                if (duration < 0 ) {
                    DialogUtils.showError(this, "Длительность не может быть меньше 0");
                    return;
                }
                if (duration > 4) {
                    DialogUtils.showError(this, "Длительность не может быть больше 4 часов");
                    return;
                }

                var desc = descTextArea.getText();

                var discount = 0.0;
                try {
                    discount = Double.parseDouble(discountField.getText());
                } catch (Exception ex) {
                    DialogUtils.showError(this, "Неверный формат скидки " + ex.getMessage());
                    return;
                }
                if (discount < 0 || discount >= 100) {
                    DialogUtils.showError(this, "Скидка не может быть меньше 0 или больше 99");
                    return;
                }

                var imgPath = imgPathTextField.getText();
                new Service(
                    service.getId(),
                    title, cost, duration, desc, discount, imgPath
                ).update();

                dispose();
                new ServiceTableForm().setVisible(true);
            }
        );
    }

}
