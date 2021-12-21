package org.learn.models;

import lombok.Data;
import org.learn.App;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Service {
    Integer id;
    String title;
    Double cost;
    Integer duration;
    String desc;
    Double discount;
    ImageIcon image;

    String imgPath;

    public Service(Integer id,
                   String title,
                   Double cost,
                   Integer duration,
                   String desc,
                   Double discount,
                   String imgPath) {
        this.id = id;
        this.title = title;
        this.cost = cost;
        this.duration = duration;
        this.desc = desc;
        this.discount = discount;
        this.imgPath = imgPath;

        if (imgPath != null && !imgPath.isEmpty()) {
            try {
                image = new ImageIcon(
                    ImageIO.read(Objects.requireNonNull(Service.class.getClassLoader().getResource(imgPath))).getScaledInstance(30, 30, 0)
                );
            } catch (IOException e) {
                e.printStackTrace();
                image = null;
            } catch (NullPointerException ex) {
                image = null;
            }
        }
    }

    public boolean save() {
        try {
            var query = App.getConnection().prepareStatement(
                "insert into service (Title, Cost, DurationInSeconds, Description, Discount, MainImagePath) values (?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS
            );

            query.setString(1,getTitle());
            query.setDouble(2, getCost());
            query.setInt(3, getDuration());
            query.setString(4, getDesc());
            query.setDouble(5, getDiscount());
            query.setString(6, getImgPath());

            query.execute();

            var set = query.getGeneratedKeys();
            if (set.next()) {
                setId(set.getInt(1));
            }

            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public static List<Service> all() {
        try {
            var query = App.getConnection().prepareStatement(
                "select * from service"
            );

            var set = query.executeQuery();
            var services = new ArrayList<Service>();

            while (set.next()) {
                services.add(
                    new Service(
                        set.getInt("ID"),
                        set.getString("Title"),
                        set.getDouble("Cost"),
                        set.getInt("DurationInSeconds"),
                        set.getString("Description"),
                        set.getDouble("Discount"),
                        set.getString("mainImagePath")
                    )
                );
            }

            return services;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean update () {
        try {
            var query = App.getConnection().prepareStatement(
                "update service set Title=?, Cost=?, DurationInSeconds=?, Description=?, Discount=?, MainImagePath=? where id = ?",
                Statement.RETURN_GENERATED_KEYS
            );

            query.setString(1, getTitle());
            query.setDouble(2, getCost());
            query.setInt(3, getDuration());
            query.setString(4, getDesc());
            query.setDouble(5, getDiscount());
            query.setString(6, getImgPath());
            query.setInt(7, getId());

            query.execute();

            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public Double getCostWithDiscount() {
        return (cost * 100 - discount) / 100;
    }
}
