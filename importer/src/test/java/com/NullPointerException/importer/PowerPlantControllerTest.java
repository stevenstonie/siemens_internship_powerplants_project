package com.NullPointerException.importer;

import com.NullPointerException.importer.controller.PowerPlantController;
import com.NullPointerException.importer.service.PowerPlantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PowerPlantControllerTest {

    @Mock
    private PowerPlantService powerPlantService;

    @InjectMocks
    private PowerPlantController powerPlantController;
    @Value("${importer.csv.filepath}")
    private String csvFilePath;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testImportCSVSuccess() throws IOException {


        ResponseEntity<String> responseEntity = powerPlantController.importCSV();
        System.out.println("Testing ImportCSV Success");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Hello world!^_^\nCSV data imported successfully!^_^ ", responseEntity.getBody());
        verify(powerPlantService).importCSV();
    }

    @Test
    public void testImportCSVFailure() throws IOException {
        String errorMsg = "Error importing CSV data *-_-: IO Exception occurred";
        doThrow(new IOException("IO Exception occurred")).when(powerPlantService).importCSV();

        ResponseEntity<String> responseEntity = powerPlantController.importCSV();
        System.out.println("Testing ImportCSV Failure passed!");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(errorMsg, responseEntity.getBody());
        verify(powerPlantService).importCSV();
    }
}
