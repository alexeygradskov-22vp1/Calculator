package ru.gav.creditbank.calculator.exceptions.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.gav.creditbank.calculator.exceptions.InvalidScoringException;

@ControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler {
    private final String NOT_VALID_DATA = "Проверьте правильность введенных данных";

    @ExceptionHandler(InvalidScoringException.class)
    public ResponseEntity<String> handleInvalidScoringException(InvalidScoringException ex) {
        logger.info(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info(ex.getMessage());
        return new ResponseEntity<>(NOT_VALID_DATA, HttpStatus.BAD_REQUEST);
    }
}