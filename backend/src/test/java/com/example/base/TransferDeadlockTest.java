package com.example.base;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sy.banking.domain.item.req.TransferReqItem;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransferDeadlockTest extends HelperMethod{

    @Test
    @DisplayName("A→B, B→A 동시 이체 시 데드락 방지 테스트")
    void transferDeadlockTest() throws InterruptedException {        
        
        setupAccount(accountA, 10000L);
        setupAccount(accountB, 10000L);

        long initialTotal = getBalance(accountA) + getBalance(accountB);

        int numOfThreads = 50;
        long transferAmount = 100L;

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(numOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        //AtomicInteger failCount = new AtomicInteger(0);

        for(int i = 0; i < numOfThreads; i++) {
            final int threadNum = i;
            executorService.submit(() -> {
                try {
                    TransferReqItem req = new TransferReqItem();
                    req.setAmount(transferAmount);

                    if(threadNum % 2 == 0) {
                        req.setAccountNumber(accountA);
                        req.setCounterpartyAccountNumber(accountB);
                    } else {
                        req.setAccountNumber(accountB);
                        req.setCounterpartyAccountNumber(accountA);
                    }

                    transferService.transfer(req);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    log.error("Thread-{} 실패: {}", threadNum, e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        long finalTotal = getBalance(accountA) + getBalance(accountB);

        assertEquals(initialTotal, finalTotal, "총 금액 변경 완료");
    }
}
