package com.sy.banking.transfer.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.sy.banking.account.mapper.AccountMapper;
import com.sy.banking.domain.item.AccountItem;
import com.sy.banking.domain.item.req.TransactionReq;
import com.sy.banking.domain.item.req.TransferReqItem;
import com.sy.banking.domain.item.req.WithdrawalReqItem;
import com.sy.banking.domain.item.res.TransactionRes;
import com.sy.banking.enumbox.TransferType;
import com.sy.banking.exception.AccountException;
import com.sy.banking.exception.TransferException;
import com.sy.banking.exception.enumbox.AccountEnum;
import com.sy.banking.exception.enumbox.TransferEnum;
import com.sy.banking.transfer.mapper.TransferMapper;
import com.sy.banking.transfer.service.TransferService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class TransferServiceImpl implements TransferService{

    private final AccountMapper accountMapper;
    private final TransferMapper transferMapper;

    private TransactionRes executeTransfer(String accountNumber, TransactionReq...transactionReqs) {
        for(TransactionReq t : transactionReqs) {
            update(new AccountItem(t.getAccountId(), t.getBalanceAfter()));
            save(t);
        }
        return res(transactionReqs[0], accountNumber);
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
    public TransactionRes deposit(TransferReqItem transactionReqItem) {

        String accountNumber = transactionReqItem.getAccountNumber();

        Optional<AccountItem> accountItem = Optional.ofNullable(accountMapper.existingAccount(accountNumber))
                                                    .orElseThrow(() -> new TransferException(TransferEnum.NO_ACCOUNT));

        String status = accountItem.get().getStatus();
        if(!status.equals("ACTIVE")) {
            throw new AccountException(AccountEnum.INACTIVE_ACCOUNT);
        }

        long accountId = accountItem.get().getAccountId();
        System.out.println("service deposit accountId: " + accountId);
        long balanceAfter = accountItem.get().getBalance() + transactionReqItem.getAmount();

        TransactionReq transactionReq;
        transactionReq = TransactionReq.builder()
                            .accountId(accountId)
                            .counterpartyAccountId(accountId)
                            .transferType(TransferType.DEPOSIT)
                            .amount(transactionReqItem.getAmount())
                            .balanceAfter(balanceAfter)
                            .memo(transactionReqItem.getAmount() + "원 입금")
                            .t(LocalDateTime.now())
                            .build();

        return executeTransfer(accountNumber, transactionReq);
        //res(transactionReq, accountNumber);
    }

    @Override
    public TransactionRes withdrawal(WithdrawalReqItem withdrawalReqItem) {
        //account select
        Optional<AccountItem> accountItem = Optional.ofNullable(accountMapper.existingAccount(withdrawalReqItem.getAccountNumber()))
                                                    .orElseThrow(() -> new TransferException(TransferEnum.NO_ACCOUNT));
        
        String status = accountItem.get().getStatus();
        if(!status.equals("ACTIVE")) {
            throw new AccountException(AccountEnum.INACTIVE_ACCOUNT);
        }

        String accountNumber = accountItem.get().getAccountNumber();
        long accountId = accountItem.get().getAccountId();
        long balance = accountItem.get().getBalance();
        long amount = withdrawalReqItem.getAmount();
        long balanceAfter = balance - amount;

        if(balance < amount) {
            throw new TransferException(TransferEnum.INSUFFICIENT_BALANCE);
        }

        //tran req
        TransactionReq transactionReq;
        //accountId, TransferType.WITHDRAWAL, amount, balanceAfter, amount + "원 출금"
        transactionReq = TransactionReq.builder()
                                        .accountId(accountId)
                                        .transferType(TransferType.WITHDRAWAL)
                                        .counterpartyAccountId(accountId)
                                        .amount(amount)
                                        .balanceAfter(balanceAfter)
                                        .memo(amount + "원 출금")
                                        .t(LocalDateTime.now())
                                        .build();
        return executeTransfer(accountNumber, transactionReq);
    }

    @Override
    public TransactionRes transfer(TransferReqItem transactionReqItem) {

       //내계좌 상대계좌 같을 시 예외처리
       if(transactionReqItem.getAccountNumber().equals(transactionReqItem.getCounterpartyAccountNumber()))
            throw new TransferException(TransferEnum.SAME_ACCOUNT_TRANSFER);

       String acc1 = transactionReqItem.getAccountNumber();
       String acc2 = transactionReqItem.getCounterpartyAccountNumber();     

       //데드락 방지용 순서비교
       String p1 = acc1.compareTo(acc2) < 0 ? acc1 : acc2;
       String p2 = acc1.compareTo(acc2) < 0 ? acc2 : acc1;

       Optional<AccountItem> p1_accountItem = Optional.ofNullable(accountMapper
        .existingAccount(p1)).orElseThrow(() ->  new TransferException(TransferEnum.NO_ACCOUNT));       
       Optional<AccountItem> p2_accountItem = Optional.ofNullable(accountMapper
        .existingAccount(p2)).orElseThrow(() -> new TransferException(TransferEnum.NO_ACCOUNT));

       String p1_status = p1_accountItem.get().getStatus();
       String p2_status = p2_accountItem.get().getStatus();
       if(!p1_status.equals("ACTIVE") || !p2_status.equals("ACTIVE")) {
            throw new AccountException(AccountEnum.INACTIVE_ACCOUNT);
        } 

       long p1_accountId = p1_accountItem.get().getAccountId();
       long p2_accountId = p2_accountItem.get().getAccountId();

       String p1_accountNumber = p1_accountItem.get().getAccountNumber();
       String p2_accountNumber = p2_accountItem.get().getAccountNumber();

       TransactionReq p1_transactionReq = new TransactionReq();
       TransactionReq p2_transactionReq = new TransactionReq();

       long p1_balance = p1_accountItem.get().getBalance();
       long amount = transactionReqItem.getAmount();
       //잔금이 없거나 -일경우, 잔금보다 요청amount가 많을 경우 exception
       if(p1_balance < amount) {
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
                            .memo(p2_accountNumber + "계좌로 " + transactionReqItem.getAmount() + "원 이체")
                            .t(LocalDateTime.now())
                            .build();            

            p2_transactionReq = TransactionReq.builder()
                            .accountId(p2_accountId)
                            .counterpartyAccountId(p1_accountId)
                            .transferType(TransferType.TRANSFER_IN)
                            .amount(transactionReqItem.getAmount())
                            .balanceAfter(p2_accountItem.get().getBalance() + transactionReqItem.getAmount())
                            .memo(p1_accountNumber + "계좌로부터 이체 받음")
                            .t(LocalDateTime.now())
                            .build();
        }

        return executeTransfer(transactionReqItem.getAccountNumber(), p1_transactionReq, p2_transactionReq);
    }

    @Override
    public TransactionRes interest(String accountNumber, TransactionReq transactionReq) {
        return executeTransfer(accountNumber, transactionReq);
    }    
}
