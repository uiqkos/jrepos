package org.jcorp6.japp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jcorp6.japp.App;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class MaterialProduct {
    String productTitle;
    String productType;
    String materialTitle;
    String materialType;
    Double materialCost;
    Integer count;

    public static List<MaterialProduct> all(int productID) {
        var materialProducts = new ArrayList<MaterialProduct>();
        try {
            var query = App.getConnection().prepareStatement(
                "select p.Title, p.ProductType, m.Title, m.MaterialType, m.Cost, pm.Count from productmaterial pm " +
                    "join product p on p.ID = pm.ProductID " +
                    "join material m on m.ID = pm.MaterialID " +
                    "where p.ID=?"
            );

            query.setInt(1, productID);

            var set = query.executeQuery();

            while (set.next()) {
                materialProducts.add(
                    new MaterialProduct(
                        set.getString(1),
                        set.getString(2),
                        set.getString(3),
                        set.getString(4),
                        set.getDouble(5),
                        set.getInt(6)
                    )
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return materialProducts;
    }

}
