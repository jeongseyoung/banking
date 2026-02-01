/* 
    private String accountNumber; //내계좌
    private String counterpartyAccountNumber; //상대방계좌
    private long amount;
    private String memo;
    private TransferType transferType;
*/

interface TransferRequest {
    fromAccountId: number,
    toAccountNumber: string,
    amount: number,
    memo?: string,
    transferType: string | null;
}

export default TransferRequest;