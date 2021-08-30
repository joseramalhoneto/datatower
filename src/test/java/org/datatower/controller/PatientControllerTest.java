package org.datatower.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.datatower.model.Patient;
import org.datatower.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PatientController.class)
class PatientControllerTest {

    private Patient patient;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService service;

    @BeforeEach
    void setUp() {
        patient = new Patient(100L, "Mary", "McGow", 'F', LocalDate.now().minusYears(30));
    }

    @Test
    void findAllFemale() throws Exception {
        when(service
                .findAllFemale())
                .thenReturn(List.of(patient));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/patient"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(100L))
                .andExpect(jsonPath("$[0].firstName").value("Mary"));
    }

    @Test
    void findById() throws Exception {
        when(service
                .findById(100L))
                .thenReturn(patient);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/patient/{id}", 100L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.firstName").value("Mary"));
    }

    @Test
    void save() throws Exception {
        when(service.save(any()))
                .thenReturn(patient);

        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(new ObjectMapper().writeValueAsString(patient))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.firstName").value("Mary"))
                .andExpect(jsonPath("$.lastName").value("McGow"))
                .andExpect(jsonPath("$.gender").value("F"));
    }

    @Test
    void deleteById() throws Exception {
        when(service.save(any()))
                .thenReturn(patient);

        this.mockMvc.perform(delete("/patient/{id}", 100L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}