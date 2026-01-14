package com.sy.banking.domain.dto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
