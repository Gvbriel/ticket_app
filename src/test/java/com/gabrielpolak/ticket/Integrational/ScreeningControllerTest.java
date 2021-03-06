package com.gabrielpolak.ticket.Integrational;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gabrielpolak.ticket.model.DAO.Movie;
import com.gabrielpolak.ticket.model.DAO.Room;
import com.gabrielpolak.ticket.model.DAO.Screening;
import com.gabrielpolak.ticket.repository.ScreeningRepository;
import com.gabrielpolak.ticket.service.ScreeningService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.ZonedDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ScreeningControllerTest {

    static ObjectMapper mapper = new ObjectMapper();
    String url = "/api/v1/screenings";
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ScreeningService screeningService;
    @Autowired
    private ScreeningRepository screeningRepository;

    @BeforeAll
    static void setUp() {
        mapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void clearRepo() {
        screeningRepository.deleteAll();
    }

    @Test
    void getScreeningsShouldReturn200() throws Exception {
        //given
        Screening screening = createReservation();
        screeningRepository.save(screening);
        System.out.println(screening.getDate());

        //when
        mvc.perform(MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(mapper.writeValueAsString(screening.getId())))
                .andExpect(jsonPath("$[0].movie.title").value(screening.getMovie().getTitle()))
                .andExpect(jsonPath("$[0].date").value(screening.getDate().toLocalDateTime().toString() + screening.getDate().getOffset()))
                .andReturn();
    }


    @Test
    void getScreeningWithZonedDateTimeShouldReturn200() throws Exception {
        Screening screening = createReservation();
        screeningRepository.save(screening);

        mvc.perform(MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("date", ZonedDateTime.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(mapper.writeValueAsString(screening.getId())))
                .andExpect(jsonPath("$[0].movie.title").value(screening.getMovie().getTitle()))
                .andExpect(jsonPath("$[0].date").value(screening.getDate().toLocalDateTime().toString() + screening.getDate().getOffset()))
                .andReturn();
    }

    @Test
    void getScreeningsBetweenShouldReturn200() throws Exception {
        Screening screening = createReservation();
        screeningRepository.save(screening);

        mvc.perform(MockMvcRequestBuilders.get(url + "/between")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("from", ZonedDateTime.now().minusDays(2).toString())
                        .param("to", ZonedDateTime.now().plusDays(2).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(mapper.writeValueAsString(screening.getId())))
                .andExpect(jsonPath("$[0].movie.title").value(screening.getMovie().getTitle()))
                .andExpect(jsonPath("$[0].date").value(screening.getDate().toLocalDateTime().toString() + screening.getDate().getOffset()))
                .andReturn();
    }


    @Test
    void getScreeningsBetweenWithOneArgumentShouldReturn400() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(url + "/between")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("to", ZonedDateTime.now().plusDays(2).toString()))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }


    private Screening createReservation() {
        return Screening.createScreeningWithRoom(Movie.createMovieWithTitle("Dallas"), ZonedDateTime.now().plusMinutes(240), Room.createRoom());
    }
}
