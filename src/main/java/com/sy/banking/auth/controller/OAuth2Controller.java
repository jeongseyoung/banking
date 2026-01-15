package com.sy.banking.auth.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class OAuth2Controller {
    
    //localhost:8098/oauth2/authorize/google
    //localhost:8098/oauth2/authorize/google
    @GetMapping("/main")
    public String redirect() {
        return new String("음?");
    }

    @GetMapping("/login_failed")
    public String redirect_login_failed() {
        return new String("음?");
    }
    

}
