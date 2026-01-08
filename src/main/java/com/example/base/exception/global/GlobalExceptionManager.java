package com.example.base.exception.global;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.cglib.core.Local;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.base.exception.UserException;
import com.example.base.exception.response.ErrorExceptionResponse;




@RestControllerAdvice
public class GlobalExceptionManager {
    
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorExceptionResponse> UserExceptionHandler(UserException e) { 

        return ResponseEntity.status(e.getUserEnum().getHttpStatus())
        .body(new ErrorExceptionResponse(false, e.getMessage(), LocalDateTime.now(), "/user/**"));

    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorExceptionResponse> DuplicateKeyExceptionHandler(DuplicateKeyException e) { 

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorExceptionResponse(false, "DUPLICATED", LocalDateTime.now(), "/user/**"));

    }

}
