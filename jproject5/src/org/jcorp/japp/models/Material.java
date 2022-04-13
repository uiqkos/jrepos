package org.jcorp.japp.models;

import lombok.NoArgsConstructor;
import org.jcorp.japp.App;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Material {
    String title;
    String materialType;
    ImageIcon image;
    Double cost;
    Integer countInStock;
    Double minCount;
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
                    Double minCount,
                    String description,
                    Double cost,
                    String imagePath,
                    String materialType) {
        this.id = id;
        this.title = title;
        this.countInPack = countInPack;
        this.unit = unit;
        this.countInStock = countInStock;
        this.minCount = minCount;
        this.description = description;
        this.cost = cost;
        this.materialType = materialType;
        this.imagePath = imagePath;

    }

    public Material(String title,
                    Integer countInPack,
                    String unit,
                    Integer countInStock,
                    Double minCount,
                    String description,
                    Double cost,
                    String materialType,
                    String imagePath) {
        this(null, title,
            countInPack,
            unit,
            countInStock,
            minCount,
            description,
            cost,
            materialType,
            imagePath);
    }

    public static List<Material> all() {
        PreparedStatement query = null;
        var materials = new ArrayList<Material>();

        try {
            query = App.connection.prepareStatement(
                "select * from material;"
            );

            var set = query.executeQuery();

            while (set.next()) {
                var material = new Material(
                    set.getInt("ID"),
                    set.getString("Title"),
                    set.getInt("CountInPack"),
                    set.getString("Unit"),
                    set.getInt("CountInStock"),
                    set.getDouble("MinCount"),
                    set.getString("Description"),
                    set.getDouble("Cost"),
                    set.getString("Image"),
                    set.getString("MaterialType")
                );

                materials.add(material);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return materials;
    }

    public Material save() throws SQLException {
        var query = App.connection.prepareStatement(
            "insert into material values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );

        query.setInt(1, id);
        query.setString(2, title);
        query.setInt(3, countInPack);
        query.setString(4, unit);
        query.setInt(5, countInStock);
        query.setDouble(6, minCount);
        query.setString(7, description);
        query.setDouble(8, cost);
        query.setString(9, imagePath);
        query.setString(10, materialType);
    }

    static public void deleteById(Integer id) {
        try {
            var query = App.connection.prepareStatement(
                "delete from material where ID=?"
            );

            query.setInt(1, id);
            query.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
