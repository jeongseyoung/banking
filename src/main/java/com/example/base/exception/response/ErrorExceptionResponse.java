package com.example.base.exception.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorExceptionResponse {
    private boolean isSucces;
    private String message;
    private LocalDateTime t;
    private String path;
}
