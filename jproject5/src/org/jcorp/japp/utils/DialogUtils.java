package org.jcorp.japp.utils;

import javax.swing.*;
import java.awt.*;

public class DialogUtils {
    static public void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(
            parent,
            "Error: " + message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
