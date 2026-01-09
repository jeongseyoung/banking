package com.sy.banking.account.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sy.banking.domain.item.AccountItem;

@Mapper
public interface AccountMapper {

    //계좌중복,존재 여부 확인
    Optional<AccountItem> existingAccount(@Param("accountNumber") String accountNumber);

    //account정보 저장
    void insertAccountInfo(@Param("account") AccountItem accountItem);
}
