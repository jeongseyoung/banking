import { useAuthStore } from '../store/UseAuthStore';
import { useState } from 'react';
import './css/BankingMain.css';
import HomeTab from './tabs/HomeTab';
import AccountsTab from './tabs/AccountsTab';
import HistoryTab from './tabs/HistoryTab';
import ProfileTab from './tabs/ProfileTab';
// //export default axiosInstance;   
function BankingMain() {
    const user = useAuthStore((state) => state.user);
    const logout = useAuthStore((state) => state.logout);
    const [activeTab, setActiveTab] = useState('home');

    return(
        <div className="banking-container">
            <div className="banking-main">
                {/* 상단 바 */}
                <header className="top-bar">
                    <h1>BANK</h1>
                    <div className="top-actions">
                        <button className="icon-btn">🔔</button>
                        <button className="icon-btn" onClick={logout}>⚙️</button>
                    </div>
                </header>
                
                {/* 탭 컨텐츠 - 훨씬 깔끔! */}
                <div className="tab-content">
                    {activeTab === 'home' && <HomeTab user={user} />}
                    {activeTab === 'accounts' && <AccountsTab />}
                    {activeTab === 'history' && <HistoryTab />}
                    {activeTab === 'profile' && <ProfileTab user={user} logout={logout} />}
                </div>
                
                {/* 하단 네비게이션 */}
                <nav className="bottom-nav">
                    <button 
                        className={`nav-item ${activeTab === 'home' ? 'active' : ''}`}
                        onClick={() => setActiveTab('home')}
                    >
                        <span className="nav-icon">🏠</span>
                        <span className="nav-label">홈</span>
                    </button>
                    <button 
                        className={`nav-item ${activeTab === 'accounts' ? 'active' : ''}`}
                        onClick={() => setActiveTab('accounts')}
                    >
                        <span className="nav-icon">💳</span>
                        <span className="nav-label">계좌</span>
                    </button>
                    <button 
                        className={`nav-item ${activeTab === 'history' ? 'active' : ''}`}
                        onClick={() => setActiveTab('history')}
                    >
                        <span className="nav-icon">📊</span>
                        <span className="nav-label">내역</span>
                    </button>
                    <button 
                        className={`nav-item ${activeTab === 'profile' ? 'active' : ''}`}
                        onClick={() => setActiveTab('profile')}
                    >
                        <span className="nav-icon">👤</span>
                        <span className="nav-label">MY</span>
                    </button>
                </nav>
            </div>
        </div>
    )
}

export default BankingMain;
