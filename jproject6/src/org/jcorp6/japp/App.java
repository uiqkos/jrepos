package org.jcorp6.japp;

import org.jcorp6.japp.ui.ProductsForm;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        var mainForm = new ProductsForm();
        mainForm.setVisible(true);
        mainForm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/jdb6",
            "root",
            "1234"
        );
    }

}
