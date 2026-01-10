package com.sy.banking.config.jwt;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

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

    // @Override
    // @Transactional(readOnly = true)
    // public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    //     return userRepository.findByEmail(email)
    //             .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    // }

    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) {

        UserDto userDto = userMapper.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("없는 유저 입니다.: " + id));
        
        return new CustomUserDetails(userDto);
    }

    // @Transactional(readOnly = true)
    // public UserDetails loadUserByUsername(long userId) throws UsernameNotFoundException {
    //      return userMapper.getUser(userId).orElseThrow(() -> new UsernameNotFoundException("NOT FOUND [" + userId + "]"));
    // }

    // @Transactional(readOnly = true)
    // public UserDetails loadUserById(Long userId) {
    //     return userMapper.getUser(userId).orElseThrow(() -> new UsernameNotFoundException("NOT FOUND [" + userId + "]"));
    // }
    // @Transactional(readOnly = true)
    // public UserDetails loadUserById(Long id) {
    //     return userMapper.getUser(id)
    //             .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    // }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDto userDto = userMapper.findByUserDtoEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("없는 유저 입니다.: " + email));
        
        return new CustomUserDetails(userDto);
    }
}
