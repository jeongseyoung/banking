package com.sy.banking.account.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;


import org.springframework.stereotype.Service;

import com.sy.banking.account.mapper.AccountMapper;
import com.sy.banking.account.service.AccountService;
import com.sy.banking.auth.mapper.UserMapper;
import com.sy.banking.domain.item.ASPageItem;
import com.sy.banking.domain.item.AccountItem;
import com.sy.banking.domain.item.TransactionListItem;
import com.sy.banking.domain.item.UserItem;
import com.sy.banking.domain.item.res.AccountItemResponse;
import com.sy.banking.domain.paging.PageResponse;
import com.sy.banking.exception.AccountException;
import com.sy.banking.exception.UserException;
import com.sy.banking.exception.enumbox.AccountEnum;
import com.sy.banking.exception.enumbox.UserEnum;
import com.sy.banking.transfer.mapper.TransferMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final UserMapper userMapper;
    private final AccountMapper accountMapper;
    private final TransferMapper transferMapper;
    private static final SecureRandom random = new SecureRandom();
    private static final int MAX_ACCOUNT_NUMBER_ATTEMPTS = 1000;

    @Override
    public AccountItem createAccount(UserItem userItem) {

        UserItem user = userMapper.findByEmail(userItem.getEmail()).orElseThrow(() -> new UserException(UserEnum.USER_NOT_FOUND));

        String accountNumber = generateAccountNumber();
        log.info("userId {} {} {} {}", user.getUserId(), user.getEmail(), user.getName(), user.getUsername());
        AccountItem accountItem = new AccountItem(user.getUserId(), accountNumber, 0, "ACTIVE", LocalDateTime.now());

        int result = accountMapper.insertAccountInfo(accountItem);

        if(result == 0) {
            log.error("계좌생성실패: userId = {}", accountItem.getUserId());
            throw new AccountException(AccountEnum.ACCOUNT_CREATE_FAILED);
        }

        log.info("계좌 생성 완료: userId={}, accountNumber={}", user.getUserId(), accountItem.getAccountNumber());
        return accountItem;
    }


    //계좌번호 생성 ex)00000-00-00000000
    private static String createAccountNum() {
        
        String part1 = generateDigits(5);
        String part2 = generateDigits(2);
        String part3 = generateDigits(8);

        return part1 + "-" + part2 + "-" + part3;
    }
    
    //length만큼 난수 생성
    private static String generateDigits(int length) {

        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < length; i++) {
            stringBuilder.append(random.nextInt(10));
        }

        return stringBuilder.toString();
    }

    //계좌 중복, 존재여부 확인
    private String generateAccountNumber() {
        int attempts = 0;

        //최대시도횟수 1000번 - 실패 시 exception 던짐.
        while(attempts < MAX_ACCOUNT_NUMBER_ATTEMPTS) {
            String accountNumber = createAccountNum();
            if(accountMapper.existingAccount(accountNumber).isEmpty()) {
                return accountNumber;
            }
            attempts++;
        }

        log.error("계좌번호 생성 실패: {}번 시도 후 모두 중복", MAX_ACCOUNT_NUMBER_ATTEMPTS);
        throw new AccountException(AccountEnum.ACCOUNT_CREATE_FAILED);     
        
    }

    /* private int page;
    private int size;
    private int totalPage;
    private int totalCount;
    private List<T> list; */ 
    @Override
    public PageResponse<TransactionListItem> getMyAccountStatement(ASPageItem asPageItem, UserItem userItem) {

        log.info("getMyAccountStatement/impl {}, {}, {}", asPageItem.getPage(), asPageItem.getSize(), userItem.getUserId());

        AccountItem accountItem = accountMapper.findAccountIdByUserId(userItem.getUserId()).orElseThrow(() -> new AccountException(AccountEnum.ACCOUNT_NOT_FOUND));
        long accountId = accountItem.getAccountId();
        String accountNumber = accountItem.getAccountNumber();
        String status = accountItem.getStatus();

        long totalCount = transferMapper.countByAccountId(accountId);
        log.info("{} {} {}", accountId, accountNumber, status);
        List<TransactionListItem> list = transferMapper.findListByAccountId(accountId, asPageItem);
        return PageResponse.page(
            asPageItem.getPage(),
            asPageItem.getSize(),
            totalCount,
            status,
            accountNumber,
            list            
        );
    }

    @Override
    public AccountItemResponse getMyAccountInfo(long userId) {

        List<AccountItem> accounts = accountMapper.findMyAccountsByUserId(userId);
        if(accounts.isEmpty())
            throw new AccountException(AccountEnum.ACCOUNT_NOT_FOUND);
        return AccountItemResponse.of(accounts);        
    }


    @Override
    public List<TransactionListItem> findAccountByUserId(long userId) {

        AccountItem accountItem = accountMapper.findAccountIdByUserId(userId).orElseThrow(() -> new AccountException(AccountEnum.ACCOUNT_NOT_FOUND));
        return transferMapper.findListByAccountId_NoPaging(accountItem.getAccountId());

    }
}
