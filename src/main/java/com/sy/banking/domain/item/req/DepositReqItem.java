package com.sy.banking.domain.item.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//입금
@Getter
@Setter
@AllArgsConstructor
public class DepositReqItem {
    private long accountId; 
    private String accountNumber;
    private long amount;
}
