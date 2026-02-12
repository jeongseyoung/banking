package com.sy.banking.auth.service;

import org.springframework.http.ResponseEntity;

import com.sy.banking.domain.dto.UserDto;
import com.sy.banking.domain.item.UserItem;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public interface UserService {
    UserItem getUser(long userId);

    ResponseEntity<String> addUser(UserDto user);

    void logout(HttpServletResponse resopnse, HttpServletRequest request);
}
