package com.sy.banking.exception.enumbox;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserEnum {
    ADD_FAILED(HttpStatus.BAD_REQUEST, "등록 실패"),
    DUPLICATED(HttpStatus.CONFLICT, "중복");

    private final HttpStatusCode httpStatus;
    private final String message;
}
