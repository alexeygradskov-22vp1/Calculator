package ru.gav.creditbank.calculator.exceptions.supplier;

import org.springframework.stereotype.Component;
import ru.gav.creditbank.calculator.exceptions.InvalidScoringException;

import java.util.function.Supplier;

@Component
public class ExceptionSupplier {

    public Supplier<InvalidScoringException> scoringExceptionSupplier(String reason){
        return ()-> new InvalidScoringException(String.format("Refusal of a loan for a reason: %s",reason));
    }
}
