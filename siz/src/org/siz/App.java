package org.siz;

import org.siz.ui.MaterialTableForm;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        new MaterialTableForm().setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            "jdbc:mysql://192.168.100.2:3306/user27",
            "user27",
            "62462"
        );
    }
}
