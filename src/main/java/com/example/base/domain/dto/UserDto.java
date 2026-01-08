package com.example.base.domain.dto;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.base.enumbox.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Alias("UserDto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements UserDetails{
    private long userId;
    private String firstName;
    private String lastName;
    private String userEmail;
    private Role user_Role;
    private String userPassword;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user_Role.name()));
    }

    // enum Role{
    //     ROLE_USER,
    //     ROLE_ADMIN;
    // }

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.firstName + " " + this.lastName;
    }
}
