package com.sy.banking.account.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sy.banking.domain.item.AccountItem;

@Mapper
public interface AccountMapper {
    
    //account정보 저장
    void insertAccountInfo(@Param("account") AccountItem accountItem);

    //계좌 업데이트
    void updateAccountInfo(@Param("account") AccountItem accountItem);

    //계좌중복, 존재 여부, select
    Optional<AccountItem> existingAccount(@Param("accountNumber") String accountNumber);

    //active계정
    List<AccountItem> fidAllActiveAccount();

    //userId로 accountId찾기 -- isPrimary 만들어야될듯? 주계좌설정해야됨.
    Optional<AccountItem> findAccountIdByUserId(long userId);

    //userId로 accounts(list)찾기
    List<AccountItem> findMyAccountsByUserId(long userId);
}
