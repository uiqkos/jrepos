package org.siz.ui;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class BaseForm extends JFrame {
    public BaseForm(String title, int width, int height) {
        setTitle(title);
        setSize(new Dimension(width, height));

        setLocation(new Point(
            Toolkit.getDefaultToolkit().getScreenSize().width / 2 - width / 2,
            Toolkit.getDefaultToolkit().getScreenSize().height / 2 - height / 2
        ));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        try {
            setIconImage(
                ImageIO.read(Objects.requireNonNull(BaseForm.class.getClassLoader().getResource("icon.png")))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
