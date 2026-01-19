package com.sy.banking.auth.service.impl;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sy.banking.auth.mapper.UserMapper;
import com.sy.banking.auth.service.UserService;
import com.sy.banking.domain.dto.UserDto;
import com.sy.banking.domain.item.UserItem;
import com.sy.banking.enumbox.Role;
import com.sy.banking.exception.UserException;
import com.sy.banking.exception.enumbox.UserEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserItem getUser(long userId) {
        
        Optional<UserDto> user = userMapper.getUser(userId);
        System.out.println("service user: " + user.get().getName());
        return new UserItem(user.get());
    } 

    @Override
    public ResponseEntity<String> addUser(UserDto user) {
        
        UserItem userItem = new UserItem(user.getEmail(), 
                                        passwordEncoder.encode(user.getPassword()),
                                        user.getName(),
                                        Role.ROLE_USER
                                        );

        int result = userMapper.addUser(userItem);
        int cntuserId = userMapper.countByuserId(user.getUserId());

        log.info("cntuserId {}", cntuserId);
        log.info("result {}", result);

        if(result == 0) throw new UserException(UserEnum.ADD_FAILED);

        return ResponseEntity.ok("가입완료");
    }
    

    //add 할때 비밀번호 인코딩  String encodedPassword = passwordEncoder.encode(rawPassword);
}
