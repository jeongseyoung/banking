import { useEffect, useState } from 'react';
import AccountItemResponse from '../../types/AccountListResponse';
import { apiGet } from '../../api/ApiExcute';
import { AccountDTO } from '../../types/dto/AccountDTO';
import TransferModal from './components/TransferModal';
import './css/AccountsTab.css' //E:\vscode\banking_project\frontend\src\pages\tabs\css\AccountsTab.css
function AccountsTab() {

    const [accountData, setAccountData] = useState<AccountItemResponse | null>(null); 
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    const [isTransferModalOpen, setIsTransferModalOpen] = useState(false);
    const [selectedAccount, setSelectedAccount] = useState<AccountDTO | null>(null);

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

    //이체버튼 클릭
    const handleTransferClick = (account: AccountDTO) => {
        setSelectedAccount(account);
        setIsTransferModalOpen(true);
    }

    //이체성공
    const handleTransferSuccess = () => {
        fetchAccounts();//계좌정보 새로고침
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

    if(!accountData || !accountData.list || accountData.list.length === 0) {
        return (
            <div className='accounts-tab'>
                <h2 className='tab-title'>계좌가 없습니다.</h2>
            </div>
        );
    }

    //const totalBalance = accountData?.totalBalance;

    return (
        <>
            <div className='accounts-tab'>
                <h2 className='tab-title'>총 {accountData?.totalCount}개의 계좌가 있습니다.</h2>

                <div className='account-grid'>
                    {accountData?.list.map((account: AccountDTO, index: number) => (
                        <div key={index} className='account-item'>
                            <div className='account-header'>
                                <span className='account-type'>입출금계좌</span>
                                {account.status === "ACTIVE" && (
                                    <span className='account-badge'>ACTIVE</span>
                                )}
                            </div>
                            <p className='account-num'>{account.accountNumber}</p>
                            <h3 className='account-money'>{account.balance}원</h3>
                            {/* 이체버튼 */}
                            <div className="account-actions-modern">
                                <button className="modern-btn transfer" onClick={() => handleTransferClick(account)}>
                                    <span className="btn-icon">→</span>
                                    <span>이체</span>
                                </button>
                                <button className="modern-btn detail">
                                    <span className="btn-icon">···</span>
                                    <span>상세</span>
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
            {/* 이체 모달 */}
            {selectedAccount && (
                <TransferModal 
                    isOpen={isTransferModalOpen}
                    onClose={() => setIsTransferModalOpen(false)}
                    fromAccount={selectedAccount}
                    onSuccess={handleTransferSuccess}
                />
            )}
        </>
    );
}

export default AccountsTab;