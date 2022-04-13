package org.siz.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.siz.App;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Supplier {
    Integer id;
    String title;

    public static List<Supplier> all() {
        try {
            var query = App.getConnection().prepareStatement(
                "select s.ID, s.Title from Supplier s"
            );

            var set = query.executeQuery();
            var suppliers = new ArrayList<Supplier>();

            while (set.next()) {
                suppliers.add(
                    new Supplier(set.getInt("s.ID"), set.getString("s.Title"))
                );
            }

            return suppliers;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
