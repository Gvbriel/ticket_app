package com.gabrielpolak.ticket.model.DAO;

import com.gabrielpolak.ticket.exceptions.WrongUserDataException;
import com.gabrielpolak.ticket.validators.PatternValidator;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String surname;
    private String email;

    @OneToMany
    private List<Reservation> reservationList;

    public static User createNewUser(String name, String surname, String email) {
        return new User(name, surname, email);
    }

    public static User createDefaultUser() {
        return new User();
    }

    public User(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public User() {

    }

    public void setName(String name) {
        if (PatternValidator.validateName(name)) {
            this.name = name;
        } else {
            throw new WrongUserDataException("Name is not correct!");
        }
    }

    public void setSurname(String surname) {
        if (PatternValidator.validateSurname(surname)) {
            this.surname = surname;
        } else {
            throw new WrongUserDataException("Surname is not correct!");
        }
    }

    public void setEmail(String email) {
        if (PatternValidator.validateEmail(email)) {
            this.email = email;
        } else {
            throw new WrongUserDataException("Email is not correct!");
        }
    }


}
