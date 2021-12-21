package org.learn.ui;

import org.learn.models.Service;
import org.learn.util.DialogUtils;

import javax.swing.*;

public class CreateServiceForm extends BaseForm {
    private JTextField titleTextField;
    private JSpinner durationSpinner;
    private JTextArea descTextArea;
    private JTextField imgPathTextField;
    private JPanel panel;
    private JButton addButton;
    private JTextField discountField;
    private JTextField costField;

    public CreateServiceForm() {
        super("Добавить услугу", 800, 800);

        setContentPane(panel);

        initButtons();

    }

    private void initButtons() {
        addButton.addActionListener(
            e -> {
                var title = titleTextField.getText();

                if (title.isEmpty()) {
                    DialogUtils.showError(this, "Название не может быть пустым");
                    return;
                }

                if (Service.all().stream().map(Service::getTitle).anyMatch(title::equals)) {
                    DialogUtils.showError(this, "Такое название уже существует");
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
                    null,
                    title, cost, duration, desc, discount, imgPath
                ).save();

                dispose();
                new ServiceTableForm().setVisible(true);
            }
        );
    }

}
