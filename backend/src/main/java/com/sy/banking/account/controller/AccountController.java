package com.sy.banking.account.controller;

import org.springframework.web.bind.annotation.RestController;

import com.sy.banking.account.service.AccountService;
import com.sy.banking.domain.item.ASPageItem;
import com.sy.banking.domain.item.AccountItem;
import com.sy.banking.domain.item.TransactionListItem;
import com.sy.banking.domain.item.UserItem;
import com.sy.banking.domain.item.res.AccountItemResponse;
import com.sy.banking.domain.paging.PageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@Slf4j
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    //private final CustomUserDetailsService customUserDetailsService;

    //계좌개설
    @GetMapping("/newaccount")
    public ResponseEntity<AccountItem> createAccount(@AuthenticationPrincipal UserItem userItem) {

        return ResponseEntity.ok(accountService.createAccount(userItem));
        
    }

    //계좌조회 - 내 계좌 상태 + 입출금list - page형태
    @PostMapping("/myaccount")
    public ResponseEntity<PageResponse<TransactionListItem>> getMyAccountStatement(
        @RequestBody ASPageItem asPageItem,
        @AuthenticationPrincipal UserItem userItem) {
        
        return ResponseEntity.ok(accountService.getMyAccountStatement(asPageItem, userItem));
        
    }

    //accountTab
    @GetMapping("/accounttab")
    public AccountItemResponse accountTab(@AuthenticationPrincipal UserItem userItem) {
        //System.out.println("userItem, accountTab" + " " + userItem.getUserId());
        return accountService.getMyAccountInfo(userItem.getUserId());
        //AccountItemResponse temp = accountService.getMyAccountInfo(userItem.getUserId());
        //log.info("zz {}", temp.getList().get(0).getAccountNumber());
        //return null;
    }
    
}
