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
            query = App.getConnection().prepareStatement(
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

    public Material save() {
        PreparedStatement query = null;
        try {
            if (id != null) {
                var existsCheckQuery = App.getConnection().prepareStatement(
                    "select * from material where ID=?"
                );

                existsCheckQuery.setInt(1, id);

                var set = existsCheckQuery.executeQuery();

                if (set.next()) {
                    query = App.getConnection().prepareStatement(
                        "update material set" +
                            "Title=?, CountInPack=?, Unit=?, CountInStock=?, MinCount=?, Description=?, Cost=?, Image=?, MaterialType=?" +
                            "where ID=?"
                    );
                    query.setString(1, title);
                    query.setInt(2, countInPack);
                    query.setString(3, unit);
                    query.setInt(4, countInStock);
                    query.setDouble(5, minCount);
                    query.setString(6, description);
                    query.setDouble(7, cost);
                    query.setString(8, imagePath);
                    query.setString(9, materialType);
                    query.setInt(10, id);
                }
            }
            if (query == null) {
                query = App.getConnection().prepareStatement(
                    "insert into material " +
                        "(Title, CountInPack, Unit, CountInStock, MinCount, Description, Cost, Image, MaterialType) " +
                        "values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );

                query.setString(1, title);
                query.setInt(2, countInPack);
                query.setString(3, unit);
                query.setInt(4, countInStock);
                query.setDouble(5, minCount);
                query.setString(6, description);
                query.setDouble(7, cost);
                query.setString(8, imagePath);
                query.setString(9, materialType);
            }

            query.execute();

            if (id == null) {
                var set = query.getGeneratedKeys();
                set.next();

                setId(set.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }

    static public void deleteById(Integer id) {
        try {
            var query = App.getConnection().prepareStatement(
                "delete from material where ID=?"
            );

            query.setInt(1, id);
            query.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
