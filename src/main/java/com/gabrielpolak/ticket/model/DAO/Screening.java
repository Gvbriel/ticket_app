package com.gabrielpolak.ticket.model.DAO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gabrielpolak.ticket.exceptions.NotEnoughTicketsException;
import lombok.Data;

import javax.persistence.*;

import java.time.ZonedDateTime;

@Data
@Entity
public class Screening {
    static final int AVAILABLE_TICKETS = 16;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ZonedDateTime date;

    private int tickets;

    @ManyToOne(cascade = CascadeType.ALL)
    private Movie movie;

    @ManyToOne(cascade = CascadeType.ALL)
    private Room room;

    @JsonIgnore
    private String movietitle;

    public static Screening createScreeningWithRoom(Movie movie, ZonedDateTime date, Room room) {
        return new Screening(movie, date, room);
    }

    public Screening(Movie movie, ZonedDateTime date, Room room) {
        this.movie = movie;
        this.date = date;
        this.room = room;
        this.movietitle = movie.getTitle();
        this.tickets = AVAILABLE_TICKETS;
    }

    public Screening(Movie movie, ZonedDateTime date) {
        this.movie = movie;
        this.date = date;
        this.tickets = AVAILABLE_TICKETS;
    }

    public Screening() {
        this.tickets = AVAILABLE_TICKETS;
    }

    public void removeTickets(int amount) {
        int pom = this.tickets;
        pom -= amount;
        if (pom < 0) {
            throw new NotEnoughTicketsException("Not enough tickets!");
        }
        this.tickets -= amount;
    }

}
