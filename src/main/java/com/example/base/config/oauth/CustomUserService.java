package com.example.base.config.oauth;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.base.domain.item.UserItem;
import com.example.base.enumbox.Role;
import com.example.base.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserService extends DefaultOAuth2UserService{
    
    private final UserMapper userMapper;

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{

        //사용자 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //google or naver or kakao
        String provider = userRequest.getClientRegistration().getRegistrationId();

        //google 사용자 정보 가져오기
        Map<String, Object> userInfo = oAuth2User.getAttributes(); //

        //processOAuth2User -> registerOrUpdateUser -> 여기로 리턴
        UserItem user = processOAuth2User(provider, userInfo); 

        //구글에서 가져온 사용자정보 저장
        user.setAttributes(userInfo);
        
        return user;

    }
// UserItem {
//     firstName = "홍길동"
//     lastName = ""
//     userEmail = "hong@gmail.com"
//     user_Role = ROLE_USER
//     userPassword = "oauth2user"
//     attributes = {
//         "sub": "123456789012345678901",
//         "name": "홍길동",
//         "given_name": "길동",
//         "family_name": "홍",
//         "picture": "https://lh3.googleusercontent.com/a/...",
//         "email": "hong@gmail.com",
//         "email_verified": true,
//         "locale": "ko"
//     }  
// }  이렇게 출력됨.
    private UserItem processOAuth2User(String provider, Map<String, Object> userInfo) {

        String email = null;
        String name = null;
        String providerId = null;

        if ("google".equals(provider)) {
            email = (String) userInfo.get("email");
            name = (String) userInfo.get("name");
            providerId = (String) userInfo.get("sub"); //맞다면 google의 고유 id가 들어옴.            
        }
        else if ("naver".equals(provider)) {
            Map<String, Object> response = (Map<String, Object>) userInfo.get("response");
            
            email = (String) response.get("email");
            name = (String) response.get("name");
            providerId = (String) response.get("id");
        }

        return registerOrUpdateUser(email, name, provider, providerId);
    }
    //기존사용자를 업데이트, 없다면 등록(회원가입) -> user db에
    private UserItem registerOrUpdateUser(String email, String name, String provider, String provideId) {
        Optional<UserItem> existingUserOpt = userMapper.findByEmail(email);

        if (existingUserOpt.isPresent()) {
            return existingUserOpt.get();
        }

        UserItem newUser = new UserItem(name, "", email, Role.ROLE_USER, "1234");
        log.info("newUser {}", newUser);
        System.out.println("newUser: " + newUser);
        userMapper.addUser(newUser);

        //insert 후 반드시 다시 조회
        return userMapper.findByEmail(email).orElseThrow(() -> new IllegalStateException("OAuth user insert failed"));
    }
}
