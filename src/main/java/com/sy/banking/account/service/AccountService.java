package com.sy.banking.account.service;

import com.sy.banking.domain.dto.UserDto;
import com.sy.banking.domain.item.AccountItem;

public interface AccountService {
    AccountItem createAccount(UserDto userDto);
}
