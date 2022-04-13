package org.jcorp6.japp.models;

import lombok.Data;
import org.jcorp6.japp.App;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Product {
    String title;
    String articleNumber;
    Double minCostForAgent;
    ImageIcon image;
    String productType;
    String description;
    Integer productionPersonCount;
    Integer productionWorkshopNumber;

    Integer id;
    String imagePath;

    public Product(Integer id,
                   String title,
                   String productType,
                   String articleNumber,
                   String description,
                   String imagePath,
                   Integer productionPersonCount,
                   Integer productionWorkshopNumber,
                   Double minCostForAgent) {
        this.id = id;
        this.title = title;
        this.productType = productType;
        this.articleNumber = articleNumber;
        this.description = description;
        this.productionPersonCount = productionPersonCount;
        this.productionWorkshopNumber = productionWorkshopNumber;
        this.minCostForAgent = minCostForAgent;

        if (imagePath == null || imagePath.isBlank()) {
            imagePath = "picture.png";
        } else {
            imagePath = imagePath.replace('\\', '/').substring(1);
        }

        this.imagePath = imagePath;

        try {
            image = new ImageIcon(
            ImageIO.read(
                Objects.requireNonNullElse(
                    Product
                        .class
                        .getClassLoader()
                        .getResource(imagePath),
                    Product
                        .class
                        .getClassLoader()
                        .getResource("picture.png")
                    )
                )
                .getScaledInstance(30, 30, 0)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Product(String title,
                   String productType,
                   String articleNumber,
                   String description,
                   Integer productionPersonCount,
                   Integer productionWorkshopNumber,
                   Double minCostForAgent,
                   String imagePath) {
        this(
            null,
            title,
            productType,
            articleNumber,
            description,
            imagePath,
            productionPersonCount,
            productionWorkshopNumber,
            minCostForAgent
        );
    }


    public static List<Product> all() {
        var products = new ArrayList<Product>();
        try {
            var query = App.getConnection().prepareStatement(
                "select * from product"
            );

            var set = query.executeQuery();

            while (set.next()) {
                products.add(
                    new Product(
                        set.getInt(1),
                        set.getString(2),
                        set.getString(3),
                        set.getString(4),
                        set.getString(5),
                        set.getString(6),
                        set.getInt(7),
                        set.getInt(8),
                        set.getDouble(9)
                    )
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public void save() throws SQLException {
        var query = App.getConnection().prepareStatement(
            "replace into product values (?,?,?,?,?,?,?,?,?) "
        );
        query.setObject(1, getId(), Types.INTEGER);
//        query.setInt(1, getId());
        query.setString(2, getTitle());
        query.setString(3, getProductType());
        query.setString(4, getArticleNumber());
        query.setString(5, getDescription());
        query.setString(6, getImagePath());
        query.setInt(7, getProductionPersonCount());
        query.setDouble(8, getProductionWorkshopNumber());
        query.setDouble(9, getMinCostForAgent());

        query.execute();
    }

    public static void delete(int id) throws SQLException {
        var query = App.getConnection().prepareStatement(
            "delete from product where ID=?"
        );

        query.setInt(1, id);
        query.execute();
    }
}
