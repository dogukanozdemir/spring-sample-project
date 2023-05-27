package com.example.healthsystem.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> defaultErrorHandler(HttpServletRequest req, HttpServletResponse res, Exception e){
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .time(LocalDateTime.now())
                        .error(e.getMessage())
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(value = {ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Object> methodArgumentNotValidHandler(HttpServletRequest req, HttpServletResponse res, MethodArgumentNotValidException e){

        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .time(LocalDateTime.now())
                        .error("Constraint Validation Failed")
                        .errors(errorMap)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<Object> responseStatusExceptionHandler(HttpServletRequest req, HttpServletResponse res, ResponseStatusException ex){
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .error(ex.getReason())
                        .time(LocalDateTime.now())
                        .build(),
                ex.getStatusCode()
        );
    }
}
