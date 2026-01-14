package com.sy.banking.domain.item;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.sy.banking.domain.dto.UserDto;
import com.sy.banking.enumbox.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserItem implements OAuth2User, UserDetails {
    //private long userId;
    private long userId;
    private String email;
    private String password;
    private String name;
    private Role user_Role;
    private Date created_at;

    //object -> 구글 로그인 정보 들어감
    private Map<String, Object> attributes;

    //생성자1
    public UserItem(UserDto user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.user_Role = user.getUser_Role();
    }

    //생성자2 - OAuth2 로그인용
    public UserItem(String email, String password, String name, Role user_Role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.user_Role = user_Role;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
         if (user_Role == null) {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
        return List.of(new SimpleGrantedAuthority(user_Role.name()));
    }

    @Override
    public String getName() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.name;
    }
}
