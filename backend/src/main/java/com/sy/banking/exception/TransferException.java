package com.sy.banking.exception;

import com.sy.banking.exception.enumbox.TransferEnum;

import lombok.Getter;

@Getter
public class TransferException extends RuntimeException{
    
    private final TransferEnum transferEnum;
    
    public TransferException(TransferEnum e) {
        super(e.getMessage());
        this.transferEnum = e;
    }
}
