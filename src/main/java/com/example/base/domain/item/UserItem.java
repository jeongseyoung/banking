package com.example.base.domain.item;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.base.domain.dto.UserDto;
import com.example.base.enumbox.Role;

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
    private String firstName;
    private String lastName;
    private String userEmail;
    private Role user_Role;
    private String userPassword;

    private Map<String, Object> attributes;

    //생성자1
    public UserItem(UserDto user) {
        //this.userId = user.getUserId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userEmail = user.getUserEmail();
        this.user_Role = user.getUser_Role();
    }

    //생성자2 - OAuth2 로그인용
    public UserItem(String firstName, String lastName, String userEmail, Role user_Role, String userPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userEmail = userEmail;
        this.user_Role = user_Role;
        this.userPassword = userPassword;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user_Role.name()));
    }

    @Override
    public String getName() {
        return this.userEmail;
    }

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.firstName + " " + this.lastName;
    }
}
