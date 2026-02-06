package com.example.base;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sy.banking.BankingApplication;
import com.sy.banking.account.mapper.AccountMapper;
import com.sy.banking.domain.item.AccountItem;
import com.sy.banking.domain.item.req.TransferReqItem;
import com.sy.banking.transfer.service.TransferService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = BankingApplication.class)
@Slf4j
public class ConcurrencyTransferTest {
    
    @Autowired
    private TransferService transferService;
    @Autowired
    private AccountMapper accountMapper;

    private static final String TEST_ACCOUNT_NUMBER = "02330-35-02978900";
    private static final long INITIAL_BALANCE = 0L;
    @BeforeEach
    void setup() {
        log.info("=== 테스트 초기화 시작 BeforeEach ===");

        AccountItem existingAccount = accountMapper.existingAccount(TEST_ACCOUNT_NUMBER).orElse(null); 

        if(existingAccount != null) {
            AccountItem resetItem = AccountItem.builder().accountId(existingAccount.getAccountId()).accountNumber(TEST_ACCOUNT_NUMBER)
            .balance(INITIAL_BALANCE).createdAt(existingAccount.getCreatedAt()).build();
            accountMapper.updateAccountInfo(resetItem);
        } else {
            log.warn("계좌를 찾을 수 없음 {} ", TEST_ACCOUNT_NUMBER);
        }

        AccountItem initialAccount = accountMapper.existingAccount(TEST_ACCOUNT_NUMBER).orElseThrow(() -> new RuntimeException("계좌 초기화 실패"));

        log.info("초기 잔액 확인 {}", initialAccount.getBalance());
        
        log.info("=== 테스트 초기화 완료 ===");
    }



    /*
    "100개의 스레드가 동시에 한 계좌에 100원씩 입금했을 때, 최종 잔액이 정확히 10,000원이 되는가?"
    */
    @Test
    @DisplayName("비관적 락 테스트(동시입금)")
    void DepositTest() throws InterruptedException {

        int numOfThreads = 100;
        long amount = 100L;
        long expectedBalance = INITIAL_BALANCE + (numOfThreads * amount);

        log.info("=== 동시성 테스트 시작 ===");

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(numOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        

        for(int i = 0; i < numOfThreads; i++) {
            final int threadNum = i;
            executorService.submit(() -> {
                try {
                    TransferReqItem req = new TransferReqItem();
                    req.setAccountNumber(TEST_ACCOUNT_NUMBER);
                    req.setAmount(amount);

                    transferService.deposit(req);
                    successCount.incrementAndGet();

                    if(threadNum % 20 == 0) {
                        log.info("Thread - {} 입금성공", threadNum);
                    }
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    log.info("Thread - {}, error {} ", threadNum, e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); //스레드 종료될 때까지 기다림.
        executorService.shutdown();

        AccountItem finalAccount = accountMapper.existingAccount(TEST_ACCOUNT_NUMBER).orElseThrow(() -> new RuntimeException("계좌 조회 실패"));
        log.info("finalAccount 최종 잔액 {} ", finalAccount.getBalance());
        log.info("차이: {}원", Math.abs(finalAccount.getBalance() - expectedBalance));

        //50개 입금(+5000), 50개 출금(-5000) = 50,000원 유지
        assertEquals(expectedBalance, finalAccount.getBalance(), "잔액일치하지않음");
    }
}
