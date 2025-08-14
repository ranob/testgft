package com.inditex.priceservice.infrastructure.adapter.input.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.Map;

@ControllerAdvice
@Slf4j 
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String name = ex.getName();
        String type = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "N/A";
        Object value = ex.getValue();
        String message = String.format("El parametro '%s' debe ser de tipo '%s' pero el valor fue: '%s'", name, type, value);
        
        log.error("Bad request received. Parameter type mismatch: {}", message, ex);

        Map<String, String> errorResponse = Map.of("error", message);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
