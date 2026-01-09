package com.sy.banking.auth.service;

import org.springframework.http.ResponseEntity;

import com.sy.banking.domain.dto.UserDto;
import com.sy.banking.domain.item.UserItem;


public interface UserService {
    UserItem getUser(long userId);

    ResponseEntity<String> addUser(UserDto user);
}
