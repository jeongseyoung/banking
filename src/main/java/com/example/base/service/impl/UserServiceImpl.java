package com.example.base.service.impl;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.base.domain.dto.UserDto;
import com.example.base.domain.item.UserItem;
import com.example.base.enumbox.Role;
import com.example.base.exception.UserException;
import com.example.base.exception.enumbox.UserEnum;
import com.example.base.mapper.UserMapper;
import com.example.base.service.UserService;

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
        System.out.println("service user: " + user.get().getFirstName());
        return new UserItem(user.get());
    } 

    @Override
    public ResponseEntity<String> addUser(UserDto user) {
        
        UserItem userItem = new UserItem(user.getFirstName(), 
                                        user.getLastName(), 
                                        user.getUserEmail(),
                                        Role.ROLE_USER,
                                        passwordEncoder.encode(user.getUserPassword()));

        int result = userMapper.addUser(userItem);
        int cntuserId = userMapper.countByuserId(user.getUserId());

        log.info("cntuserId {}", cntuserId);
        log.info("result {}", result);

        if(result == 0) throw new UserException(UserEnum.ADD_FAILED);

        return ResponseEntity.ok("가입완료");
    }
    

    //add 할때 비밀번호 인코딩  String encodedPassword = passwordEncoder.encode(rawPassword);
}
