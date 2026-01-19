package com.sy.banking.transfer.service;


import com.sy.banking.domain.item.req.TransactionReq;
import com.sy.banking.domain.item.req.TransferReqItem;
import com.sy.banking.domain.item.req.WithdrawalReqItem;
import com.sy.banking.domain.item.res.TransactionRes;

public interface TransferService {

    TransactionRes deposit(TransferReqItem transactionReqItem); //입금 ex)cd기 - System_deposit

    TransactionRes withdrawal(WithdrawalReqItem withdrawalReqItem);

    TransactionRes transfer(TransferReqItem transferReqItem);

    TransactionRes interest(String accountNumber, TransactionReq transactionReq);

}
