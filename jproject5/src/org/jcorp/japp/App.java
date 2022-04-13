package org.jcorp.japp;

import org.jcorp.japp.ui.MaterialListForm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        new MaterialListForm().setVisible(true);
    }

    static public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            "jdbc:mysql://127.0.0.1:3306/jdb?serverTimezone=Europe/Moscow",
            "root",
            "1234"
        );
    }
}
