package org.datatower.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.datatower.model.Patient;
import org.datatower.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class PatientConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    CommandLineRunner runner(PatientService service) {
        return args -> {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Patient>> typeReference = new TypeReference<List<Patient>>(){};
            InputStream inputStream = TypeReference.class.getResourceAsStream("/static/datatower_structure.json");
            try {
                service.deleteAll();
                List<Patient> patients = mapper.readValue(inputStream,typeReference);
                service.saveAll(patients);
            } catch (IOException e){
                logger.error(e.getMessage());
            }
        };
    }

}
