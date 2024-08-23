package ru.yandex.practicum.Filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import ru.yandex.practicum.Filmorate.exceptions.ValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleNotFoundException(ResponseStatusException ex) {
        return new ResponseEntity<>(ex.getReason(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>("Internal Server Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
