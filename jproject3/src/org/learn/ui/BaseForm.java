package org.learn.ui;

import javax.swing.*;
import java.awt.*;

public class BaseForm extends JFrame {
    public BaseForm(String title, int height, int width) {
        super(title);
        setSize(new Dimension(width, height));
        setLocation(new Point(
            Toolkit.getDefaultToolkit().getScreenSize().width / 2 - width / 2,
            Toolkit.getDefaultToolkit().getScreenSize().height / 2 - height / 2
        ));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(title);
    }
}
