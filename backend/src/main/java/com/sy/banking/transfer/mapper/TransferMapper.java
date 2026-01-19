package com.sy.banking.transfer.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sy.banking.domain.item.ASPageItem;
import com.sy.banking.domain.item.TransactionListItem;
import com.sy.banking.domain.item.req.TransactionReq;

@Mapper
public interface TransferMapper {

    void saveTransaction(TransactionReq transactionReq);

    List<TransactionListItem> findListByAccountId(@Param("accountId") long accountId, @Param("page") ASPageItem asPageItem);

    //total count
    long countByAccountId(long accountId);
}
