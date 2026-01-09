package com.sy.banking.account.service;

import org.springframework.http.ResponseEntity;

import com.sy.banking.domain.dto.UserDto;
import com.sy.banking.domain.item.AccountItem;

public interface AccountService {
    ResponseEntity<AccountItem> createAccount(UserDto userDto);
}
