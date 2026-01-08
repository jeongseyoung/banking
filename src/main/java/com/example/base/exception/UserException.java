package com.example.base.exception;

import com.example.base.exception.enumbox.UserEnum;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException{
    
    private final UserEnum userEnum;

    public UserException(UserEnum e) {
        super(e.getMessage());
        this.userEnum = e;
    }
}
