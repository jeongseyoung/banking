import { useAuthStore } from '../../store/UseAuthStore';
import { UserResponse } from '../../types/UserResponse';

interface HomeTabProps {
    user: UserResponse | null;
}

function HomeTab({ user }: HomeTabProps) {
    return (
        <div className="home-tab">
            {/* 환영 배너 */}
            <div className="welcome-banner">
                <h2>{user?.name}님</h2>
                <p>{user?.email}</p>
            </div>
            
            {/* 총 자산 */}
            <div className="balance-summary">
                <div className="balance-item">
                    <p className="balance-label">총 자산</p>
                    <h2 className="balance-amount">1,234,567원</h2>
                </div>
            </div>  
            
            {/* 빠른 액션 버튼 */}
            <div className="action-buttons">
                <button className="action-btn primary">송금하기</button>
                <button className="action-btn secondary">충전하기</button>
            </div>
            
            {/* 최근 거래 미리보기 */}
            <div className="recent-preview">
                <div className="preview-header">
                    <h3>최근 거래</h3>
                    <button className="text-btn">전체보기</button>
                </div>
                <div className="preview-list">
                    <div className="preview-item">
                        <div className="preview-left">
                            <div className="preview-icon">🛒</div>
                            <div>
                                <p className="preview-title">쿠팡</p>
                                <p className="preview-time">오늘 14:23</p>
                            </div>
                        </div>
                        <p className="preview-amount out">-15,000원</p>
                    </div>
                    <div className="preview-item">
                        <div className="preview-left">
                            <div className="preview-icon">☕</div>
                            <div>
                                <p className="preview-title">스타벅스</p>
                                <p className="preview-time">오늘 09:15</p>
                            </div>
                        </div>
                        <p className="preview-amount out">-4,500원</p>
                    </div>
                    <div className="preview-item">
                        <div className="preview-left">
                            <div className="preview-icon">💵</div>
                            <div>
                                <p className="preview-title">급여</p>
                                <p className="preview-time">어제</p>
                            </div>
                        </div>
                        <p className="preview-amount in">+3,000,000원</p>
                    </div>
                </div>
            </div>
            
            {/* 혜택 배너 */}
            <div className="benefit-banner">
                <div className="benefit-icon">🎁</div>
                <div className="benefit-text">
                    <h4>친구 초대하고 5,000원 받기</h4>
                    <p>초대 코드를 공유해보세요</p>
                </div>
                <button className="benefit-btn">›</button>
            </div>
        </div>
    );
}

export default HomeTab;