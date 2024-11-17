package ru.gav.creditbank.calculator.exceptions;

public class InvalidScoringException extends RuntimeException {
    public InvalidScoringException(String message) {
        super(message);
    }
}
