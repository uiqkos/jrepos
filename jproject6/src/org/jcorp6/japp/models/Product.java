package org.jcorp6.japp.models;

import lombok.Data;
import org.jcorp6.japp.App;

import javax.swing.*;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
        this.imagePath = imagePath;

        try {
            image = new ImageIcon()
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

    public void save() {
        try {
            var query = App.getConnection().prepareStatement(
                "insert into product (Title,ProductType,ArticleNumber,Description,Image,ProductionPersonCount,ProductionWorkshopNumber,MinCostForAgent)" +
                    "values (?,?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS
            );

            query.setString(1, getTitle());
            query.setString(2, getProductType());
            query.setString(3, getArticleNumber());
            query.setString(4, getDescription());
            query.setString(5, getImagePath());
            query.setInt(6, getProductionPersonCount());
            query.setDouble(7, getProductionWorkshopNumber());
            query.setDouble(8, getMinCostForAgent());

            var set = query.executeQuery();
            set.next();

            setId(set.getInt(1));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int id) {
        try {
            var query = App.getConnection().prepareStatement(
                "delete from product where ID=?"
            );

            query.setInt(1, id);
            query.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        try {
            var query = App.getConnection().prepareStatement(
                "update product set Title=?," +
                    "ProductType=?," +
                    "ArticleNumber=?," +
                    "Description=?," +
                    "Image=?," +
                    "ProductionPersonCount=?," +
                    "ProductionWorkshopNumber=?," +
                    "MinCostForAgent=? " +
                    "where ID=?",
                Statement.RETURN_GENERATED_KEYS
            );

            query.setString(1, getTitle());
            query.setString(2, getProductType());
            query.setString(3, getArticleNumber());
            query.setString(4, getDescription());
            query.setString(5, getImagePath());
            query.setInt(6, getProductionPersonCount());
            query.setDouble(7, getProductionWorkshopNumber());
            query.setDouble(8, getMinCostForAgent());
            query.setInt(9, id);

            var set = query.executeQuery();
            set.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
