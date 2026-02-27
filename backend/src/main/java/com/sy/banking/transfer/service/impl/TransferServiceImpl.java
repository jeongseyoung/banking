package com.sy.banking.transfer.service.impl;

import java.time.LocalDateTime;

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
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.REPEATABLE_READ)
@Slf4j
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
        int result = accountMapper.updateAccountInfo(accountItem);
        if(result == 0) {
            throw new TransferException(TransferEnum.UPDATE_FAILED);
        }
    }
    private void save(TransactionReq t){
        int result = transferMapper.saveTransaction(t);
        if(result == 0) {
            throw new TransferException(TransferEnum.SAVE_FAILED);
        }
    }

    //response
    private TransactionRes res(TransactionReq transactionReq, String accountNumber) {
        return new TransactionRes(true, accountNumber, transactionReq.getAmount(), transactionReq.getBalanceAfter(), transactionReq.getMemo(), transactionReq.getT());    
    }

    @Override
    public TransactionRes deposit(TransferReqItem transactionReqItem) {

        String accountNumber = transactionReqItem.getAccountNumber();

        //유효한 amount인지 확인
        long amount = transactionReqItem.getAmount();
        validateAmount(amount);
        
        AccountItem accountItem = accountMapper.existingAccount(accountNumber)
                                                    .orElseThrow(() -> new TransferException(TransferEnum.NO_ACCOUNT));

        //계좌 상태 확인
        String status = accountItem.getStatus();
        validateAccountStatus(status);


        long accountId = accountItem.getAccountId();
        long balanceAfter = accountItem.getBalance() + transactionReqItem.getAmount();

        log.info("입금 시작 ----- accountId: {}, amount: {}, balanceAfter: {} ----", accountId, amount, balanceAfter);

        TransactionReq transactionReq = TransactionReq.builder()
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

    //출금
    @Override
    public TransactionRes withdrawal(WithdrawalReqItem withdrawalReqItem) {

        String accountNumber = withdrawalReqItem.getAccountNumber();
        long amount = withdrawalReqItem.getAmount();

        validateAmount(amount);

        //account select
        AccountItem accountItem = accountMapper.existingAccount(accountNumber)
                                                    .orElseThrow(() -> new TransferException(TransferEnum.NO_ACCOUNT));
        
        String status = accountItem.getStatus();
        validateAccountStatus(status);

        long accountId = accountItem.getAccountId();
        long balance = accountItem.getBalance();
        long balanceAfter = balance - amount;

        if(balance < amount) {
            log.warn("잔액부족: accountId = {}, balance = {}, amount = {}", accountId, balance, amount);
            throw new TransferException(TransferEnum.INSUFFICIENT_BALANCE);
        }

        log.info("출금 시작 ----- accountId: {}, balance = {}, amount = {} -> {}----", accountId, balance, amount, balanceAfter);

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

    //이체
    @Override
    public TransactionRes transfer(TransferReqItem transactionReqItem) {

        String fromAccount = transactionReqItem.getAccountNumber();
        String toAccount = transactionReqItem.getCounterpartyAccountNumber();
        long amount = transactionReqItem.getAmount();

        validateAmount(amount);

       //내계좌 상대계좌 같을 시 예외처리
       if(fromAccount.equals(toAccount))
            throw new TransferException(TransferEnum.SAME_ACCOUNT_TRANSFER);

       //데드락 방지용 순서비교
       String p1 = fromAccount.compareTo(toAccount) < 0 ? fromAccount : toAccount;
       String p2 = fromAccount.compareTo(toAccount) < 0 ? toAccount : fromAccount;

       AccountItem first = accountMapper.existingAccount(p1).orElseThrow(() ->  new TransferException(TransferEnum.NO_ACCOUNT));       
       AccountItem second = accountMapper.existingAccount(p2).orElseThrow(() ->  new TransferException(TransferEnum.NO_ACCOUNT));       

       String p1_status = first.getStatus();
       validateAccountStatus(p1_status);
       String p2_status = second.getStatus();
       validateAccountStatus(p2_status);

       AccountItem from = fromAccount.equals(first.getAccountNumber()) ? first : second;
       AccountItem to = toAccount.equals(first.getAccountNumber()) ? first : second;
       long p1_accountId = from.getAccountId();
       long p2_accountId = to.getAccountId();

       String p1_accountNumber = from.getAccountNumber();
       String p2_accountNumber = to.getAccountNumber();

       TransactionReq p1_transactionReq = new TransactionReq();
       TransactionReq p2_transactionReq = new TransactionReq();

       long fromBalance = from.getBalance();
       //잔금이 없거나 -일경우, 잔금보다 요청amount가 많을 경우 exception
       if(fromBalance < amount) {
            log.warn("잔액부족: accountId={}, balance={}, amount={}", from.getAccountId(), from.getBalance(), amount);
            throw new TransferException(TransferEnum.INSUFFICIENT_BALANCE);
       }

       log.info("이체시작: from={}, to={}, amount={}", from.getAccountNumber(), to.getAccountNumber(), amount);

            p1_transactionReq = TransactionReq.builder()
                            .accountId(p1_accountId)
                            .counterpartyAccountId(p2_accountId)
                            .transferType(TransferType.TRANSFER_OUT)
                            .amount(transactionReqItem.getAmount())
                            .balanceAfter(from.getBalance() - transactionReqItem.getAmount())
                            .memo(p2_accountNumber + "계좌로 " + transactionReqItem.getAmount() + "원 이체")
                            .t(LocalDateTime.now())
                            .build();            

            p2_transactionReq = TransactionReq.builder()
                            .accountId(p2_accountId)
                            .counterpartyAccountId(p1_accountId)
                            .transferType(TransferType.TRANSFER_IN)
                            .amount(transactionReqItem.getAmount())
                            .balanceAfter(to.getBalance() + transactionReqItem.getAmount())
                            .memo(p1_accountNumber + "계좌로부터 이체 받음")
                            .t(LocalDateTime.now())
                            .build();

        return executeTransfer(transactionReqItem.getAccountNumber(), p1_transactionReq, p2_transactionReq);
    }

    //이자
    @Override
    public TransactionRes interest(String accountNumber, TransactionReq transactionReq) {
        return executeTransfer(accountNumber, transactionReq);
    }
    
    //잔액 확인
    private void validateAmount(long amount) {
        if(amount <= 0) {
            log.warn("잘못된 금액: {}", amount);
            throw new TransferException(TransferEnum.INVALID_AMOUNT);
        }
        if(amount > 10_000_000) {
            log.warn("금액 초과 {}", amount);
            throw new TransferException(TransferEnum.AMOUNT_EXCEEDED);
        }
    }

    //active상태인지 확인
    private void validateAccountStatus(String status) {
        if(!"ACTIVE".equals(status)) {
            throw new AccountException(AccountEnum.INACTIVE_ACCOUNT);
        }
    }
}
