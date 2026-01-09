package com.sy.banking.domain.dto;

import java.time.LocalDateTime;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Alias("AccountDto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    
    private long userId;
    private String accountNumber;
    private long balance;
    private String status;
    private LocalDateTime created_at;

}
