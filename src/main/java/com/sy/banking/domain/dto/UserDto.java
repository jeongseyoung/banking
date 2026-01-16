package com.sy.banking.domain.dto;

import java.time.LocalDateTime;

import org.apache.ibatis.type.Alias;

import com.sy.banking.enumbox.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Alias("UserDto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto{
    private long userId;
    private String email;
    private String password;
    private String name;
    private Role user_Role;
    private LocalDateTime created_at;

}
