package com.sy.banking.domain.item;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountItem {

    private long userId;
    private String accountNumber;
    private long balance;
    private String status; //ACTIVE, BLOCKED
    private LocalDateTime createdAt;
    

    // public AccountItem(long userId, String accountNumber, long balance, String status, LocalDateTime createAt) {
    //     this.userId = userId;
    //     this.accountNumber = accountNumber;
    //     this.balance = balance;
    //     this.status = status;
    //     this.createdAt = createAt;
    // }
}
