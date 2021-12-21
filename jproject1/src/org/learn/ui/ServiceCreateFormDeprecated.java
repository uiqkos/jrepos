package org.learn.ui;

import org.learn.models.Service;
import org.learn.util.DialogUtils;

import javax.swing.*;
import java.awt.*;

public class ServiceCreateFormDeprecated extends BaseForm {
    private JTextField nameTextField;
    private JTextField costTextField;
    private JTextField discountTextField;
    private JTextField descriptionTextField;
    private JTextField imgPathTextField;
    private JSpinner durationSpinner;
    private JLabel nameLabel;
    private JPanel panel;
    private JButton createButton;

    public ServiceCreateFormDeprecated() throws HeadlessException {
        super("Создать услугу");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setContentPane(panel);

        createButton.addActionListener(
            e -> {
                var title = nameTextField.getText();
                var cost = 0;
                var duration = 0;
                var discount = 0.0;

                if (title.isEmpty() || title.length() > 100) {
                    DialogUtils.showError(this, "Неверно введено название");
                    return;
                }

                try {
//                    duration = Integer.parseInt(durationSpinner.getValue());
                } catch (Exception ex) {
                    DialogUtils.showError(this, "Неверно введен период");
                    return;
                }
                try {
                    cost = Integer.parseInt(costTextField.getText());
                } catch (Exception ex) {
                    DialogUtils.showError(this, "Неверно введена цена");
                    return;
                }

                try {
                    discount = Double.parseDouble(discountTextField.getText());
                } catch (Exception ex) {
                    DialogUtils.showError(this, "Неверно введена скидка");
                    return;
                }

                var description = descriptionTextField.getText();
                var imgPath = imgPathTextField.getText();

                if (imgPath.isEmpty()) {
                    DialogUtils.showError(this, "Неверно введен путь до картинки");
                    return;
                }

                if (new Service(title, cost, duration, description, discount, imgPath).save()) {
                    dispose();
                    var f = new ServiceTableFrom();
                    f.setVisible(true);
                    return;
                }

                DialogUtils.showError(this,"Ошибка сохранения услуги");
            }
        );

    }

}
