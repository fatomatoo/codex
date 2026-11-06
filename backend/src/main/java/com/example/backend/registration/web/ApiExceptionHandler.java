package com.example.backend.registration.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.backend.registration.domain.EmailAlreadyRegisteredException;
import com.example.backend.registration.domain.InvalidRegistrationDataException;
import com.example.backend.registration.domain.RegistrationNotFoundException;

/** Translates domain exceptions into HTTP responses with user-facing (German) messages. */
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final String INVALID_REQUEST = "Ungültige Anfrage.";

    public record ApiError(String message) {
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    ResponseEntity<ApiError> conflict(EmailAlreadyRegisteredException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError("E-Mail ist bereits angemeldet."));
    }

    @ExceptionHandler(RegistrationNotFoundException.class)
    ResponseEntity<ApiError> notFound(RegistrationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError("Anmeldung nicht gefunden."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> invalid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse(INVALID_REQUEST);
        return ResponseEntity.badRequest().body(new ApiError(message));
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class, InvalidRegistrationDataException.class })
    ResponseEntity<ApiError> unreadable(Exception e) {
        return ResponseEntity.badRequest().body(new ApiError(INVALID_REQUEST));
    }
}
