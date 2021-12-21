package org.learn.ui;

import javax.swing.*;
import java.util.Objects;

public class LoginForm extends BaseForm {

    private JTextField loginTextField;
    private JTextField passwordTextField;
    private JButton loginButton;
    private JLabel passwordLabel;
    private JLabel loginLabel;
    private JPanel panel;

    public LoginForm() {
        super("Login");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setContentPane(panel);

        loginButton.addActionListener(
            e -> {
                if (passwordTextField.getText() == null || !Objects.equals(passwordTextField.getText(), "0000")) return;

                var tableForm = new ServiceTableFrom();
                tableForm.setVisible(true);
            }
        );

    }
}
