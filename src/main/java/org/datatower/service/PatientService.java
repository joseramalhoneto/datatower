package org.datatower.service;

import lombok.Synchronized;
import org.datatower.exception.PatientInvalidException;
import org.datatower.exception.PatientNotFoundException;
import org.datatower.model.Patient;
import org.datatower.repository.PatientRepository;
import org.datatower.utility.PatientUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository repository;

    @Autowired
    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    public List<Patient> findAllFemale() {
        List<Patient> allPatients = repository.findAll();

        return allPatients
                .stream()
                .filter(patient -> patient.getGender() == PatientUtility.FEMALE)
                .sorted(Comparator.comparing(Patient::getLastName))
                //.sorted(Comparator.comparing(Patient::getLastName, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public Patient findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found."));
    }

    @Synchronized
    public Patient save(Patient patient) {
        if(!PatientUtility.isPatientValid(patient))
            throw new PatientInvalidException("Patient invalid.");

        if(!PatientUtility.isOlderThan18(patient))
            throw new PatientInvalidException("Patient is not older than 18 years.");

        return repository.save(patient);
    }

    @Synchronized
    public void deleteById(Long id) {
        if(!repository.existsById(id))
            throw new PatientNotFoundException("Patient not found.");

        repository.deleteById(id);
    }

    public void saveAll(List<Patient> patients) {
        repository.saveAll(patients);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

}
