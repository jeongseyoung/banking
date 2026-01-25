package com.sy.banking.auth.controller;

import org.springframework.web.bind.annotation.RestController;

import com.sy.banking.account.service.AccountService;
import com.sy.banking.domain.item.UserItem;
import com.sy.banking.domain.item.res.UserItemResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class OAuth2Controller {
    private final AccountService accountService;
    //localhost:8098/oauth2/authorize/google
    //localhost:8098/oauth2/authorize/google
    @GetMapping("/me")
    public ResponseEntity<UserItemResponse> redirect(@AuthenticationPrincipal UserItem userItem) {
        accountService.getMyAccountInfo(userItem.getUserId());
        return ResponseEntity.ok(new UserItemResponse(userItem.getEmail(), userItem.getName(), userItem.getUser_Role(), userItem.getCreated_at()));
    }

    @GetMapping("/login_failed")
    public String redirect_login_failed() {
        return new String("음?");
    }
    
    

}
   