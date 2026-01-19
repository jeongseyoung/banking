package com.sy.banking.domain.item.res;

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
public class AccountStatementRes {
    /*
    private String accountId;
    private String counterPartyAccountId;
    private TransferType transferType;
    private long amount;
    private long balanceAfter;
    private String memo;
    private LocalDateTime t;
    */
    private String accountId;
    private String counterPartyAccountId;
    private TransferType transferType;
    private long amount;
    private long balanceAfter;
    private String memo;
    private LocalDateTime t;
}
