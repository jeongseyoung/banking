package com.example.base.service;

import org.springframework.http.ResponseEntity;

import com.example.base.domain.dto.UserDto;
import com.example.base.domain.item.UserItem;

public interface UserService {
    UserItem getUser(long userId);

    ResponseEntity<String> addUser(UserDto user);
}
