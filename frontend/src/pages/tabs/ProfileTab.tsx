import { UserResponse } from '../../types/UserResponse';

interface ProfileTabProps {
    user: UserResponse | null;
    logout: () => Promise<void>;
}

function ProfileTab({ user, logout }: ProfileTabProps) {
    const handleLogout = async () => {
        await logout();
        window.location.href = '/';
    };

    return (
        <div className="profile-tab">
            {/* 프로필 헤더 */}
            <div className="profile-header">
                <div className="profile-avatar">
                    {user?.name?.charAt(0)}
                </div>
                <h2>{user?.name}</h2>
                <p>{user?.email}</p>
                <p className="user-role">{user?.userRole}</p>
            </div>
            
            {/* 내 정보 섹션 */}
            <div className="profile-section">
                <h3 className="section-title">내 정보</h3>
                <div className="info-grid">
                    <div className="info-item">
                        <span className="info-label">이름</span>
                        <span className="info-value">{user?.name}</span>
                    </div>
                    <div className="info-item">
                        <span className="info-label">이메일</span>
                        <span className="info-value">{user?.email}</span>
                    </div>
                    <div className="info-item">
                        <span className="info-label">가입일</span>
                        <span className="info-value">
                            {user?.createdAt ? new Date(user.createdAt).toLocaleDateString('ko-KR') : '-'}
                        </span>
                    </div>
                    <div className="info-item">
                        <span className="info-label">회원 등급</span>
                        <span className="info-value">{user?.userRole || 'MEMBER'}</span>
                    </div>
                </div>
            </div>
            
            {/* 설정 메뉴 */}
            <div className="profile-section">
                <h3 className="section-title">설정</h3>
                <div className="profile-menu">
                    <button className="profile-menu-item">
                        <div className="menu-left">
                            <span className="menu-icon">👤</span>
                            <span>개인정보 수정</span>
                        </div>
                        <span className="menu-arrow">›</span>
                    </button>
                    <button className="profile-menu-item">
                        <div className="menu-left">
                            <span className="menu-icon">🔒</span>
                            <span>보안 설정</span>
                        </div>
                        <span className="menu-arrow">›</span>
                    </button>
                    <button className="profile-menu-item">
                        <div className="menu-left">
                            <span className="menu-icon">🔔</span>
                            <span>알림 설정</span>
                        </div>
                        <span className="menu-arrow">›</span>
                    </button>
                    <button className="profile-menu-item">
                        <div className="menu-left">
                            <span className="menu-icon">💳</span>
                            <span>결제 수단 관리</span>
                        </div>
                        <span className="menu-arrow">›</span>
                    </button>
                </div>
            </div>
            
            {/* 고객센터 */}
            <div className="profile-section">
                <h3 className="section-title">고객센터</h3>
                <div className="profile-menu">
                    <button className="profile-menu-item">
                        <div className="menu-left">
                            <span className="menu-icon">❓</span>
                            <span>자주 묻는 질문</span>
                        </div>
                        <span className="menu-arrow">›</span>
                    </button>
                    <button className="profile-menu-item">
                        <div className="menu-left">
                            <span className="menu-icon">📞</span>
                            <span>고객센터</span>
                        </div>
                        <span className="menu-arrow">›</span>
                    </button>
                    <button className="profile-menu-item">
                        <div className="menu-left">
                            <span className="menu-icon">📋</span>
                            <span>약관 및 정책</span>
                        </div>
                        <span className="menu-arrow">›</span>
                    </button>
                </div>
            </div>
            
            {/* 로그아웃 버튼 */}
            <button className="logout-button" onClick={handleLogout}>
                로그아웃
            </button>
            
            {/* 앱 버전 */}
            <p className="app-version">버전 1.0.0</p>
        </div>
    );
}

export default ProfileTab;