package org.learn.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.learn.App;

import java.sql.Date;
import java.sql.SQLException;

@Data
@AllArgsConstructor
public class Client {
    Integer id;
    String firstName;
    String lastName;
    String patronymic;
    Date birthday;
    Date registrationDate;
    String email;
    String phone;
    Character gender;
    String photoPath;

    public Client(String firstName,
                  String lastName,
                  String patronymic,
                  Date birthday,
                  Date registrationDate,
                  String email,
                  String phone,
                  Character gender,
                  String photoPath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.registrationDate = registrationDate;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.photoPath = photoPath;
    }

    public static Integer clientIdByFIO(String fio) {
        try {
            var query = App.getConnection().prepareStatement(
                "select ID, FirstName + ' '+ LastName + ' ' + Patronymic as fio from client " +
                    "where FirstName + ' ' + LastName + ' ' + Patronymic = ? limit 1"
            );

            var set = query.executeQuery();
            set.next();

            return set.getInt(1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -1;
        }
    }
}
