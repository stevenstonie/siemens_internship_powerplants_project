package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.example.demo.model.PowerPlant;
import com.example.demo.repository.PowerPlantRepository;
import com.example.demo.service.PowerPlantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.times;



import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class PowerPlantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PowerPlantService powerPlantService;

    @InjectMocks
    PowerPlantController powerPlantController;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @MockBean
    private PowerPlantRepository powerPlantRepository;
    // verifică dacă metoda controllerului a fost apelată cu succes și dacă răspunsul HTTP este  200 OK
    @Test
    public void testStartEnergyProduction() throws Exception {
        PowerPlant testPlant = new PowerPlant("ROU", "Romania", "TestPlant",
                                            "1234", 500, 45.95, 24.97, "Coal");
        when(powerPlantRepository.findById(anyLong())).thenReturn(Optional.of(testPlant));

        mockMvc.perform(post("/api/powerplants/start-production/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(powerPlantService).startEnergyProduction(any(PowerPlant.class));
    }


@Test
public void testStopEnergyProduction() {
    // Act
    ResponseEntity<?> response = powerPlantController.stopEnergyProduction();

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(powerPlantService, times(1)).stopEnergyProduction();
}

}
