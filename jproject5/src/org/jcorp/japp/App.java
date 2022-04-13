package org.jcorp.japp;

import org.jcorp.japp.ui.MaterialListForm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    public static Connection connection;

    public static void main(String[] args) {
        try {
            connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/jdb?serverTimezone=Europe/Moscow",
                "root",
                "1234"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        new MaterialListForm().setVisible(true);
    }
}
