import { useState } from 'react';

interface Transaction {
    id: number;
    type: 'in' | 'out';
    title: string;
    amount: number;
    date: string;
    time: string;
    category: string;
    balance: number;
}

function HistoryTab() {
    const [filter, setFilter] = useState<'all' | 'in' | 'out'>('all');
    const [transactions] = useState<Transaction[]>([
        {
            id: 1,
            type: 'out',
            title: '쿠팡',
            amount: 15000,
            date: '2026-01-22',
            time: '14:23',
            category: '쇼핑',
            balance: 835000
        },
        {
            id: 2,
            type: 'out',
            title: '스타벅스 강남점',
            amount: 4500,
            date: '2026-01-22',
            time: '09:15',
            category: '카페',
            balance: 850000
        },
        {
            id: 3,
            type: 'in',
            title: '급여',
            amount: 3000000,
            date: '2026-01-21',
            time: '09:00',
            category: '월급',
            balance: 854500
        },
        {
            id: 4,
            type: 'out',
            title: 'GS25 편의점',
            amount: 8500,
            date: '2026-01-20',
            time: '22:30',
            category: '편의점',
            balance: -2145500
        },
        {
            id: 5,
            type: 'out',
            title: '넷플릭스',
            amount: 13500,
            date: '2026-01-20',
            time: '15:00',
            category: '구독',
            balance: -2137000
        },
        {
            id: 6,
            type: 'in',
            title: '김철수',
            amount: 50000,
            date: '2026-01-19',
            time: '18:45',
            category: '송금',
            balance: -2123500
        }
    ]);

    const filteredTransactions = transactions.filter(t => {
        if (filter === 'all') return true;
        return t.type === filter;
    });

    const groupByDate = (transactions: Transaction[]) => {
        const groups: { [key: string]: Transaction[] } = {};
        transactions.forEach(t => {
            const date = t.date;
            if (!groups[date]) groups[date] = [];
            groups[date].push(t);
        });
        return groups;
    };

    const groupedTransactions = groupByDate(filteredTransactions);

    const formatDate = (dateStr: string) => {
        const date = new Date(dateStr);
        const today = new Date();
        const yesterday = new Date(today);
        yesterday.setDate(yesterday.getDate() - 1);

        if (date.toDateString() === today.toDateString()) return '오늘';
        if (date.toDateString() === yesterday.toDateString()) return '어제';
        
        return `${date.getMonth() + 1}월 ${date.getDate()}일`;
    };

    return (
        <div className="history-tab">
            <h2 className="tab-title">거래내역</h2>
            
            {/* 이번 달 지출 요약 */}
            <div className="spending-summary">
                <div className="spending-item">
                    <p className="spending-label">이번 달 수입</p>
                    <h3 className="spending-amount in">+3,050,000원</h3>
                </div>
                <div className="spending-divider"></div>
                <div className="spending-item">
                    <p className="spending-label">이번 달 지출</p>
                    <h3 className="spending-amount out">-41,500원</h3>
                </div>
            </div>
            
            {/* 필터 버튼 */}
            <div className="history-filters">
                <button 
                    className={`filter-btn ${filter === 'all' ? 'active' : ''}`}
                    onClick={() => setFilter('all')}
                >
                    전체
                </button>
                <button 
                    className={`filter-btn ${filter === 'in' ? 'active' : ''}`}
                    onClick={() => setFilter('in')}
                >
                    입금
                </button>
                <button 
                    className={`filter-btn ${filter === 'out' ? 'active' : ''}`}
                    onClick={() => setFilter('out')}
                >
                    출금
                </button>
            </div>
            
            {/* 거래 내역 리스트 */}
            <div className="history-list">
                {Object.keys(groupedTransactions).map(date => (
                    <div key={date}>
                        <div className="history-date">{formatDate(date)}</div>
                        {groupedTransactions[date].map(transaction => (
                            <div key={transaction.id} className="history-item">
                                <div className="history-left">
                                    <div className={`history-icon ${transaction.type}`}>
                                        {transaction.type === 'in' ? '입' : '출'}
                                    </div>
                                    <div>
                                        <p className="history-title">{transaction.title}</p>
                                        <p className="history-time">
                                            {transaction.time} · {transaction.category}
                                        </p>
                                    </div>
                                </div>
                                <div className="history-right">
                                    <p className={`history-amount ${transaction.type === 'in' ? 'in' : 'out'}`}>
                                        {transaction.type === 'in' ? '+' : '-'}
                                        {transaction.amount.toLocaleString()}원
                                    </p>
                                    <p className="history-balance">
                                        잔액 {transaction.balance.toLocaleString()}원
                                    </p>
                                </div>
                            </div>
                        ))}
                    </div>
                ))}
            </div>
        </div>
    );
}

export default HistoryTab;