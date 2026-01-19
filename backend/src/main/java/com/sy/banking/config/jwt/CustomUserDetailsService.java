package com.sy.banking.config.jwt;

import lombok.RequiredArgsConstructor;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sy.banking.auth.mapper.UserMapper;
import com.sy.banking.domain.dto.UserDto;
import com.sy.banking.domain.item.UserItem;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    //private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) {
        System.out.println("id loadById: " + id);
        UserItem userItem = userMapper.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("없는 유저 입니다.: " + id));
        System.out.println("userItem loadById: " + userItem.getAuthorities());
        return userItem;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDto userDto = userMapper.findByUserDtoEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("없는 유저 입니다.: " + email));
        
        return new UserItem(userDto);
    }
}
