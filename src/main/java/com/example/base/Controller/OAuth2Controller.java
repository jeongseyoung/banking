package com.example.base.Controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class OAuth2Controller {
    

    //http://localhost:8098/login/oauth2/code/google
    @GetMapping("/main")
    public String redirect() {
        return new String("음?");
    }

    @GetMapping("/login_failed")
    public String redirect_login_failed() {
        return new String("음?");
    }
    

}
