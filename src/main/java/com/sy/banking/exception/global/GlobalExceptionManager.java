package com.sy.banking.exception.global;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sy.banking.exception.TransferException;
import com.sy.banking.exception.UserException;
import com.sy.banking.exception.response.ErrorExceptionResponse;
import com.sy.banking.exception.response.TransferExceptionRes;

import jakarta.servlet.http.HttpServletRequest;





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

    //transferexception
    @ExceptionHandler(TransferException.class)
    public ResponseEntity<TransferExceptionRes> TransferExceptionHandler(TransferException e, HttpServletRequest req) { 

        String requestId = UUID.randomUUID().toString();
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new TransferExceptionRes(e.getTransferEnum().getMessage(), LocalDateTime.now(), "/transfer/**", requestId));

    }

}
