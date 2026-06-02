package com.portfolio.helpdesk.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IncidentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleIncidentNotFound(
            IncidentNotFoundException exception,
            HttpServletRequest request
    ){
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                List.of(exception.getMessage()),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(IncidentAlreadyClosedException.class)
    public ResponseEntity<ErrorResponse> handleIncidentAlreadyClosed(
            IncidentAlreadyClosedException exception,
            HttpServletRequest request
    ){
        return buildErrorResponse(
                HttpStatus.CONFLICT,
                List.of(exception.getMessage()),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        List<String> messages = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                messages,
                request.getRequestURI()
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status,
            List<String> messages,
            String path
    ){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                messages,
                path
        );
        return ResponseEntity.status(status).body(errorResponse);
    }

}
