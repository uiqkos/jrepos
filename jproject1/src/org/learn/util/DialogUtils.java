package org.learn.util;

import javax.swing.*;
import java.awt.*;

public class DialogUtils {
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Информация", JOptionPane.INFORMATION_MESSAGE);
    }
}
