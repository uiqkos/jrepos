package org.learn.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.learn.App;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data @AllArgsConstructor
public class ClientService {
    Integer id;
    Client client;
    Service service;
    Date startTime;
    String comment;

    public static List<ClientService> all() {
        try {
            var query = App.getConnection().prepareStatement(
                "select cs.*, c.*, s.* from clientservice as cs " +
                    "inner join client c on c.ID = cs.ClientID " +
                    "inner join service s on s.ID = cs.ServiceID"
            );

            var set = query.executeQuery();

            var clientServices = new ArrayList<ClientService>();

            while (set.next()) {
                clientServices.add(
                    new ClientService(
                        set.getInt("cs.id"),
                        new Client(
                            set.getInt("ID"),
                            set.getString("c.FirstName"),
                            set.getString("c.LastName"),
                            set.getString("c.Patronymic"),
                            set.getDate("c.Birthday"),
                            set.getDate("c.RegistrationDate"),
                            set.getString("c.Email"),
                            set.getString("c.Phone"),
                            set.getString("c.GenderCode").toCharArray()[0],
                            set.getString("PhotoPath")
                        ),
                        new Service(
                            set.getInt("s.ID"),
                            set.getString("s.Title"),
                            set.getDouble("s.Cost"),
                            set.getInt("s.DurationInSeconds"),
                            set.getString("s.Description"),
                            set.getDouble("s.Discount"),
                            set.getString("s.MainImagePath")
                        ),
                        set.getDate("cs.StartTime"),
                        set.getString("cs.Comment")
                    )
                );
            }

            return clientServices;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean save() {
        try {
            var query = App.getConnection().prepareStatement(
                "insert into clientservice (ClientID, ServiceID, StartTime, Comment) values (?, ?, ?, ?);"
            );

            query.setInt(1, client.getId());
            query.setInt(2, service.getId());
            query.setDate(3, startTime);
            query.setString(4, comment);

            query.execute();
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

}

