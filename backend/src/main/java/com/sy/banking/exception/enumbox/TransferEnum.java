package com.sy.banking.exception.enumbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransferEnum {
    INSUFFICIENT_BALANCE("Insufficient Balance."),
    NO_ACCOUNT("Does not have an account."),
    SAME_ACCOUNT_TRANSFER("동일한 계좌로 이체할 수 없습니다");

    private final String message;
}
