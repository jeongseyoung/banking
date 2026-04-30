package com.sy.banking.exception.enumbox;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserEnum {
    ADD_FAILED(HttpStatus.BAD_REQUEST, "ADD_FAILED, 등록 실패"),
    DUPLICATED(HttpStatus.CONFLICT, "DUPLICATED, 중복"),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "LOGIN_FAILED, 로그인실패"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND, 없는 유저");

    private final HttpStatusCode httpStatus;
    private final String message;
}
