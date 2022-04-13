package org.siz.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.siz.App;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class MaterialSupplier {
    String name;
    String type;
    Integer rating;
    String comment;

    Integer materialId;
    Integer supplierId;

    public static List<MaterialSupplier> all() {
        try {
            var query = App.getConnection().prepareStatement(
                "select s.Title as name, s.SupplierType as type, s.QualityRating as rating, ms.ContractComment as comment, " +
                    "m.ID as mId, s.ID as sId " +
                    "from MaterialSupplier ms " +
                    "inner join Material m on m.ID = ms.MaterialID " +
                    "inner join Supplier s on s.ID = ms.SupplierID"
            );

            var set = query.executeQuery();
            ArrayList<MaterialSupplier> materials = new ArrayList<>();

            while (set.next()) {
                materials.add(
                    new MaterialSupplier(
                        set.getString("name"),
                        set.getString("type"),
                        set.getInt("rating"),
                        set.getString("comment"),
                        set.getInt("mId"),
                        set.getInt("sId")
                    )
                );
            }

            return materials;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void insert(Integer materialId, Integer supplierId, String comment) {
        try {
            var query = App.getConnection().prepareStatement(
                "insert into MaterialSupplier(MaterialID, SupplierID, ContractComment) values " +
                    "(?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );

            query.setInt(1, materialId);
            query.setInt(2, supplierId);
            query.setString(3, comment);

            query.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
