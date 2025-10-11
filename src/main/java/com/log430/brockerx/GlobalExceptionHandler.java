package com.log430.brockerx;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.log430.brockerx.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.postgresql.util.PSQLException;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
        System.out.println("🌟 GlobalException loaded!");
    }

    // Erreurs de validation ou business
    @ExceptionHandler(IllegalArgumentException.class) public ResponseEntity<ApiError> handleIllegalArgs(
            IllegalArgumentException e, HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), "Bad Request", e.getMessage(),
                                      request.getRequestURI());
        return ResponseEntity.badRequest().body(error);
    }

    // Exception générale
    @ExceptionHandler(Exception.class) public ResponseEntity<ApiError> handleGeneric(Exception e,
                                                                                     HttpServletRequest request) {
        e.printStackTrace(); // log côté serveur
        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
                                      "Une erreur est survenue, contactez le support", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDuplicateEmail(DataIntegrityViolationException ex,
                                                         HttpServletRequest request) {
        String message = "Duplicate email";

        ApiError error = new ApiError(HttpStatus.CONFLICT.value(), "Conflict", message, request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonParseError(HttpMessageNotReadableException ex) {
        String message = "Invalid JSON: " + ex.getMostSpecificCause().getMessage();
        return ResponseEntity.badRequest().body(Map.of("error", message));
    }


}
