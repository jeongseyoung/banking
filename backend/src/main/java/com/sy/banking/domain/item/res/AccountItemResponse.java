package com.sy.banking.domain.item.res;

import java.util.List;

import com.sy.banking.domain.item.AccountItem;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccountItemResponse {
    // private long accountId;
    // private long userId;
    // private String accountNumber;
    // private long balance;
    // private String status; //ACTIVE, BLOCKED
    // private LocalDateTime createdAt;
    private int totalCount;
    private long totalBalance;
    private List<AccountItem> list;

    public static AccountItemResponse of(List<AccountItem> accounts){

        //계좌 개수
        int totalCount = accounts.size();
        //계좌 잔액 합치기
        long totalBalance = accounts.stream().mapToLong(AccountItem::getBalance).sum();

        return new AccountItemResponse(totalCount, totalBalance, accounts);
    }
}
