package com.inghub.store.config;

import com.inghub.store.exception.DuplicateNameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class StoreServiceGlobalExceptionHandler {

    @ExceptionHandler({DuplicateNameException.class})
    public ResponseEntity<String> handleDuplicateNameException(DuplicateNameException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
