package com.example.base;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sy.banking.BankingApplication;
import com.sy.banking.account.mapper.AccountMapper;
import com.sy.banking.domain.item.AccountItem;
import com.sy.banking.transfer.service.TransferService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = BankingApplication.class)
@Slf4j
public class HelperMethod {
     @Autowired
    protected TransferService transferService;
    
    @Autowired
    protected AccountMapper accountMapper;

    protected static final String TEST_ACCOUNT_NUMBER = "02330-35-02978900";
    protected static final long INITIAL_BALANCE = 10000L;

    protected String accountA = "02330-35-02978900";
    protected String accountB = "01212-32-12940482";
    
    // ========== 헬퍼 메서드들 ==========
    
    /**
     * 계좌 잔액을 특정 금액으로 설정
     */
    protected void setupAccount(String accountNumber, long balance) {
        AccountItem account = accountMapper.existingAccount(accountNumber)
            .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없음: " + accountNumber));
        
        AccountItem setupItem = AccountItem.builder()
            .accountId(account.getAccountId())
            .accountNumber(accountNumber)
            .balance(balance)
            .createdAt(account.getCreatedAt())
            .userId(account.getUserId())  // 필요시 추가
            .build();
        
        accountMapper.updateAccountInfo(setupItem);
        log.info("계좌 {} 잔액을 {}원으로 설정", accountNumber, balance);  
    }
    
    /**
     * 계좌 잔액 조회
     */
    protected long getBalance(String accountNumber) {
        AccountItem account = accountMapper.existingAccount(accountNumber)
            .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없음: " + accountNumber));
        return account.getBalance();
    }
    
    /**
     * 여러 계좌를 한번에 초기화
     */
    protected void setupMultipleAccounts(String... accountNumbers) {
        for (String accountNumber : accountNumbers) {
            setupAccount(accountNumber, 10000L);  // 기본 1만원
        }
    }
    
    // ========== 기존 테스트들 ==========
    
    @BeforeEach
    void setup() {
        log.info("=== 테스트 초기화 시작 ===");
        
        AccountItem existingAccount = accountMapper.existingAccount(TEST_ACCOUNT_NUMBER)
            .orElse(null);
        
        if (existingAccount != null) {
            setupAccount(TEST_ACCOUNT_NUMBER, INITIAL_BALANCE);  // 헬퍼 메서드 사용
        } else {
            log.warn("계좌를 찾을 수 없음: {}", TEST_ACCOUNT_NUMBER);
        }
        
        log.info("초기 잔액 확인: {}원", getBalance(TEST_ACCOUNT_NUMBER));  // 헬퍼 메서드 사용
        log.info("=== 테스트 초기화 완료 ===\n");
    }
}

