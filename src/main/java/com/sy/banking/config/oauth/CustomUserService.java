// package com.sy.banking.config.oauth;

// import java.util.Map;
// import java.util.Optional;

// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
// import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
// import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
// import org.springframework.security.oauth2.core.user.OAuth2User;
// import org.springframework.stereotype.Service;

// import com.sy.banking.auth.mapper.UserMapper;
// import com.sy.banking.domain.dto.UserDto;
// import com.sy.banking.domain.item.UserItem;
// import com.sy.banking.enumbox.Role;

// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
// @Service
// @RequiredArgsConstructor
// public class CustomUserService extends DefaultOAuth2UserService{
    
//     private final UserMapper userMapper;

//     public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{

//         //사용자 정보 가져오기
//         OAuth2User oAuth2User = super.loadUser(userRequest);

//         //google or naver or kakao
//         String provider = userRequest.getClientRegistration().getRegistrationId();

//         //google 사용자 정보 가져오기
//         Map<String, Object> userInfo = oAuth2User.getAttributes(); //

//         //processOAuth2User -> registerOrUpdateUser -> 여기로 리턴
//         //UserItem user = processOAuth2User(provider, userInfo); 
//         System.out.println("userInfo: " + userInfo);
//         //구글에서 가져온 사용자정보 저장
//         //user.setAttributes(userInfo);
        
//         UserItem userItem;
//         String email = (String) userInfo.get("email");

//         Optional<UserDto> userDto = userMapper.findByUserDtoEmail(email);
//         if(userDto.ifPresent(null);) {
//             userItem = new UserItem(userDto.get());
//         } else {
//             userItem = new UserItem(email, "oauth2_google", (String) userInfo.get("name"), Role.ROLE_USER);
//             userMapper.addUser(userItem);
//         }
        
//         userItem.setAttributes(userInfo);
//         log.info("OAuth2User 생성 완료??: email={}, role={}", email, userItem.getUser_Role());
        
//         return userItem;

//     }
// // UserItem {
// //     firstName = "홍길동"
// //     lastName = ""
// //     email = "hong@gmail.com"
// //     user_Role = ROLE_USER
// //     userPassword = "oauth2user"
// //     attributes = {
// //         "sub": "123456789012345678901",
// //         "name": "홍길동",
// //         "given_name": "길동",
// //         "family_name": "홍",
// //         "picture": "https://lh3.googleusercontent.com/a/...",
// //         "email": "hong@gmail.com",
// //         "email_verified": true,
// //         "locale": "ko"
// //     }  
// // }  이렇게 출력됨.
//     private UserItem processOAuth2User(String provider, Map<String, Object> userInfo) {

//         String email = null;
//         String name = null;
//         String providerId = null;

//         if ("google".equals(provider)) {
//             email = (String) userInfo.get("email");
//             name = (String) userInfo.get("name");
//             providerId = (String) userInfo.get("sub"); //맞다면 google의 고유 id가 들어옴.            
//         }
//         else if ("naver".equals(provider)) {
//             Map<String, Object> response = (Map<String, Object>) userInfo.get("response");
            
//             email = (String) response.get("email");
//             name = (String) response.get("name");
//             providerId = (String) response.get("id");
//         }

//         return registerOrUpdateUser(email, name, provider, providerId);
//     }
//     //기존사용자를 업데이트, 없다면 등록(회원가입) -> user db에
//     private UserItem registerOrUpdateUser(String email, String name, String provider, String provideId) {
//         Optional<UserItem> existingUserOpt = userMapper.findByEmail(email);

//         if (existingUserOpt.isPresent()) {
//             return existingUserOpt.get();
//         }

//         UserItem newUser = new UserItem(email, "oauth2_google", name, Role.ROLE_USER);
//         System.out.println("newUser: " + newUser);
//         userMapper.addUser(newUser);

