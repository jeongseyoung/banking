import { useState } from 'react';
import './css/LoginPage.css'

function LoginPage() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  //const navigate =  ;

  const handleGoogleLogin = () => {
    window.location.href = 'http://localhost:8098/oauth2/authorize/google';
  };

  const handleNaverLogin = () => {
    window.location.href = 'http://localhost:8098/oauth2/authorization/naver';
  };

  const handleKakaoLogin = () => {
    window.location.href = 'http://localhost:8098/oauth2/authorization/kakao';
  };

  const handleLogout = () => {
    fetch('http://localhost:8098/logout', { method: 'POST' })
      .then(() => window.location.href = '/');
  };

  return (
    <div className="login-page">
      <div className="login-container">
        {/* 상단 로고 */}
        <div className="login-header">
          <div className="bank-logo">
            <div className="logo-circle"></div>
            <h1>BANK</h1>
          </div>
          <h2>로그인</h2>
        </div>

        {/* 로그인 카드 */}
        <div className="login-card">
          {!isLoggedIn ? (
            <>
              {/* 간편로그인 안내 */}
              <div className="simple-login-notice">
                <div className="notice-icon">!</div>
                <p>간편로그인으로 빠르고 안전하게</p>
              </div>

              {/* 소셜 로그인 버튼들 */}
              <div className="social-buttons">
                <button className="social-btn google" onClick={handleGoogleLogin}>
                  <div className="btn-icon google-icon"></div>
                  <span>구글</span>
                </button>
                
                <button className="social-btn naver" onClick={handleNaverLogin}>
                  <div className="btn-icon naver-icon"></div>
                  <span>네이버</span>
                </button>
                
                <button className="social-btn kakao" onClick={handleKakaoLogin}>
                  <div className="btn-icon kakao-icon"></div>
                  <span>카카오</span>
                </button>
              </div>

              {/* 하단 텍스트 */}
              <div className="login-footer">
                <p>앱에서만 사용 가능합니다.</p>
                <p>비밀번호를 잊으셨나요? <a href="#">비밀번호 찾기</a></p>
              </div>
            </>
          ) : (
            <div className="dashboard">
              <h3>환영합니다!</h3>
              <button className="logout-btn" onClick={handleLogout}>로그아웃</button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default LoginPage;