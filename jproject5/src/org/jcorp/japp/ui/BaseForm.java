package org.jcorp.japp.ui;


import javax.swing.*;
import java.awt.*;

public class BaseForm extends JFrame {
    public BaseForm(String title, Integer height, Integer width) {
        super(title);
        setSize(width, height);
        setLocation(
            Toolkit.getDefaultToolkit().getScreenSize().width / 2 - width / 2,
            Toolkit.getDefaultToolkit().getScreenSize().height / 2 - height / 2
        );

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(title);
    }
}
