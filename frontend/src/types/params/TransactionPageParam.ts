import TransferType from "../type/TransferType";

interface TransactionPageParam {
    page: number,
    size: number,
    /* 
    @Schema(description = "DEPOSIT, WITHDRAWAL, TRANS_IN, TRANS_OUT, INTEREST /// IF NULL = ALL")
    private TransferType transferType;
    @Schema(description = "DESC, ASC // IF NULL DESC")
    private OrderByType orderByType;
    */
    transferType?: TransferType,
    orderByType?: string
}

export default TransactionPageParam;