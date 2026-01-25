import { AccountDTO } from "./dto/AccountDTO";

interface AccountItemResponse {
    totalCount: number;
    totalBalance: number;
    list: AccountDTO[];
}

export default AccountItemResponse;