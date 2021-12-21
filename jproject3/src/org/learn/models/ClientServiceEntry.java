package org.learn.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.learn.App;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ClientServiceEntry {
    String serviceTitle;
    String fio;
    String email;
    String phone;
    Date date;
    private ClientService clientService;

    public ClientServiceEntry(ClientService clientService) {
        this.clientService = clientService;
        serviceTitle = clientService.service.getTitle();
        fio = clientService.client.firstName + " " + clientService.client.lastName + " " + clientService.client.patronymic;
        email = clientService.client.email;
        phone = clientService.client.phone;
        date = clientService.client.registrationDate;
    }

    public static List<String> allFIOs() {
        try {
            var query = App.getConnection().prepareStatement(
                "select FirstName + ' '+ LastName + ' ' + Patronymic from client"
            );

            var set = query.executeQuery();

            var fios = new ArrayList<String>();

            while (set.next()) {
                fios.add(
                    set.getString(1)
                );
            }

            return fios;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new ArrayList<>();
        }
    }


}
