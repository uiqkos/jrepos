package org.learn.models;

import lombok.Data;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.sql.Date;
import java.util.Objects;

@Data
public class Client {
    private String firstName;
    private String lastName;
    private String patronymic;
    private Date birthdate;
    private Date registrationDate;
    private String email;
    private String phone;
    private Character gender;
    private String photoPath;

    private ImageIcon photo;

    public Client(String firstName,
                  String lastName,
                  String patronymic,
                  Date birthdate,
                  Date registrationDate,
                  String email,
                  String phone,
                  Character gender,
                  String photoPath) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.birthdate = birthdate;
        this.registrationDate = registrationDate;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.photoPath = photoPath;

        try {
            photo = new ImageIcon(
                ImageIO.read(
                    Objects.requireNonNull(
                        Client.class.getClassLoader().getResource(photoPath))
                )
            );
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

    }
}
