package org.datatower.utility;

import org.datatower.model.Patient;

import java.time.LocalDate;
import java.time.Period;

public class PatientUtility {

    public static final char FEMALE = 'F';
    public static final char MALE = 'M';
    public static final char UNDEFINED = 'U';

    public static boolean isPatientValid(Patient patient) {
        if(patient == null ||
                patient.getFirstName().equals("") ||
                patient.getLastName().equals("") ||
                !isGenderValid(patient) ||
                patient.getBirthday().equals(""))
            return false;

        return true;
    }

    public static boolean isGenderValid(Patient patient) {
        char gender = Character.toUpperCase(patient.getGender());

        if(Character.isWhitespace(gender))
           return false;

        if(gender == FEMALE ||
            gender == MALE  ||
            gender == UNDEFINED)
            return true;

        return false;
    }

    public static boolean isOlderThan18(Patient patient) {
        LocalDate birthday = patient.getBirthday();
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthday, now);

        if(period.getYears() < 18)
            return false;

        return true;
    }

}
