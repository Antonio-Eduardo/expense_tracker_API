package com.eduardo.expense_tracker.infra.exception;

import com.eduardo.expense_tracker.services.exceptions.BusinessException;
import com.eduardo.expense_tracker.services.exceptions.DuplicateResourceException;
import com.eduardo.expense_tracker.services.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandartError> resourceNotFound(ResourceNotFoundException e){
        StandartError error = new StandartError(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Resource not found",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandartError> genericException(Exception e){
        StandartError error = new StandartError(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<StandartError> duplicateResource(DuplicateResourceException e){
        StandartError error = new StandartError(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                "Duplicate resource",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<StandartError> businessException(BusinessException e){
        StandartError error = new StandartError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Business exception",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
