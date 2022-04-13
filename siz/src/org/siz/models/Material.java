package org.siz.models;


import lombok.Data;
import org.siz.App;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

@Data
public class Material {
    String title;
    String type;
    ImageIcon image;
    Double cost;
    Integer countInStock;
    Integer minCount;
    Integer countInPack;
    String unit;
    String description;

    Integer id;
    String imagePath;

    public Material(Integer id,
                    String title,
                    Integer countInPack,
                    String unit,
                    Integer countInStock,
                    Integer minCount,
                    String description,
                    Double cost,
                    String type,
                    String imagePath) {
        this.id = id;
        this.title = title;
        this.countInPack = countInPack;
        this.unit = unit;
        this.countInStock = countInStock;
        this.minCount = minCount;
        this.description = description;
        this.cost = cost;
        this.type = type;

        this.imagePath = imagePath;

        try {
            var imgPath = imagePath;
            if (imagePath == null || imagePath.isEmpty()) {
                imgPath = "picture.png";
            }
            image = new ImageIcon(
                ImageIO.read(Objects.requireNonNull(Material.class.getClassLoader().getResource(imgPath)))
                    .getScaledInstance(30, 30, 0)
            );
        } catch (Exception e) {
//            e.printStackTrace();

        }

    }

    public static boolean update(Material material) {
        try {
            var query = App.getConnection().prepareStatement(
                "update Material set Title=?, CountInPack=?, Unit=?, CountInStock=?, MinCount=?, Description=?, Cost=?, Image=?, MaterialType=? " +
                    " where id=?"
            );

            query.setString(1, material.title);
            query.setInt(2, material.countInPack);
            query.setString(3, material.unit);
            query.setInt(4, material.countInStock);
            query.setInt(5, material.minCount);
            query.setString(6, material.description);
            query.setDouble(7, material.cost);
            query.setString(8, material.type);
            query.setString(9, material.imagePath);
            query.setInt(10, material.id);

            query.execute();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean remove(Integer id) {
        try {
            var query = App.getConnection().prepareStatement(
                "delete from Material where id=?"
            );

            query.setInt(1, id);

            query.execute();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean save() {
        try {
            var query = App.getConnection().prepareStatement(
                "insert into Material(Title, CountInPack, Unit, CountInStock, MinCount, Description, Cost, Image, MaterialType) values " +
                    "(?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );

            query.setString(1, title);
            query.setInt(2, countInPack);
            query.setString(3, unit);
            query.setInt(4, countInStock);
            query.setInt(5, minCount);
            query.setString(6, description);
            query.setDouble(7, cost);
            query.setString(8, type);
            query.setString(9, imagePath);

            query.execute();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Material> all() {
        try {
            var query = App.getConnection().prepareStatement(
                "select * from Material"
            );

            var set = query.executeQuery();
            var materials = new ArrayList<Material>();

            while (set.next()) {
                materials.add(
                    new Material(
                        set.getInt(1),
                        set.getString(2),
                        set.getInt(3),
                        set.getString(4),
                        set.getInt(5),
                        set.getInt(6),
                        set.getString(7),
                        set.getDouble(8),
                        set.getString(10),
                        set.getString(9)
                    )
                );
            }

            return materials;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
