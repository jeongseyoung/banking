package com.example.base.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.base.domain.dto.UserDto;
import com.example.base.domain.item.UserItem;

@Mapper
public interface UserMapper {
    Optional<UserDto> getUser(@Param("userId") long userId);

    int addUser(@Param("user") UserItem userItem);

    int countByuserId(@Param("userId") long userId);
    
    int countByuserEmail(@Param("userEmail") String userEmail);

    Optional<UserItem> findByEmail(@Param("userEmail") String userEmail);

}
