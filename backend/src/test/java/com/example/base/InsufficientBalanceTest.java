package com.example.base;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sy.banking.domain.item.req.WithdrawalReqItem;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InsufficientBalanceTest extends HelperMethod{
    
    @Test
    @DisplayName("잔액 1000원일 때, 100명이 동시에 100원 출금")
    void insufficientBalanceTest() throws InterruptedException {

        //잔액설정
        setupAccount(TEST_ACCOUNT_NUMBER, 1000L);

        int numOfThreads = 100;
        long withdrawalAmount = 100L;

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(numOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < numOfThreads; i++) {
            final int threadNum = i;
            executorService.submit(() -> {
                try {
                    WithdrawalReqItem req = new WithdrawalReqItem();
                    req.setAccountNumber(TEST_ACCOUNT_NUMBER);
                    req.setAmount(withdrawalAmount);

                    transferService.withdrawal(req);
                    successCount.incrementAndGet();

                    if(successCount.get() <= 10) {
                        log.info("thread: {}, success: {}번째", threadNum, successCount.get());
                    }
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    if(failCount.get() <= 5) 
                        log.debug("thread: {}, 실패: {}", threadNum, e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();//다 돌아갈때까지 기다림
        executorService.shutdown();

        long finalBalance = getBalance(TEST_ACCOUNT_NUMBER);

        log.info("성공: {}번, 실패: {}번, 최종잔액: {}원", successCount.get(), failCount.get(), finalBalance);  

        assertEquals(10, successCount.get(), "10번만 성공해야됨");
        assertEquals(0L, finalBalance, "최종잔액 -> 0원이어야 함");
    }
}
