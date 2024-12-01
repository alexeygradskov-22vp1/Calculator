package ru.gav.creditbank.calculator.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.gav.creditbank.calculator.validation.validators.CheckAgeValidator;


import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {CheckAgeValidator.class})
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAge{
    String message() default "Возраст должен быть больше 18";
    boolean required() default true;
    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}