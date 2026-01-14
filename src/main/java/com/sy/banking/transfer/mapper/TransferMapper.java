package com.sy.banking.transfer.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.sy.banking.domain.item.req.TransactionReq;

@Mapper
public interface TransferMapper {
    void saveTransaction(TransactionReq transactionReq);

}
