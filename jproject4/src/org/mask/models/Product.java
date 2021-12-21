package org.mask.models;

import lombok.Data;
import org.mask.App;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


@Data
public class Product {
    Integer id;
    String title;
    String type;
    Integer article;
    String desc;
    ImageIcon image;
    Integer productPersonCount;
    Integer productionWorkshopNumber;
    Integer minCostForAgent;

    String imgPath;

    public Product(Integer id,
                   String title,
                   String type,
                   Integer article,
                   String desc,
                   String imgPath,
                   Integer productPersonCount,
                   Integer productionWorkshopNumber,
                   Integer minCostForAgent) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.article = article;
        this.desc = desc;
        this.productPersonCount = productPersonCount;
        this.productionWorkshopNumber = productionWorkshopNumber;
        this.minCostForAgent = minCostForAgent;
        this.imgPath = imgPath;

        try {
            image = new ImageIcon(
                ImageIO.read(Product.class.getClassLoader().getResource(imgPath))
            );

        } catch (IOException e) {
            e.printStackTrace();
            image = null;
        } catch (NullPointerException e) {
            image = null;
        }

    }

    public void save() {
        try {
            var query = App.getConnection().prepareStatement(
                "insert into product (Title, ProductType, ArticleNumber, Description, Image, ProductionPersonCount, ProductionWorkshopNumber, MinCostForAgent) values " +
                    "(?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
            );

            query.execute();

            var set = query.getGeneratedKeys();

            if (set.next()) {
                setId(set.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Product> all() {
        try {
            var query = App.getConnection().prepareStatement(
                "select * from product"
            );

            var set = query.executeQuery();
            var products = new ArrayList<Product>();

            while(set.next()) {
                products.add(
                    new Product(
                        set.getInt("ID"),
                        set.getString("Title"),
                        set.getString("ProductType"),
                        set.getInt("ArticleNumber"),
                        set.getString("Description"),
                        set.getString("Image"),
                        set.getInt("ProductionPersonCount"),
                        set.getInt("ProductionWorkshopNumber"),
                        set.getInt("MinCostForAgent")
                    )
                );
            }

            return products;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
