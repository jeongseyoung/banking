package com.sy.banking.interest;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sy.banking.account.mapper.AccountMapper;
import com.sy.banking.domain.item.AccountItem;
import com.sy.banking.domain.item.req.TransactionReq;
import com.sy.banking.enumbox.TransferType;
import com.sy.banking.transfer.service.TransferService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Interest {
    
    private final TransferService transferService;
    private final AccountMapper accountMapper;
    
    //매일 새벽 2시에 모든 계좌에 이자 지급
    @Scheduled(cron = "0 0 2 * * ?")  
    public void interest() {

        List<AccountItem> accountItem = accountMapper.fidAllActiveAccount(); 
        for(AccountItem a : accountItem) {
            long accountId = a.getAccountId();
            String accountNumber = a.getAccountNumber();
            long balance = a.getBalance();
            if(balance > 0 ) {

                long interest = calculateInterest(balance);

                TransactionReq transactionReq = TransactionReq.builder()
                                                                 .accountId(accountId)
                                                                 .counterpartyAccountId(accountId)
                                                                 .amount(interest)
                                                                 .memo("이자 " + interest +"원 지급.")
                                                                 .transferType(TransferType.INTEREST)
                                                                 .balanceAfter(balance + interest)
                                                                 .t(LocalDateTime.now())
                                                                 .build();
                transferService.interest(accountNumber, transactionReq);
            }
        }

    }
    
    private long calculateInterest(long balance) {
        double rate = 0.03 / 365;  // 연 3% → 일 복리
        return (long)(balance * rate);
    }
}
