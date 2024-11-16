package ru.gav.creditbank.calculator.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import ru.gav.creditbank.calculator.validation.CheckAge;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Slf4j
public class CheckAgeValidator implements ConstraintValidator<CheckAge, LocalDate> {

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate now = LocalDate.now();
        long years = ChronoUnit.YEARS.between(localDate, now);
        return years>=18;

    }
}

