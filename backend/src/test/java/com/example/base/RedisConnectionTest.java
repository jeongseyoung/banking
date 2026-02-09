package com.example.base;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import com.sy.banking.BankingApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = BankingApplication.class)
@Slf4j
public class RedisConnectionTest {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void redisConnectionTest() {
        redisTemplate.opsForValue().set("keytest", "Hello");

        String value = (String) redisTemplate.opsForValue().get("keytest");

        assertEquals("Hello", value);

        log.info("성공 {}", value);

        redisTemplate.delete("keytest");
    }

    @Test
    void operationTest() {
        
        //문자
        redisTemplate.opsForValue().set("string_test", "test");
        assertEquals("test", redisTemplate.opsForValue().get("string_test"));

        //숫자증가
        redisTemplate.opsForValue().set("cnt", 0);
        redisTemplate.opsForValue().increment("cnt");
        assertEquals(1, redisTemplate.opsForValue().get("cnt"));

        //hash
        redisTemplate.opsForHash().put("user1", "name", "sy");
        redisTemplate.opsForHash().put("user1", "email", "@@@");
        assertEquals("sy", redisTemplate.opsForHash().get("user1", "name"));
        assertEquals("@@@", redisTemplate.opsForHash().get("user1", "email"));

        //list
        redisTemplate.opsForList().rightPush("list_1", "1");
        redisTemplate.opsForList().rightPush("list_1", "2");
        assertEquals(2, redisTemplate.opsForList().size("list_1"));
        log.info("list size: {}개", redisTemplate.opsForList().size("list_1"));


        redisTemplate.delete("string_test");
        redisTemplate.delete("cnt");
        redisTemplate.delete("user1");
        redisTemplate.delete("list_1");

        log.info("완료");
    }
}
