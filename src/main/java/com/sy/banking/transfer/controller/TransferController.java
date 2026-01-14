package com.sy.banking.transfer.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transfer")
public class TransferController {
    //나에게 입금 
    @PostMapping("/deposit/{accountNumber}")
    public String deposit(
        @PathVariable String accountNumber,
        @RequestBody String entity) {
        
        return entity;
    }
    
    //출금 
    @PostMapping("/withdraw/{accountNumber}")
    public String withdraw(
        @PathVariable String accountNumber,
        @RequestBody String entity) {
        
        return entity;
    }
    //다른 계좌에 이체

    //계좌조회
}
