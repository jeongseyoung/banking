package com.sy.banking.transfer.service;


import com.sy.banking.domain.item.req.TransferReqItem;
import com.sy.banking.domain.item.req.WithdrawalReqItem;

public interface TransferService {

    void deposit(TransferReqItem transactionReqItem); //입금 ex)cd기 - System_deposit

    void withdrawal(WithdrawalReqItem withdrawalReqItem);

    void transfer(TransferReqItem transferReqItem);


}
