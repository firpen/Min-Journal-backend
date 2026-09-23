package com.minjournal.min_journal_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice gör alla @ExceptionHandler-metoder tillgängliga i mina controllers
@RestControllerAdvice 
public class GlobalExceptionHandler {
    // Fångar upp UsernameAlreadyExistsExceptions och kör koden nedan när ett sådant exception kastas
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<String> handleUsernameExists(UsernameAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    // Fångar upp InvalidCredentialsException och kör koden nedan när ett sådant exception kastas
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    // Fångar upp MethodArgumentNotValidExceptions som kastas av @Valid på mina controllermetoder.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The input didnt match the requirements");
    }
}
