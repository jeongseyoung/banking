package com.example.base;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sy.banking.BankingApplication;
import com.sy.banking.account.mapper.AccountMapper;
import com.sy.banking.domain.item.AccountItem;
import com.sy.banking.domain.item.req.TransferReqItem;
import com.sy.banking.domain.item.req.WithdrawalReqItem;
import com.sy.banking.transfer.service.TransferService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = BankingApplication.class)
@Slf4j
public class MixedDepositAndWithdrawalTest {
    
    @Autowired
    private TransferService transferService;
    @Autowired
    private AccountMapper accountMapper;

    private static final String TEST_ACCOUNT_NUMBER = "02330-35-02978900";
    //private static final long INITIAL_BALANCE = 0L;

    @Test
    @DisplayName("입금 출금 동시 발생 시 잔액 정합성 test")
    void MixedDepositAndWithdrawal() throws InterruptedException {
        AccountItem account = accountMapper.existingAccount(TEST_ACCOUNT_NUMBER).orElseThrow();
        AccountItem setupItem = AccountItem.builder()
                                            .accountId(account.getAccountId())
                                            .accountNumber(TEST_ACCOUNT_NUMBER)
                                            .balance(50000L).createdAt(account.getCreatedAt()).build();

        accountMapper.updateAccountInfo(setupItem);

        int numOfThreads = 100;
        long amount = 100L;

        //log.info("===  ===");

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(numOfThreads);
        AtomicInteger  deposit_Success = new AtomicInteger(0);
        AtomicInteger  withdrawal_Success = new AtomicInteger(0);

        for(int i = 0; i < numOfThreads; i++) {
            final int threadNum = i;
            executorService.submit(() -> {
                try {

                    TransferReqItem t_req = new TransferReqItem();
                    t_req.setAccountNumber(TEST_ACCOUNT_NUMBER);
                    t_req.setAmount(amount);

                    WithdrawalReqItem w_req = new WithdrawalReqItem();
                    w_req.setAccountNumber(TEST_ACCOUNT_NUMBER);
                    w_req.setAmount(amount);

                    if(threadNum % 2 == 0) {
                        transferService.deposit(t_req);
                        deposit_Success.incrementAndGet();

                        transferService.withdrawal(w_req);
                        withdrawal_Success.incrementAndGet();
                    }
                } catch (Exception e) {
                    log.error("Thread {} 실패 {}", threadNum, e.getMessage());
                } finally {
                latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        log.info("입금: {}건, 출금: {}건", deposit_Success.get(), withdrawal_Success.get());  

        AccountItem finalAccount = accountMapper.existingAccount(TEST_ACCOUNT_NUMBER).orElseThrow();
        long expectedBalance  = 50000L + (deposit_Success.get() * amount) - (withdrawal_Success.get() * amount);

        log.info("최종잔액: {}원, 예상: {}원", finalAccount.getBalance(), expectedBalance);  
        assertEquals(expectedBalance, finalAccount.getBalance());
    }
}