//         //insert 후 반드시 다시 조회
//         return userMapper.findByEmail(email).orElseThrow(() -> new IllegalStateException(provider + " 로그인 실패"));
//     }
// }
package com.sy.banking.config.oauth;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.sy.banking.auth.mapper.UserMapper;
import com.sy.banking.domain.dto.UserDto;
import com.sy.banking.domain.item.UserItem;
import com.sy.banking.enumbox.Role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserService extends DefaultOAuth2UserService {
    
    private final UserMapper userMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        
        // 1. OAuth2 사용자 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        // 2. 제공자 확인 (google, naver, kakao 등)
        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> userInfo = oAuth2User.getAttributes();
        
        log.info("OAuth2 로그인 시도: provider={}, email={}", 
                 provider, userInfo.get("email"));
        
        // 3. 사용자 처리 (조회 또는 생성)
        UserItem userItem = processOAuth2User(provider, userInfo);
        
        // 4. OAuth2 속성 정보 설정
        userItem.setAttributes(userInfo);
        
        log.info("OAuth2 사용자 인증 완료: email={}, role={}", 
                 userItem.getEmail(), userItem.getUser_Role());
        
        return userItem;
    }

    
    //OAuth2 사용자 정보 처리 (조회 또는 신규 등록)
    private UserItem processOAuth2User(String provider, Map<String, Object> userInfo) {
        
        // 제공자별 사용자 정보 추출
        String email = extractEmail(provider, userInfo);
        String name = extractName(provider, userInfo);
        
        if (email == null) {
            throw new OAuth2AuthenticationException(
                "이메일 정보를 가져올 수 없습니다. provider: " + provider
            );
        }
        
        // DB에서 사용자 조회
        Optional<UserDto> existingUser = userMapper.findByUserDtoEmail(email);
        
        if (existingUser.isPresent()) {
            // 기존 사용자 반환
            log.debug("기존 사용자 로그인: {}", email);
            UserItem userItem = new UserItem(existingUser.get());
            
            // ⭐ 중요: Role이 null인 경우 기본값 설정
            if (userItem.getUser_Role() == null) {
                log.warn("사용자의 역할이 null입니다. 기본 역할 설정: {}", email);
                userItem.setUser_Role(Role.ROLE_USER);
                // DB 업데이트
                userMapper.updateUserRole(email, Role.ROLE_USER);
            }
            
            return userItem;
        } else {
            // 신규 사용자 등록
            log.info("신규 사용자 등록: {}", email);
            return registerNewUser(email, name, provider);
        }
    }

  
    //신규 사용자 등록     
    private UserItem registerNewUser(String email, String name, String provider) {
        UserItem newUser = new UserItem(email, "oauth2_" + provider, name, Role.ROLE_USER);
        
        try {
            userMapper.addUser(newUser);
            log.info("신규 사용자 등록 완료: email={}, provider={}", email, provider);
            
            // INSERT 후 userId를 포함한 완전한 정보를 다시 조회
            Optional<UserDto> userDto = userMapper.findByUserDtoEmail(email);
            if (userDto.isPresent()) {
                return new UserItem(userDto.get());
            } else {
                throw new IllegalStateException("사용자 등록 후 조회 실패: " + email);
            }
            
        } catch (Exception e) {
            log.error("사용자 등록 실패: email={}, error={}", email, e.getMessage());
            throw new OAuth2AuthenticationException("사용자 등록에 실패했습니다.");
        }
    }

    
    //제공자별 이메일 추출    
    private String extractEmail(String provider, Map<String, Object> userInfo) {
        if ("google".equals(provider)) {
            return (String) userInfo.get("email");
        } else if ("naver".equals(provider)) {
            // Map<String, Object> response = (Map<String, Object>) userInfo.get("response");
            // return response != null ? (String) response.get("email") : null;
        } else if ("kakao".equals(provider)) {
            // Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
            // return kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
        }
        return null;
    }

    
    //제공자별 이름 추출
    private String extractName(String provider, Map<String, Object> userInfo) {
        if ("google".equals(provider)) {
            return (String) userInfo.get("name");
        } else if ("naver".equals(provider)) {
            // Map<String, Object> response = (Map<String, Object>) userInfo.get("response");
            // return response != null ? (String) response.get("name") : "사용자";
        } else if ("kakao".equals(provider)) {
            // Map<String, Object> properties = (Map<String, Object>) userInfo.get("properties");
            // return properties != null ? (String) properties.get("nickname") : "사용자";
        }
        return "사용자";
    }
}
