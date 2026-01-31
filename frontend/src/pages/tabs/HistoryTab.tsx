import { useEffect, useState } from 'react';
import { apiGet } from '../../api/ApiExcute';
import TransactionPageParam from '../../types/params/TransactionPageParam';
import TransactionPageResponse from '../../types/TransactionPageResponse';
import TransactionDTO from '../../types/dto/TransactionDTO';
import TransferType from '../../types/type/TransferType';

function HistoryTab() {
    const [transactionData, setTransactionData] = useState<TransactionPageResponse | null>(null);
    const [allTransactions, setAllTransactions] = useState<TransactionDTO[]>([]);
    //const [transferType, setTransferType] = useState<string | null>("");
    const [currentPage, setCurrentPage] = useState<number>(1);
    const [loading, setLoading] = useState<boolean>(true);
    const [loadingMore, setLoadingMore] = useState<boolean>(false);  // 더보기 로딩
    const [error, setError] = useState<string | null>();
    const [filter, setFilter] = useState<TransferType>(null);
    
    useEffect(() => {
        fetchTransactions(1);
    }, []);

    useEffect(() => {
        setAllTransactions([]);
        setCurrentPage(1);
        fetchTransactions(1, true);
    }, [filter])

    const fetchTransactions = async (page: number, reset: boolean = false) => {
        const url = "/api/account/myaccount";
        const params: TransactionPageParam = {
            page: page,
            size: 5,
            transferType: filter || undefined
        }
        
        try {
            const data = await apiGet<TransactionPageResponse>(url, params);
            setTransactionData(data);
            
            if(reset) {
                setAllTransactions(data.list)
            } else {
                setAllTransactions(prev => [...prev, ...data.list])
            }
        } catch (error: any) {
            console.error("거래 내역 조회 실패:", error);
            setError(error.message || "거래 내역을 불러올 수 없습니다.");
        } finally {
            setLoading(false);
            setLoadingMore(false);
        }

    }
    
    const handleLoadMore = () => {
            const nextPage = currentPage + 1;
            setCurrentPage(nextPage);
            fetchTransactions(nextPage, false);
        }
        
        const groupByDate = (transactions: TransactionDTO[]) => {
            const groups: {[key: string] : TransactionDTO[]} = {};
            transactions.forEach(f => {
                const date = f.createdAt.split("T")[0];
                if(!groups[date]) {
                    groups[date] = []
                }
                groups[date].push(f);
            });
            return groups;
        }

        const formatDate = (d: string) => {
            const date = new Date(d);
            const today = new Date();
            const yesterday = new Date(today);
            yesterday.setDate(yesterday.getDate() - 1);

            if(date.toDateString() === today.toDateString()) return "오늘";
            if(date.toDateString() === yesterday.toDateString()) return "어제";

            const result = `${date.getMonth() + 1}월 ${date.getDay()}일`;
            
            return result;
        }

        const formatTime = (d: string) => {
            const date = new Date(d);
            const hours = String(date.getHours()).padStart(2, "0"); // 시간부분만 가져와서 한자리면 0채워서 리턴. 1 -> 01 // 12 -> 12
            const minutes = String(date.getMinutes()).padStart(2, "0");

            return `${hours}시 ${minutes}분`
        }

        const getTransactionType = (type: string): "in" | "out" | "interest"=> {
            if(type === "DEPOSIT" || type === "TRANS_IN") return "in";
            else if(type === "INTEREST") return "interest";
            
            return "out";
        };

        const getTransactionTypeKorean = (type: string) => {

            switch(type) {
                case "DEPOSIT": return "입금";
                case "WITHDRAWAL": return "출금";
                case "TRANSFER_IN": return "받은송금";
                case "TRANSFER_OUT": return "보낸송금";
                case "INTEREST": return "이자";
                default: return type;
            }
        };

        if(loading) {

            return (
                <div className='history-tab'>
                    <p>loading...</p>
                </div>
            )
        }

        if(error) {

            return (
                <div className='history-tab'>
                    <p>오류</p>
                    <button onClick={() => fetchTransactions(1, true)}>다시 시도</button>
                </div>
            )
        }

    const transactionGroups = groupByDate(allTransactions);
    const more = transactionData && currentPage < transactionData.totalPage;
    

    return (
        <div className="history-tab">
            <h2 className="tab-title">거래내역</h2>
            
            {/* 이번 달 지출 요약 */}
            {transactionData && (
                <div className='account-info-card'>
                    <p className='account-label'>계좌번호</p>
                    <p className='account-number'>{transactionData.accountNumber}</p>
                    <p className='account-status'>{transactionData.status}</p>
                </div>
            )}
            
            {/* 필터 버튼 */}
            <div className="history-filters">
                <button 
                    className={`filter-btn ${filter === null ? 'active' : ''}`}
                    onClick={() => setFilter(null)}
                >
                    전체
                </button>
                <button 
                    className={`filter-btn ${filter === 'DEPOSIT' ? 'active' : ''}`}
                    onClick={() => setFilter('DEPOSIT')}>
                    입금
                </button>
                <button 
                    className={`filter-btn ${filter === 'WITHDRAWAL' ? 'active' : ''}`}
                    onClick={() => setFilter('WITHDRAWAL')}>
                    출금
                </button>
                <button 
                    className={`filter-btn ${filter === 'TRANSFER_IN' ? 'active' : ''}`}
                    onClick={() => setFilter('TRANSFER_IN')}>
                    받은송금
                </button>
                <button 
                    className={`filter-btn ${filter === 'TRANSFER_OUT' ? 'active' : ''}`}
                    onClick={() => setFilter('TRANSFER_OUT')}>
                    보낸송금
                </button>
                <button 
                    className={`filter-btn ${filter === 'INTEREST' ? 'active' : ''}`}
                    onClick={() => setFilter('INTEREST')}>
                    이자
                </button>
            </div>
            
            {/* 거래 내역 리스트 */}
            {allTransactions.length === 0 ? (
                <div className='empty-state'>
                    <p>거래 내역이 없습니다.</p>
                </div>
            ) : (
                <>
                    <div className='history-list'>
                        {Object.keys(transactionGroups).map(date => (
                            <div key={date}>
                                <div className='history-date'>{formatDate(date)}</div>
                                {transactionGroups[date].map((transaction, index) =>{
                                    const transactionType = getTransactionType(transaction.type);
                                    return (
                                        <div key={`${transaction.accountId}-${index}`} className='history-item'>
                                            <div className='history-left'>
                                                <div className={`history-icon ${transactionType}`}>
                                                    {transactionType === "in" || "interest" ? "입" : "출"}
                                                </div>
                                                <div>
                                                    <p className='history-title'>
                                                        {transaction.memo || getTransactionTypeKorean(transaction.type)}
                                                    </p>
                                                    <p className="history-time">
                                                        {formatTime(transaction.createdAt)} · {getTransactionTypeKorean(transaction.type)}
                                                    </p>
                                                </div>
                                            </div>
                                            <div className='history-right'>
                                                <p className={`history-amount ${transactionType}`}>
                                                    {transactionType === "in" || "interest" ? "+" : "-"}
                                                    {transaction.amount.toLocaleString()}원 
                                                </p>
                                                <p className='histroy-balance'>
                                                    잔액 {transaction.balanceAfter.toLocaleString()}원
                                                </p>
                                            </div>
                                        </div>
                                    )
                                })}
                            </div>
                        ))}
                    </div>
                    
                    {/*더보기버튼 */}
                    {more && (
                        <div className='load-more-container'>
                            <button className='load-more-btn' onClick={handleLoadMore} disabled={loadingMore}>
                                {loadingMore ? (
                                    <span>loading.......</span>
                                ) : (
                                    <>
                                        <span>더보기</span>
                                        <span className='laod-more-icon'>↓</span>
                                    </>
                                )}
                            </button>
                            <p className='load-more-info'>
                                {currentPage} / {transactionData.totalPage} 페이지
                            </p>
                        </div>
                    )}
                    {!more && allTransactions.length > 0 && (
                        <div className='end-message'>
                            <p>모든 거래 내역을 확인했습니다.</p>
                        </div>
                    )}
                </>
            )} 
        </div>
    );
}

export default HistoryTab;