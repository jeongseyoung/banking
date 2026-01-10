package com.sy.banking.account.controller;

import org.springframework.web.bind.annotation.RestController;

import com.sy.banking.account.service.AccountService;
import com.sy.banking.config.jwt.CustomUserDetails;
import com.sy.banking.config.jwt.CustomUserDetailsService;
import com.sy.banking.domain.dto.UserDto;
import com.sy.banking.domain.item.AccountItem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final CustomUserDetailsService customUserDetailsService;

    //계좌개설
    @GetMapping("/account/newaccount")
    public ResponseEntity<AccountItem> createAccount(@AuthenticationPrincipal CustomUserDetails customUserDetails, @AuthenticationPrincipal UserDto userDto) {
        System.out.println("zz: " + customUserDetails.getUserDto());
        System.out.println("zzzzz : " + userDto);
        //UserDetails userDetails = customUserDetailsService.loadUserById(userDto.getUserId());
        //log.info("userDto userDetails {}", userDto.getEmail(), userDetails);
        //return ResponseEntity.ok(accountService.createAccount(userDto));
        return null;
    }
    
//
}
