package com.sy.banking.config.jwt;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sy.banking.domain.dto.UserDto;


public class CustomUserDetails implements UserDetails{

    private final UserDto userDto;

    public CustomUserDetails(UserDto userDto) {
        this.userDto = userDto;
    }

    public UserDto getUserDto() {
        return this.userDto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userDto.getUser_Role().name()));
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return userDto.getPassword();
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return userDto.getUsername();
    }
    
    
}
