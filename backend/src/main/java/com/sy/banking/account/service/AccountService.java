package com.sy.banking.account.service;


import com.sy.banking.domain.item.ASPageItem;
import com.sy.banking.domain.item.AccountItem;
import com.sy.banking.domain.item.TransactionListItem;
import com.sy.banking.domain.item.UserItem;
import com.sy.banking.domain.item.res.AccountItemResponse;
import com.sy.banking.domain.paging.PageResponse;

public interface AccountService {
    //새 계좌
    AccountItem createAccount(UserItem userItem);

    PageResponse<TransactionListItem> getMyAccountStatement(ASPageItem asPageItem, UserItem userItem);

    AccountItemResponse getMyAccountInfo(long userId);

}
