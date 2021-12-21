package org.learn;

import org.learn.ui.ClientServiceTableForm;
import org.learn.ui.ServiceTableForm;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.function.Function;

public class App {
    public static void main(String[] args) {
        new ServiceTableForm().setVisible(true);
//
//        new ClientServiceTableForm().setVisible(true);

//        Function<String, Integer> cls = o -> o.length();



    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/service",
            "root",
            "1234"
        );
    }
}
