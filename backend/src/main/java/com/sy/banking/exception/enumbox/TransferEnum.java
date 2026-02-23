package com.sy.banking.exception.enumbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransferEnum {
    INSUFFICIENT_BALANCE("Insufficient Balance."),
    NO_ACCOUNT("Does not have an account."),
    SAME_ACCOUNT_TRANSFER("동일한 계좌로 이체할 수 없습니다"),
    INVALID_AMOUNT("잘못된 금액"),
    AMOUNT_EXCEEDED("금액 초과(최대 1000만원"),
    UPDATE_FAILED("업데이트 실패"),
    SAVE_FAILED("저장 실패");

    private final String message;
}
