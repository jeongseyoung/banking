package com.sy.banking.config.redis;

import org.springframework.stereotype.Component;

@Component
public class RedisKeyConstants {

    public static final String REFRESH_TOKEN = "auth:refresh:";
    public static final String BLACKLIST = "auth:blacklist:";
    public static final String LOGIN_ATTEMPTS = "auth:login:attemps:";

    public static String REFRESH_KEY(String username) {
        return REFRESH_TOKEN + username;
    }

    public static String BLACKLIST_KEY(String token) {
        return BLACKLIST + token;
    }
    
}
