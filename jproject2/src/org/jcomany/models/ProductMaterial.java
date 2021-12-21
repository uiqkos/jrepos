/*
package org.jcomany.models;

import lombok.Data;
import org.jcomany.App;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class ProductMaterial {
    Product product;
    Material material;
    Integer count;

    public ProductMaterial(Product product, Material material, Integer count) {
        this.product = product;
        this.material = material;
        this.count = count;
    }

    public ProductMaterial(Integer productId, Integer materialId, Integer count) {
        this.product = Product.all().stream().filter(p -> Objects.equals(p.getId(), productId)).findFirst().orElse(null);
        this.material = Material.all().stream().filter(m -> Objects.equals(m.getId(), materialId)).findFirst().orElse(null);
        this.count = count;
    }
*/
/*
    public static List<Material> materialsByProductId(int id) {
        try {
            var query = App.getConnection().prepareStatement(
                "select * from material where ID in (select MaterialID from productmaterial where ProductID = ?)"
            );

            query.setInt(1, id);

            var set = query.executeQuery();

            var materials = new ArrayList<Material>();

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
    *//*

    public static List<ProductMaterial> all() {
        try {
            var query = App.getConnection().prepareStatement(
                "select m.*, p.*, Count from productmaterial " +
                    "inner join material m on productmaterial.MaterialID = m.ID " +
                    "inner join product p on productmaterial.ProductID = p.ID"
            );

            var set = query.executeQuery();
            var productMaterials = new ArrayList<ProductMaterial>();

            while (set.next()) {
                productMaterials.add(
                    new ProductMaterial(
                        set.getInt(1),
                        new Product(
                            set.getInt(1),
                            set.getString(2),
                            set.getString(3),
                            set.getInt(4),
                            set.getString(5),
                            set.getInt(7),
                            set.getInt(8),
                            set.getInt(9),
                            set.getString(6)
                        ),
                        new Material(
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
                        ),
                    )
                );
            }

            return productMaterials;

        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            return new ArrayList<>();
        }
    }

}
*/
