package org.jcomany.models;

import lombok.Data;
import org.jcomany.App;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Material {
    Integer id;
    String title;
    Integer CountInPack;
    String unit;
    Integer countInStock;
    Integer minCount;
    String description;
    Integer cost;
    String materialType;
    ImageIcon image;

    String imagePath;

    public Material(Integer id,
                    String title,
                    Integer countInPack,
                    String unit,
                    Integer countInStock,
                    Integer minCount,
                    String description,
                    Integer cost,
                    String materialType,
                    String imagePath) throws IOException {
        this.id = id;
        this.title = title;
        CountInPack = countInPack;
        this.unit = unit;
        this.countInStock = countInStock;
        this.minCount = minCount;
        this.description = description;
        this.cost = cost;
        this.materialType = materialType;
        this.imagePath = imagePath;

        try {
            this.image = new ImageIcon(ImageIO.read(Objects.requireNonNullElse(
                Material.class.getClassLoader().getResource(
                    Objects.requireNonNullElse(imagePath, "default.png")
                ),
                Material.class.getClassLoader().getResource("default.png"))
            ));
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            this.image = null;
        }
    }

    public boolean save() {
        try {
            var query = App.getConnection().prepareStatement(
                "insert into material (Title, CountInPack, Unit, CountInStock, MinCount, Description, Cost, MaterialType, Image)" +
                    " values (?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            query.setString(1, getTitle());
            query.setInt(2, getCountInPack());
            query.setString(3, getUnit());
            query.setInt(4, getCountInStock());
            query.setInt(5, getMinCount());
            query.setString(6, getDescription());
            query.setInt(7, getCost());
            query.setString(8, getMaterialType());
            query.setString(9, getImagePath());

            query.execute();

            var keys = query.getGeneratedKeys();
            if (keys.next()) {
                setId(keys.getInt(1));
            }
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public static List<Material> all() {
        try {
            var query = App.getConnection().prepareStatement(
                "select * from material"
            );

            var materials = new ArrayList<Material>();

            var set = query.executeQuery();

            while (set.next()) {
                var material = new Material(
                    set.getInt(1),
                    set.getString(2),
                    set.getInt(3),
                    set.getString(4),
                    set.getInt(5),
                    set.getInt(6),
                    set.getString(7),
                    set.getInt(8),
                    set.getString(9),
                    set.getString(10)
                );

                materials.add(material);

            }

            return materials;

        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            return new ArrayList<>();
        }
    }

}
