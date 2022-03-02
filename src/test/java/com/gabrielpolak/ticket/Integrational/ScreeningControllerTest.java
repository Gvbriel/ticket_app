package com.gabrielpolak.ticket.Integrational;

import com.gabrielpolak.ticket.Integrational.Controller.ScreeningController;
import com.gabrielpolak.ticket.Integrational.Service.ScreeningService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ScreeningControllerTest {

    @Test
    void createScreening(){
        ScreeningService screeningService = Mockito.mock(ScreeningService.class);
        ScreeningController screeningController = new ScreeningController(screeningService);
    }
}