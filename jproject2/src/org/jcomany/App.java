package org.jcomany;

import org.jcomany.models.Product;
import org.jcomany.ui.MaterialTableForm;
import org.jcomany.ui.ProductTableForm;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {

    private static Connection con = null;

    public static void main(String[] args) throws IOException {
        var f = new ProductTableForm();
        f.setVisible(true);
    }


    public static Connection getConnection() throws SQLException {
        if (con == null) {
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/jdatabase2",
                "root",
                "1234"
            );
        }
        return con;
    }
}
