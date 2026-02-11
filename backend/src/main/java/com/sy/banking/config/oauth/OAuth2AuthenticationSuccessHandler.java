package com.sy.banking.config.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
//import org.springframework.web.util.UriComponentsBuilder;

import com.sy.banking.config.jwt.CustomUserDetailsService;
import com.sy.banking.config.jwt.JwtTokenProvider;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CustomUserDetailsService customUserDetailsService;

    //@Value("${app.oauth2.redirect-uri}")
    //private String redirectUri;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        //토큰생성
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        if(refreshToken == null) 
            throw new IllegalStateException("refresh token is null");

        //accesstoken, refreshtoken 쿠키에 저장
        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(false); // 로컬은 false, 운영은 true
        accessCookie.setPath("/"); // 모든 경로에 쿠키 전달
        accessCookie.setMaxAge(60 * 15); // 15분

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7일

        //refreshtoken redis(accesstoken만료시 refreshtoken 검증 후 accesstoken 재발급)
        redisTemplate.opsForValue().set("refresh:" + authentication.getName(), refreshToken, 7, TimeUnit.DAYS);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return "http://localhost:3000/main";
        
        // return UriComponentsBuilder.fromUriString("http://localhost:3000/main")
        //         .queryParam("accessToken", accessToken)
        //         .queryParam("refreshToken", refreshToken)
        //         .build()
        //         .toUriString();
    }

    // refresh token 검증 후 accesstoken, refreshtoken 각각 재발급
    public void refreshTokenValidation(String refreshToken, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        if(refreshToken == null) {
            throw new IllegalStateException("refreshtoken 없음");
        }
        
        if(!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalStateException("refreshtoken expire");
        }

        String name = jwtTokenProvider.getUsernameFromToken(refreshToken);
        String savedToken = (String) redisTemplate.opsForValue().get("refresh:" + name);

        if(savedToken == null || !savedToken.equals(refreshToken)) {
            throw new IllegalStateException("refreshtoken 검증 오류"); 
        }



        UserDetails userDetails = customUserDetailsService.loadUserByUsername(name);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(false); // 로컬은 false, 운영은 true
        accessCookie.setPath("/"); // 모든 경로에 쿠키 전달
        accessCookie.setMaxAge(60 * 15); // 15분

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7일

        if(refreshToken == null) {
            throw new IllegalStateException("refreshtokengeneration error");
        }

        redisTemplate.delete("refresh:" + authentication.getName());
        redisTemplate.opsForValue().set("refresh:" + authentication.getName(), refreshToken, 7, TimeUnit.DAYS);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }
}