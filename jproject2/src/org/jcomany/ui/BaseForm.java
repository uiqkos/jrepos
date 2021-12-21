package org.jcomany.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class BaseForm extends JFrame {
    public BaseForm(String title, int height, int width) {
        setTitle(title);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(width, height));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setLocation(new Point(
            Toolkit.getDefaultToolkit().getScreenSize().width / 2 - width / 2,
            Toolkit.getDefaultToolkit().getScreenSize().height / 2 - height / 2
        ));

        try {
            setIconImage(
                ImageIO.read(Objects.requireNonNull(BaseForm.class.getClassLoader().getResource("default.png")))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BaseForm(String title) {
        this(title, 800, 800);
    }
}
