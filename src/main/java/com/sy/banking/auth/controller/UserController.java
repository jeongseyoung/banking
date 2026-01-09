package com.sy.banking.auth.controller;


import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sy.banking.auth.service.UserService;
import com.sy.banking.domain.dto.UserDto;
import com.sy.banking.domain.item.UserItem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/getUser")
    public ResponseEntity<UserItem> getMethodName(@RequestParam("userId") long id) {
        return ResponseEntity.ok().body(userService.getUser(id));
    }
    
    @PostMapping("/user/insertUser")
    public ResponseEntity<String> addUser(@RequestBody UserDto user) {
        
        return userService.addUser(user);
        
    }
    
    @GetMapping("/oauth2/callback")
    public String hi(@RequestParam String param){
        return "hi";
    }
    
}
