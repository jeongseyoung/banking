package com.sy.banking.exception.enumbox;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountEnum {
    DUPLICATED_ACCOUNT_NUMBER(HttpStatus.CONFLICT, "DUPLICATED_ACCOUNT_NUMBER, 계좌중복"),
    INACTIVE_ACCOUNT(HttpStatus.FORBIDDEN, "INACTIVE_ACCOUNT, 비활성 계좌"),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCOUNT_NOT_FOUND, 없는 계좌 입니다."),
    ACCOUNT_CREATE_FAILED(HttpStatus.NOT_IMPLEMENTED, "ACCOUNT_CREATE_FAILED, 계좌 생설 실패");

    private final HttpStatusCode httpStatus; 
    private final String message;
}
