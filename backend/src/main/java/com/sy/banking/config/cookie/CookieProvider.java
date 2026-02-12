package com.sy.banking.config.cookie;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieProvider {

    //쿠키등록
    public void setCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        response.addCookie(createCookies("accessToken", accessToken, 60 * 15)); //15분
        response.addCookie(createCookies("refreshToken", refreshToken, 60 * 60 * 24 * 7)); //7일

    }
    //쿠키생성
    public Cookie createCookies(String name, String token, int maxAge) {
        Cookie cookie = new Cookie(name, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // 로컬은 false, 운영은 true
        cookie.setPath("/"); // 모든 경로에 쿠키 전달
        cookie.setMaxAge(maxAge); //60 * 15 = 15분 // 60 * 60 * 24 * 7 = 7일
        
        return cookie;
    }

    //쿠키추출
    public String extractCookies(HttpServletRequest request, String name) {
        
        if(request.getCookies() == null) 
            return null;

        for(Cookie c : request.getCookies()) {
            if(c.getName().equals(name))
                return c.getValue();
        }

        return null;
    }


    //쿠키삭제
    public void deleteCookies(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
    }
}
