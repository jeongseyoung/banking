interface TransactionDTO {
    /* 
    private String accountId;
    private String counterPartyAccountId;
    private TransferType type;
    private long amount;
    private long balanceAfter;
    private String memo;
    private LocalDateTime createdAt;
    */
   accountId: number,
   counterPartyAccountId: number | null,
   type: string,
   amount: number,
   balanceAfter: number,
   memo: string,
   createdAt: string
}

export default TransactionDTO;