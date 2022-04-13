package org.jcorp6.japp.ui;

import javax.swing.*;
import java.awt.*;

public class BaseForm extends JFrame {
    public BaseForm(String title, int height, int width) throws HeadlessException {
        super(title);

        setSize(width, height);
        setLocation(
            Toolkit.getDefaultToolkit().getScreenSize().width / 2 - width / 2,
            Toolkit.getDefaultToolkit().getScreenSize().height / 2 - height / 2
        );

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
