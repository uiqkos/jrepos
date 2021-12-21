package org.learn.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.learn.App;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class Service {
    private String title;
    private Integer cost;
    private Integer durationInSeconds;
    private String description;
    private Double discount;
    private ImageIcon image;
    private String mainImagePath;

    public Service(String title, Integer cost, Integer durationInSeconds, String description, Double discount, String mainImagePath) {
        this.title = title;
        this.cost = cost;
        this.durationInSeconds = durationInSeconds;
        this.description = description;
        this.discount = discount;
        this.mainImagePath = mainImagePath;

        updateImage();
    }

    private void updateImage() {
        try {
            this.image = new ImageIcon(
                ImageIO.read(
                    Objects.requireNonNullElse(
                        Service.class
                            .getClassLoader()
                            .getResourceAsStream(Objects.requireNonNullElse(mainImagePath, "icoco.png")),
                        Service.class
                            .getClassLoader()
                            .getResourceAsStream("icoco.png")
                    )
                ).getScaledInstance(50, 50, Image.SCALE_DEFAULT)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean save() {
        try {
            var connection = App.getConnection();

            var query = connection.prepareStatement(
                "insert into service.service (Title, Cost, DurationInSeconds, Description, Discount, MainImagePath) " +
                    "values (?, ?, ?, ?, ?, ?)"
            );

            query.setString(1, getTitle());
            query.setInt(2, getCost());
            query.setInt(3, getDurationInSeconds());
            query.setString(4, getDescription());
            query.setDouble(5, getDiscount());
            query.setString(6, getMainImagePath());

            query.execute();

            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean update(int id, Service service) {
        try {
            var connection = App.getConnection();

            var query = connection.prepareStatement(
                "update service.service set Title = ?, Cost = ?, DurationInSeconds = ?, Description = ?, Discount = ?, MainImagePath = ? " +
                    "where ID = ?"
            );

            query.setString(1, service.getTitle());
            query.setInt(2, service.getCost());
            query.setInt(3, service.getDurationInSeconds());
            query.setString(4, service.getDescription());
            query.setDouble(5, service.getDiscount());
            query.setString(6, service.getMainImagePath());
            query.setInt(7, id);

            query.execute();

            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public static boolean delete(int id) {
        try {
            var connection = App.getConnection();

            var query = connection.prepareStatement(
                "delete from service.service where ID = ?"
            );

            query.setInt(1, id);

            query.execute();

            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public static List<Service> all() {
        try {
            var connection = App.getConnection();

            var query = connection.prepareStatement(
                "select * from service.service"
            );

            var querySet = query.executeQuery();
            var services = new ArrayList<Service>();

            while (querySet.next()) {
                services.add(
                    new Service(
                        querySet.getString(2),
                        querySet.getInt(3),
                        querySet.getInt(4),
                        querySet.getString(5),
                        querySet.getDouble(6),
                        querySet.getString(7)
                    )
                );
            }

            return services;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return List.of();
        }
    }

}
