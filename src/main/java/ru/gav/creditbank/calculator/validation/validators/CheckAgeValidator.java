package ru.gav.creditbank.calculator.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;
import ru.gav.creditbank.calculator.validation.CheckAge;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class CheckAgeValidator implements ConstraintValidator<CheckAge, LocalDate> {

    @Override
    public boolean isValid(@NotNull LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(localDate)) return false;
        var now = LocalDate.now();
        long years = ChronoUnit.YEARS.between(localDate, now);
        return years>=18;

    }
}

