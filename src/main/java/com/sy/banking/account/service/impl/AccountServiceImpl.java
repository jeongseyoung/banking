package com.sy.banking.account.service.impl;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.sy.banking.account.mapper.AccountMapper;
import com.sy.banking.account.service.AccountService;
import com.sy.banking.auth.mapper.UserMapper;
import com.sy.banking.domain.dto.UserDto;
import com.sy.banking.domain.item.AccountItem;
import com.sy.banking.domain.item.UserItem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final UserMapper userMapper;
    private final AccountMapper accountMapper;
    private static final Random random = new Random();

    @Override
    public AccountItem createAccount(UserItem userItem) {

        if (userMapper.findByEmail(userItem.getEmail()).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 사용자, 계좌생성 불가능");
        };

        String accountNumber = generateAccountNumber();
        log.info("userId {} {} {} {}", userItem.getUserId(), userItem.getEmail(), userItem.getName(), userItem.getUsername());
        AccountItem accountItem = new AccountItem(userItem.getUserId(), accountNumber, 0, "ACTIVE", LocalDateTime.now());
        accountMapper.insertAccountInfo(accountItem);

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

        String accountNumber;
        
        do {
            accountNumber = createAccountNum();
        } while (accountMapper.existingAccount(accountNumber).isPresent());

        return accountNumber;
    }
}
