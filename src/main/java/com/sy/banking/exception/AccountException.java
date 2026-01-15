package com.sy.banking.exception;

import com.sy.banking.exception.enumbox.AccountEnum;

import lombok.Getter;

@Getter
public class AccountException extends RuntimeException{
    private final AccountEnum accountEnum;

    public AccountException(AccountEnum e) {
        super(e.getMessage());
        this.accountEnum = e;
    }
}
