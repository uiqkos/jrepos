package org.learn.ui;

import javax.swing.*;
import java.awt.*;

public class BaseForm extends JFrame  {
    public BaseForm(String title, int height, int width) throws HeadlessException {

        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(width, height));

        setLocation(new Point(
            Toolkit.getDefaultToolkit().getScreenSize().width / 2 - width / 2,
            Toolkit.getDefaultToolkit().getScreenSize().height / 2 - height / 2
        ));

    }

    public BaseForm(String title) throws HeadlessException {
        this(title, 900, 900);
    }
}
