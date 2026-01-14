package com.sy.banking.domain.item.req;

import com.sy.banking.enumbox.TransferType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferReqItem {
    private String accountNumber; //내계좌
    private String counterpartyAccountNumber; //상대방계좌
    private long amount;
    private String memo;
    private TransferType transferType;
}
