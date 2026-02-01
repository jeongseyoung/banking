interface TransferResponse {
    transactionId: number,
    fromAccountId: number,
    toAccountId: number,
    amount: number,
    balanceAfter: number,
    createdAt: string,
    status: string,
}

export default TransferResponse;