package com.sy.banking.account.service;

import com.sy.banking.domain.item.AccountItem;
import com.sy.banking.domain.item.UserItem;

public interface AccountService {
    //새 계좌
    AccountItem createAccount(UserItem userItem);

}
