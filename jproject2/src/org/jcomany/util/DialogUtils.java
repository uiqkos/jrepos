package org.jcomany.util;

import javax.swing.*;
import java.awt.*;

public class DialogUtils {
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, "Ошибка " + message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
}
