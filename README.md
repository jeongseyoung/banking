유니크키 설정 - 중복방지를 db에서 직접
ALTER TABLE user ADD CONSTRAINT unique_key_userId UNIQUE (userId);
ALTER TABLE user ADD CONSTRAINT unique_key_userEmail UNIQUE (userEmail);

JWT 토큰을 받을 최종 페이지
app:
  oauth2:
    redirect-uri: http://localhost:8098/oauth2/redirect
