package com.sy.banking.transfer.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sy.banking.account.mapper.AccountMapper;
import com.sy.banking.domain.item.AccountItem;
import com.sy.banking.domain.item.req.TransactionReq;
import com.sy.banking.domain.item.req.TransferReqItem;
import com.sy.banking.domain.item.req.WithdrawalReqItem;
import com.sy.banking.domain.item.res.TransactionRes;
import com.sy.banking.enumbox.TransferType;
import com.sy.banking.exception.TransferException;
import com.sy.banking.exception.enumbox.TransferEnum;
import com.sy.banking.transfer.mapper.TransferMapper;
import com.sy.banking.transfer.service.TransferService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TransferServiceImpl implements TransferService{

    private final AccountMapper accountMapper;
    private final TransferMapper transferMapper;

    private void executeTransfer(String accountNumber, TransactionReq...transactionReqs) {
        for(TransactionReq t : transactionReqs) {
            update(new AccountItem(t.getAccountId(), t.getBalanceAfter()));
            save(t);
        }
        res(transactionReqs[0], accountNumber);
    }
    private void update(AccountItem accountItem){
        accountMapper.updateAccountInfo(accountItem);
    }
    private void save(TransactionReq t){
        transferMapper.saveTransaction(t);
    }

    //response
    private TransactionRes res(TransactionReq transactionReq, String accountNumber) {
        return new TransactionRes(true, accountNumber, transactionReq.getAmount(), transactionReq.getBalanceAfter(), transactionReq.getMemo(), transactionReq.getT());    
    }

    @Override
    public void deposit(TransferReqItem transactionReqItem) {

        String accountNumber = transactionReqItem.getAccountNumber();

        Optional<AccountItem> accountItem = Optional.ofNullable(accountMapper.existingAccount(accountNumber))
                                                    .orElseThrow(() -> new TransferException(TransferEnum.NO_ACCOUNT));

        long accountId = accountItem.get().getAccountId();
        long balanceAfter = accountItem.get().getBalance() + transactionReqItem.getAmount();

        TransactionReq transactionReq;
        transactionReq = TransactionReq.builder()
                            .accountId(accountId)
                            .counterpartyAccountId(accountId)
                            .transferType(TransferType.DEPOSIT)
                            .amount(transactionReqItem.getAmount())
                            .balanceAfter(balanceAfter)
                            .memo(transactionReqItem.getAmount() + "원 입금완료")
                            .t(LocalDateTime.now())
                            .build();

        executeTransfer(accountNumber, transactionReq);
        res(transactionReq, accountNumber);
    }

    @Override
    public void withdrawal(WithdrawalReqItem withdrawalReqItem) {
        //account select
        Optional<AccountItem> accountItem = Optional.ofNullable(accountMapper.existingAccount(withdrawalReqItem.getAccountNumber()))
                                                    .orElseThrow(() -> new TransferException(TransferEnum.NO_ACCOUNT));
        
        long accountId = accountItem.get().getAccountId();
        long balance = accountItem.get().getBalance();
        long amount = withdrawalReqItem.getAmount();
        long balanceAfter = accountItem.get().getBalance() - amount;

        if((balance <= 0) || (balance < amount)) {
            throw new TransferException(TransferEnum.INSUFFICIENT_BALANCE);
        }

        //tran req
        TransactionReq transactionReq = new TransactionReq(accountId, TransferType.WITHDRAWAL, amount, balanceAfter, "출금");

        accountMapper.updateAccountInfo(new AccountItem(transactionReq.getAccountId(), transactionReq.getBalanceAfter()));
        transferMapper.saveTransaction(transactionReq);
        
        res(transactionReq, withdrawalReqItem.getAccountNumber());
    }

    @Override
    public void transfer(TransferReqItem transactionReqItem) {

       //내계좌 상대계좌 같을 시 예외처리
       if(transactionReqItem.getAccountNumber().equals(transactionReqItem.getCounterpartyAccountNumber()))
            throw new TransferException(TransferEnum.SAME_ACCOUNT_TRANSFER);

       Optional<AccountItem> p1_accountItem = accountMapper.existingAccount(transactionReqItem.getAccountNumber());       
       Optional<AccountItem> p2_accountItem = Optional.ofNullable(accountMapper
        .existingAccount(transactionReqItem.getCounterpartyAccountNumber())).orElseThrow(() -> new TransferException(TransferEnum.NO_ACCOUNT));

       long p1_accountId = p1_accountItem.get().getAccountId();
       long p2_accountId = p2_accountItem.get().getAccountId();

       TransactionReq p1_transactionReq = new TransactionReq();
       TransactionReq p2_transactionReq = new TransactionReq();

       long p1_accountItem_balance = p1_accountItem.get().getBalance();
       //잔금이 없거나 -일경우, 잔금보다 요청amount가 많을 경우 exception
       if((p1_accountItem_balance <= 0) || (p1_accountItem_balance < transactionReqItem.getAmount())) {
            throw new TransferException(TransferEnum.INSUFFICIENT_BALANCE);
       }

       //A -> B 이체
       if(p1_accountItem.isPresent() && p2_accountItem.isPresent() && p1_accountId != p2_accountId) {

            p1_transactionReq = TransactionReq.builder()
                            .accountId(p1_accountId)
                            .counterpartyAccountId(p2_accountId)
                            .transferType(TransferType.TRANSFER_OUT)
                            .amount(transactionReqItem.getAmount())
                            .balanceAfter(p1_accountItem.get().getBalance() - transactionReqItem.getAmount())
                            .memo(p2_accountItem.get().getAccountNumber() + "에게 " + transactionReqItem.getAmount() + "원 입금")
                            .t(LocalDateTime.now())
                            .build();            

            p2_transactionReq = TransactionReq.builder()
                            .accountId(p2_accountId)
                            .counterpartyAccountId(p1_accountId)
                            .transferType(TransferType.TRANSFER_IN)
                            .amount(transactionReqItem.getAmount())
                            .balanceAfter(p2_accountItem.get().getBalance() + transactionReqItem.getAmount())
                            .memo("")
                            .t(LocalDateTime.now())
                            .build();
        }

        executeTransfer(transactionReqItem.getAccountNumber(), p1_transactionReq, p2_transactionReq);
    }    
}



//response 수정
//withdrawal에 executeTransfer로 수정