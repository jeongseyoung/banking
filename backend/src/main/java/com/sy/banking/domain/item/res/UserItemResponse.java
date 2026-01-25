package com.sy.banking.domain.item.res;

import java.time.LocalDateTime;

import com.sy.banking.enumbox.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserItemResponse {
    
    private String email;
    private String name;
    private Role userRole;
    private LocalDateTime createdAt;

    // public UserItemResponse(String email, String name, Role user_Role, LocalDateTime createdAt) {
    //     this.email = email;
    //     this.name = name;
    //     this.userRole = user_Role;
    //     this.createdAt = createdAt;
    // }
}
