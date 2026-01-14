package com.sy.banking.domain.item.res;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//내계좌 상태 리턴, ResponseENtity로?
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRes {
    private boolean isSuccess;
    private String accountNumber;
    private long amount;
    private long balanceAfter;
    private String memo;
    private LocalDateTime t;
}
