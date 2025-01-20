package org.yapp.domain.profile.application.util;

import java.time.LocalDate;
import java.time.Period;

public class DateUtils {

    public static int calculateAge(LocalDate birthdate) {
        LocalDate today = LocalDate.now();
        return Period.between(birthdate, today).getYears();
    }
}