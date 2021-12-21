package org.jcomany.models;

import lombok.Data;
import org.jcomany.App;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class Product {
    Integer id;
    String title;
    String productType;
    Integer articleNumber;
    String description;
    Integer productionPersonCount;
    Integer productionWorkshopNumber;
    Integer minCostForAgent;
    ImageIcon image;
    Integer cost;

    String imagePath;

    public Product(Integer id,
                   String title,
                   String productType,
                   Integer articleNumber,
                   String description,
                   Integer productionPersonCount,
                   Integer productionWorkshopNumber,
                   Integer minCostForAgent,
                   String imagePath) {
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
            image = new ImageIcon(ImageIO.read(Objects.requireNonNullElse(
                Product.class.getClassLoader().getResource(
                    Objects.requireNonNullElse(imagePath, "default.png")
                ),
                Product.class.getClassLoader().getResource("default.png"))
            ).getScaledInstance(30, 30, 0));

        } catch (NullPointerException | IOException ex) {
            ex.printStackTrace();
            this.image = null;
        }

    }

    public void updateCost() {
        if (id != null) {
            try {
                var query = App.getConnection().prepareStatement(
                    "select m.Cost, pm.Count from productmaterial as pm " +
                        "inner join material m on pm.MaterialID = m.ID " +
                        "where pm.ProductID = ?"
                );

                query.setInt(1, getId());

                var set = query.executeQuery();
                var cost = 0;

                while (set.next()) {
                    cost += set.getInt(1) * set.getInt(2);
                }

                setCost(cost);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
                setCost(null);
            }
        }
    }

    public boolean save() {
        try {
            var query = App.getConnection().prepareStatement(
                "insert into product (Title, ProductType, ArticleNumber, Description, Image, ProductionPersonCount, ProductionWorkshopNumber, MinCostForAgent)" +
                    " values (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
            );

            query.setString(1, getTitle());
            query.setString(2, getProductType());
            query.setInt(3, getArticleNumber());
            query.setString(4, getDescription());
            query.setString(5, getImagePath());
            query.setInt(6, getProductionPersonCount());
            query.setInt(7, getProductionWorkshopNumber());
            query.setInt(8, getMinCostForAgent());

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

    public boolean update() {
        try {
            var query = App.getConnection().prepareStatement(
                "update product  set Title=?, ProductType=?, ArticleNumber=?, Description=?, Image=?, ProductionPersonCount=?, ProductionWorkshopNumber=?, MinCostForAgent=? where ID = ?"
            );

            query.setString(1, getTitle());
            query.setString(2, getProductType());
            query.setInt(3, getArticleNumber());
            query.setString(4, getDescription());
            query.setString(5, getImagePath());
            query.setInt(6, getProductionPersonCount());
            query.setInt(7, getProductionWorkshopNumber());
            query.setInt(8, getMinCostForAgent());
            query.setInt(9, getId());

            query.execute();

            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public static List<Product> all() {
        try {
            var query = App.getConnection().prepareStatement(
                "select p.*, ifnull(c.cost, 0) from product as p " +
                    "    left join ( " +
                    "        select " +
                    "               sum(m.cost * p2.Count) as cost, " +
                    "               p2.ProductID as pId " +
                    "        from material as m " +
                    "            inner join productmaterial p2 " +
                    "                on m.ID = p2.MaterialID " +
                    "        group by pId " +
                    "    ) as c on p.ID = c.pId"
            );

            var products = new ArrayList<Product>();

            var set = query.executeQuery();

            while (set.next()) {
                var product = new Product(
                    set.getInt(1),
                    set.getString(2),
                    set.getString(3),
                    set.getInt(4),
                    set.getString(5),
                    set.getInt(7),
                    set.getInt(8),
                    set.getInt(9),
                    set.getString(6)
                );

                product.setCost(set.getInt(7));

                products.add(product);

            }

            return products;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static Set<String> allProductTypes() {
        return Product
            .all()
            .stream()
            .map(Product::getProductType)
            .collect(Collectors.toSet());
    }

}
