package com.sy.banking.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sy.banking.auth.mapper.UserMapper;

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

    // @Transactional(readOnly = true)
    // public UserDetails loadUserById(Long id) {
    //     return userRepository.findById(id)
    //             .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    // }

    // @Transactional(readOnly = true)
    // public UserDetails loadUserByUsername(long userId) throws UsernameNotFoundException {
    //      return userMapper.getUser(userId).orElseThrow(() -> new UsernameNotFoundException("NOT FOUND [" + userId + "]"));
    // }

    // @Transactional(readOnly = true)
    // public UserDetails loadUserById(Long userId) {
    //     return userMapper.getUser(userId).orElseThrow(() -> new UsernameNotFoundException("NOT FOUND [" + userId + "]"));
    // }
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) {
        return userMapper.getUser(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
    }
}
