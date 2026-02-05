package com.sy.banking.account.controller;

import org.springframework.web.bind.annotation.RestController;

import com.sy.banking.account.service.AccountService;
import com.sy.banking.domain.item.ASPageItem;
import com.sy.banking.domain.item.AccountItem;
import com.sy.banking.domain.item.TransactionListItem;
import com.sy.banking.domain.item.UserItem;
import com.sy.banking.domain.item.res.AccountItemResponse;
import com.sy.banking.domain.paging.PageResponse;
import com.sy.banking.enumbox.TransferType;
import com.sy.banking.utils.ExcelConverter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @GetMapping("/myaccount")
    public ResponseEntity<PageResponse<TransactionListItem>> getMyAccountStatement(
        @RequestParam(name = "page") int p,
        @RequestParam(name = "size") int s,
        @RequestParam(name = "transferType", defaultValue = "") TransferType t,
        @AuthenticationPrincipal UserItem userItem) {

        ASPageItem asPageItem = new ASPageItem(p, s, t);
        log.info("{} {} {}", p, s, t);
        //PageResponse<TransactionListItem> temp = accountService.getMyAccountStatement(asPageItem, userItem);
        return ResponseEntity.ok(accountService.getMyAccountStatement(asPageItem, userItem));
        
    }

    //accountTab
    @GetMapping("/accounttab")
    public AccountItemResponse accountTab(@AuthenticationPrincipal UserItem userItem) {
        return accountService.getMyAccountInfo(userItem.getUserId());
    }
    
    //엑셀변환
    @GetMapping("/excel")
    public void downloadTransactionsExcel(HttpServletResponse response, 
        @AuthenticationPrincipal UserItem userItem) throws IOException{
        
        List<TransactionListItem> list = accountService.findAccountByUserId(userItem.getUserId());
        log.info("excel list {}", list);
        ExcelConverter.exportTransactions(response, list);
    }
}
