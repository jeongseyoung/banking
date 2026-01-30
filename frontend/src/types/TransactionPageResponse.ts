import TransactionDTO from "./dto/TransactionDTO";

interface TransactionPageResponse {
    /* 
            page, 
            size, 
            totalPage, 
            totalCount, 
            status, 
            accountNumber,
            list
    */
   page: number,
   size: number,
   totalPage: number,
   totalCount: number,
   status: string,
   accountNumber: string,
   list: TransactionDTO[]
}

export default TransactionPageResponse;