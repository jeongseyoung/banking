import { useState } from "react";
import { AccountDTO } from "../../../types/dto/AccountDTO";
import TransferRequest from "../../../types/req/TransferRequest";
import { apiPost } from "../../../api/ApiExcute";
import TransferResponse from "../../../types/res/TransferResponse";

interface TransferModalProps {
    isOpen: boolean,
    onClose: () => void,
    fromAccount: AccountDTO,
    onSuccess: () => void;
}

function TransferModal({ isOpen, onClose, fromAccount, onSuccess} : TransferModalProps) {
    const [toAccountNumber, setToAccountNumber] = useState("");
    const [amount, setAmount] = useState("");
    const [memo, setMemo] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [step, setStep] = useState<"input" | "confirm" | "complete">("input");
    const [result, setResult] = useState<TransferResponse  | null>(null);
    const [transferType, setTransferType] = useState<"DEPOSIT" | "WITHDRAWAL" | "TRANSFER_OUT" | "TRANSFER_IN" | null>(null);


    if(!isOpen) return null;

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);

        //유효성검사
        if(!toAccountNumber.trim()) {
            setError("계좌번호를 입력하세요");
            return;
        }

        if(!amount || (Number(amount) <= 0)) {
            setError("금액을 입력해주세요");
            return;
        }

        if(Number(amount) > fromAccount.balance) {
            setError("잔액부족");
            return
        }

        setStep("confirm");
    }

    const handleConfirm = async () => {
        setLoading(true);
        setError(null);

        try {

            const transferData: TransferRequest = {
                fromAccountId: fromAccount.accountId,
                toAccountNumber: toAccountNumber,
                amount: Number(amount),
                memo: memo || undefined,
                transferType: transferType
            }
            const url = ""
            const response = await apiPost<TransferResponse>(url, transferData);

            setResult(response);
            setStep("complete");

            setTimeout(() => {
                onSuccess();
                handleClose();
            }, 3000);//3초 후 닫기, 새로고침

        } catch (error: any) {   
            console.error("이체실패", error);
            setError(error.response?.data?.message || "이체실패");
            setStep("input");
        } finally {
            setLoading(false);
        }
    }

    const handleClose = () => {
        setToAccountNumber("");
        setAmount("");
        setMemo("");
        setError("");
        setStep("input");
        setResult(null);
        onClose();
    }

    const handleBack = () => {
        setStep("input");
        setError(null);
    }

    const formatAccountNumber = (num: string) => {
        return num.replace(/(\d{5})(\d{2})(\d{8})/, '$1-$2-$3');
    };

    return (
        <div className="modal-overlay" onClick={handleClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                {/* 입력화면 */}
                {step === "input" && (
                    <>
                        <div className="modal-header">
                            <h2>계좌이체</h2>
                            <button className="modal-close" onClick={handleClose}>X</button>
                        </div>

                        <form onSubmit={handleSubmit}>
                            <div className="modal-body">
                                {/* 출금계좌 */}
                                <div className="transfer-section">
                                    <label className="transfer-label">출금계좌</label>
                                    <div className="account-info-box">
                                        <p className="account-num-text">{fromAccount.accountNumber}</p>
                                        <p className="account-balance-text">잔액: {fromAccount.balance.toLocaleString()}원</p>
                                    </div>
                                </div>
                                {/* 받는계좌 */}
                                <div className="transfer-section">
                                    <label className="transfer-label">받는 계좌번호</label>
                                    <input 
                                        type="text" 
                                        className="transfer-input"
                                        placeholder="12345-67-89012345"
                                        value={toAccountNumber}
                                        onChange={(e) => setToAccountNumber(e.target.value)}
                                        maxLength={17}
                                    />
                                </div>
                                {/* 이체 금액 */}
                                <div className="transfer-section">
                                    <label className="transfer-label">이체금액</label>
                                    <div className="amount-input-wrapper">
                                        <input 
                                            type="number"
                                            className="transfer-input"
                                            placeholder="0" 
                                            value={amount}
                                            onChange={(e) => setAmount(e.target.value)}
                                            min={0}
                                            max={fromAccount.balance}
                                        />
                                        <span className="currency">원</span>
                                    </div>

                                    <div className="quick-amounts">
                                        <button type="button" onClick={() => setAmount("10000")}>+1만</button>
                                        <button type="button" onClick={() => setAmount("50000")}>+5만</button>
                                        <button type="button" onClick={() => setAmount("100000")}>+10만</button>
                                        <button type="button" onClick={() => setAmount(String(fromAccount.balance))}>전액</button>
                                    </div>
                                </div>

                                {/* 메모 */}
                                <div className="transfer-section">
                                    <label className="transfer-label">메모(option)</label>
                                    <input 
                                        type="text" 
                                        className="transfer-input"
                                        placeholder="메모를 입력하세요. 50자 이내"
                                        value={memo}
                                        onChange={(e) => setMemo(e.target.value)}
                                        maxLength={50}/>
                                </div>
                                {error && (
                                    <div className="error-message">
                                        {error}
                                    </div>
                                )}
                            </div>
                            <div className="modal-footer">
                                <button type="button" className="modal-btn cancel" onClick={handleClose}>
                                    취소
                                </button>
                                <button type="submit" className="modal-btn confirm">
                                    다음
                                </button>
                            </div>
                        </form>
                    </>
                )}

                {/* 확인화면 */}
                {step === "confirm" && ( 
                    <>
                        <div className="modal-header">
                            <h2>이체 확인</h2>
                            <button className="modal-close" onClick={handleClose}>X</button>
                        </div>
                        <div className="modal-body">
                            <div className="confirm-section">
                                <div className="confirm-item">
                                    <span className="confirm-label">출금계좌</span>
                                    <span className="confirm-value">{fromAccount.accountNumber}</span>
                                </div>
                                <div className="confirm-item">
                                    <span className="confirm-label">받는계좌</span>
                                    <span className="confirm-value">{toAccountNumber}</span>
                                </div>
                                <div className="confirm-item highlight">
                                    <span className="confirm-label">이체금액</span>
                                    <span className="confirm-value amount">{Number(amount).toLocaleString()}원</span>
                                </div>
                                {memo && (
                                    <div className="confirm-item">
                                        <span className="confirm-label">메모</span>
                                        <span className="confirm-value">{memo}</span>
                                    </div>
                                )}
                                <div className="confirm-item">
                                    <span className="confirm-label">이체 후 잔액</span>
                                    <span className="confirm-value">{(fromAccount.balance - Number(amount)).toLocaleString()}원</span>
                                </div>
                            </div>
                            {error && (
                                <div className="error-message">
                                    {error}
                                </div>
                            )}
                        </div>
                        <div className="modal-footer">
                            <button className="modal-btn cancel" onClick={handleBack}>이전</button>
                            <button className="modal-btn confirm" onClick={handleConfirm} disabled={loading}>
                                {loading ? "처리중.." : "이체하기"}
                            </button>
                        </div>
                    </>
                )}
                {/* 완료화면 */}
                {step === "complete" && result && (
                    <>
                        <div className="modal-header">
                            <h2>이체완료</h2>
                        </div>
                        <div className="modal-body">
                            <div className="success-icon">✓</div>
                            <h3 className="success-title">이체가 완료되었습니다.</h3>
                            <div className="result-section">
                                <div className="result-item">
                                    <span className="result-label">이체금액</span>
                                    <span className="result-value">{result.amount.toLocaleString()}원</span>
                                </div>
                                <div className="result-item">
                                    <span className="result-label">이체 후 잔액</span>
                                    <span className="result-value">
                                        {result.balanceAfter.toLocaleString()}원
                                    </span>
                                </div>
                                <div className="result-item">
                                    <span className="result-label">거래번호</span>
                                    <span className="result-value">#{result.transactionId}</span>
                                </div>
                            </div>

                            <p className="auto-close-message">3초 후 자동으로 닫힙니다...</p>
                        </div>

                        <div className="modal-footer">
                            <button className="modal-btn confirm full" onClick={handleClose}>
                                확인
                            </button>
                        </div>
                    </>
                )}
            </div>
        </div>
    );
}

export default TransferModal;