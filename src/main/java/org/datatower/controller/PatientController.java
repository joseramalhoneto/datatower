package org.datatower.controller;

import org.datatower.model.Patient;
import org.datatower.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PatientService service;

    @Autowired
    public PatientController(PatientService service) {
        this.service = service;
    }

    @GetMapping("/female")
    @ResponseStatus(HttpStatus.OK)
    public List<Patient> findAllFemale() {
        logger.info("@GetMapping/findAllFemale");
        List<Patient> allFemale = service.findAllFemale();
        return allFemale;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Patient findById(@PathVariable Long id) {
        logger.info("@GetMapping/findById");
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient save(@RequestBody Patient area) {
        logger.info("@GetMapping/save");
        return service.save(area);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable Long id){
        logger.info("@GetMapping/deleteById");
        service.deleteById(id);
    }

}
