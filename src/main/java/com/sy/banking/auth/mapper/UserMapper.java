package com.sy.banking.auth.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sy.banking.domain.dto.UserDto;
import com.sy.banking.domain.item.UserItem;
import com.sy.banking.enumbox.Role;


@Mapper
public interface UserMapper {
    Optional<UserDto> getUser(@Param("userId") long userId);

    int addUser(@Param("user") UserItem userItem);

    int countByuserId(@Param("userId") long userId);
    
    int countByemail(@Param("email") String email);

    Optional<UserItem> findByEmail(@Param("email") String email);

    Optional<UserDto> findByUserDtoEmail(String email);

    Optional<UserItem> findById(@Param("userId") long userId);

    void updateUserRole(@Param("email") String email, @Param("role") Role role);
}
