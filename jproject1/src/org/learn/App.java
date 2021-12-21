package org.learn;

import org.learn.ui.LoginForm;
import org.learn.ui.ServiceTableFrom;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) {
//        var loginForm = new LoginForm();
//        loginForm.setVisible(true);
        var f = new ServiceTableFrom();
        f.setVisible(true);
//        javaGovno();
    }

    public static Connection getConnection() throws SQLException {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/mysql",
            "root",
            "1234"
        );
    }

    public static void javaGovno() {
        var list = new ArrayList<Supplier<Boolean>>();
        list.add(() -> {
            System.out.println("1");
            return true;
        });
        list.add(() -> {
            System.out.println("2");
            return false;
        });
        list.add(() -> {
            System.out.println("3");
            return true;
        });

        list.stream().anyMatch(o -> !o.get());
    }
}
