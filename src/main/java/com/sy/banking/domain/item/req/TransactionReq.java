package com.sy.banking.domain.item.req;

import java.time.LocalDateTime;

import com.sy.banking.enumbox.TransferType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionReq {
    private long accountId;
    private long counterpartyAccountId;
    private TransferType transferType;
    private long amount;
    private long balanceAfter;
    private String memo;
    private LocalDateTime t;

    //출금
    public TransactionReq(long accountId, TransferType transferType, long amount, long balanceAfter, String memo) {
        this.accountId = accountId;
        this.transferType = transferType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.memo = memo;

    }
}
