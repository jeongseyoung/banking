package com.sy.banking.exception.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransferExceptionRes {
    private String message;
    private LocalDateTime t;
    private String path;
    private String requestId;
}
