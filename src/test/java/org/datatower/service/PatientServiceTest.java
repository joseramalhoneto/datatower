package org.datatower.service;

import org.datatower.exception.PatientInvalidException;
import org.datatower.exception.PatientNotFoundException;
import org.datatower.model.Patient;
import org.datatower.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class PatientServiceTest {

    private Patient patient;

    @InjectMocks
    private PatientService service;

    @Mock
    private PatientRepository repository;

    @BeforeEach
    void setUp() {
        service = new PatientService(repository);
        patient = new Patient(100L, "Mary", "McGow", 'F', LocalDate.now().minusYears(30));
    }

    @Test
    void findAllFemale() {
        List<Patient> list = new ArrayList<>();
        list.add(patient);

        when(repository.findAll()).thenReturn(list);
        List<Patient> result = service.findAllFemale();

        assertEquals(1, result.size());
        assertEquals('F', result.get(0).getGender());
        verify(repository, times(1)).findAll();
    }

    @Test
    void findById() {
        when(repository.findById(patient.getId())).thenReturn(Optional.of(patient));
        Patient patientFounded = service.findById(patient.getId());

        assertThat(patientFounded).isEqualTo(patient);
        assertEquals("Mary", patientFounded.getFirstName());
        assertEquals("McGow", patientFounded.getLastName());
        assertEquals('F', patientFounded.getGender());
        verify(repository, times(1)).findById(patientFounded.getId());
    }

    @Test
    void save() {
        when(repository.save(patient)).thenReturn(patient);
        Patient result = service.save(patient);

        assertThat(result).isEqualTo(patient);
        verify(repository, times(1)).save(patient);
    }

    @Test
    void deleteById() {
        when(repository.existsById(any())).thenReturn(true);
        when(repository.save(patient)).thenReturn(patient);

        Patient patientSaved = service.save(patient);
        service.deleteById(patientSaved.getId());

        Optional<Patient> result = repository.findById(patientSaved.getId());
        assertFalse(result.isPresent());
        verify(repository, times(1)).deleteById(patientSaved.getId());
    }

    @Test
    void isPatientFirstNameInvalid(){
        patient = new Patient(100L, "", "McGow", 'F', LocalDate.now().minusYears(30));

        Exception exception = assertThrows(PatientInvalidException.class, () -> service.save(patient));

        String expectedMessage = "Patient invalid.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void isPatientLastNameInvalid(){
        patient = new Patient(100L, "Mary", "", 'F', LocalDate.now().minusYears(30));

        Exception exception = assertThrows(PatientInvalidException.class, () -> service.save(patient));

        String expectedMessage = "Patient invalid.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void isPatientGenderInvalid(){
        patient = new Patient(100L, "Mary", "McGow", 'X', LocalDate.now().minusYears(30));

        Exception exception = assertThrows(PatientInvalidException.class, () -> service.save(patient));

        String expectedMessage = "Patient invalid.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void isYoungerThan18(){
        patient = new Patient(100L, "Mary", "McGow", 'F', LocalDate.now());

        Exception exception = assertThrows(PatientInvalidException.class, () -> service.save(patient));

        String expectedMessage = "Patient is not older than 18 years.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}