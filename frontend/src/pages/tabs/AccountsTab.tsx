import { useEffect, useState } from 'react';
import AccountItemResponse from '../../types/AccountListResponse';
import { apiGet } from '../../api/ApiExcute';
import { AccountDTO } from '../../types/dto/AccountDTO';

function AccountsTab() {

    const [accountData, setAccountData] = useState<AccountItemResponse | null>(null); 
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        fetchAccounts();
    }, []);
    
    const fetchAccounts = async () => {

        try {
            //export const apiGet = <TResponse = any> (url: string, params: Record<string,any>): Promise<TResponse> => api<TResponse>(url, "GET", {params});
            const url = "/api/account/accounttab";
            const data = await apiGet<AccountItemResponse>(url);
            setAccountData(data);
            console.log("totalbalance", data.totalBalance)
        } catch (error: any) {
            console.error("계좌 조회 실패", error);
            setError(error.message || "계죄 조회 실패");
        } finally {
            setLoading(false);
        }
    }

    if (loading) {
        return (
            <div className='accounts-tab'>
                <p>loading......</p>
            </div>
        )
    }

    if (error) {
        return (
            <div className='accounts-tab'>
                <p style={{ color: 'red' }}>{error}</p>
                <p style={{ color: 'blue' }}>다시 로그인 해주세요.</p>
                <button onClick={fetchAccounts}>다시 시도</button>
            </div>
        );
    }

    //const totalBalance = accountData?.totalBalance;

    return (
        <div className='accounts-tab'>
            <h2 className='tab-title'>총 {accountData?.totalCount}개의 계좌가 있습니다.</h2>

            <div className='account-grid'>
                {accountData?.list.map((accountDto: AccountDTO, index: number) => (
                    <div key={index} className='account-item'>
                        <div className='account-header'>
                            <span className='account-type'>입출금계좌</span>
                            {accountDto.status === "ACTIVE" && (
                                <span className='account-badge'>ACTIVE</span>
                            )}
                        </div>
                        <p className='account-num'>{accountDto.accountNumber}</p>
                        <h3 className='account-money'>{accountDto.balance}원</h3>
                        <div className="account-actions-floating">
                            <button className="floating-btn transfer">
                                <span className="floating-icon">→</span>
                                <span className="floating-text">이체</span>
                            </button>
                            <button className="floating-btn detail">
                                <span className="floating-icon">···</span>
                                <span className="floating-text">상세</span>
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
        
    );
}

export default AccountsTab;