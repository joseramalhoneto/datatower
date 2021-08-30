package org.datatower.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PatientInvalidException extends RuntimeException{
    public PatientInvalidException(String message) {
        super(message);
    }
}
