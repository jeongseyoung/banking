package com.sy.banking.exception.enumbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountEnum {
    DUPLICATED_ACCOUNT_NUMBER("계좌중복"),
    INACTIVE_ACCOUNT("비활성 계좌");

    private final String message;
}
