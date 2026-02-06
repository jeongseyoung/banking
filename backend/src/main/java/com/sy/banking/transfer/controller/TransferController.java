package com.sy.banking.transfer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sy.banking.domain.item.req.TransferReqItem;
import com.sy.banking.domain.item.req.WithdrawalReqItem;
import com.sy.banking.domain.item.res.TransactionRes;
import com.sy.banking.transfer.service.TransferService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService transferService;

    //나에게 입금 
    @PostMapping("/deposit")
    public ResponseEntity<TransactionRes> deposit(@RequestBody TransferReqItem transferReqItem) {
        log.info("accountNum, amount {}, {}", transferReqItem.getAccountNumber(), transferReqItem.getAmount());
        return ResponseEntity.ok(transferService.deposit(transferReqItem));
    }
    
    //출금 
    @PostMapping("/withdrawal")
    public ResponseEntity<TransactionRes> withdraw(@RequestBody WithdrawalReqItem withdrawalReqItem) {
            return ResponseEntity.ok(transferService.withdrawal(withdrawalReqItem));
    }

    //다른 계좌에 이체
    @PostMapping("/t")
    public ResponseEntity<TransactionRes> transfer(@RequestBody TransferReqItem transferReqItem) {
        return ResponseEntity.ok(transferService.transfer(transferReqItem));
    }
}
