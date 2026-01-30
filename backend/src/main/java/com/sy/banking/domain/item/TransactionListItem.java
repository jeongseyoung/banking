package com.sy.banking.domain.item;

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
public class TransactionListItem {
    private long accountId;
    private long counterPartyAccountId;
    private TransferType type;
    private long amount;
    private long balanceAfter;
    private String memo;
    private LocalDateTime createdAt;
}
