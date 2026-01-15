기술 스택
Backend: Spring Boot 3.x, MyBatis 3.x, Lombok
Database: MySQL 8.0 / MariaDB
Security: JWT, OAuth2
Transaction: @Transactional(REPEATABLE_READ)
API: RESTful JSON API
Mapper: XML + ResultMap

핵심 기능 (100% 완성)
API	기능	상태
POST /transfer/deposit	계좌 입금	✅ 테스트 완료
POST /transfer/withdraw	계좌 출금	✅ 테스트 완료
POST /transfer/transfer	계좌 간 이체	✅ 더블 엔트리 구현

뱅킹 핵심 원칙 구현
✅ 더블 엔트리 회계원칙 (TRANSFER_OUT + TRANSFER_IN)
✅ 트랜잭션 격리 (REPEATABLE_READ)
✅ 실시간 잔액 검증 (balance_after)
✅ 계좌 상태 검증 (ACTIVE)
✅ 커스텀 예외처리 체계

시스템 아키텍처
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Controller    │───▶│   ServiceImpl    │───▶│    Mapper       │
│                 │    │                   │    │                 │
│ • deposit()     │    │ • executeTransfer │    │ • saveTransaction│
│ • withdrawal()  │    │ • REPEATABLE_READ │    │ • updateAccount │
│ • transfer()    │    │                   │    │                 │
└─────────────────┘    └──────────────────┘    └─────────┬───────┘
                                                        │
                                               ┌────────▼────────┐
                                               │     MySQL       │
                                               │                 │
                                               │ • accounts      │
                                               │ • transactions  │
                                               └─────────────────┘
DB 스키마
accounts 테이블
sql
account_id (PK, BIGINT)
account_number (VARCHAR(20), UNIQUE)
balance (BIGINT, 기본 0)
status (VARCHAR(10), ACTIVE/DORMANT/CLOSED)
created_at (DATETIME)
transactions 테이블
sql
id (PK, AUTO_INCREMENT)
account_id (FK, NOT NULL)
type (VARCHAR(20), DEPOSIT/WITHDRAWAL/TRANSFER_OUT/TRANSFER_IN)
counterparty_account_id (FK, NULL 허용)
amount (BIGINT)
balance_after (BIGINT)
memo (VARCHAR(255))
created_at (DATETIME)

API 명세
1. 입금
POST /transfer/deposit
{
  "accountNumber": "02330-35-02978900",
  "amount": 10000
}
Response: {success: true, balanceAfter: 10000}
2. 출금
POST /transfer/withdraw  
{
  "accountNumber": "02330-35-02978900",
  "amount": 5000
}
Response: {success: true, balanceAfter: 5000}
3. 이체
POST /transfer/transfer
{
  "accountNumber": "02330-35-02978900",
  "counterpartyAccountNumber": "12345-67-89012345",
  "amount": 3000
}
보안 & 트랜잭션
✅ JWT + OAuth2 인증 (jeongsy5021@gmail.com)
✅ @Transactional(isolation = REPEATABLE_READ)
✅ 더블 엔트리 원자성 보장
✅ 잔액 검증 (balance < amount 시 예외)
✅ 계좌 활성 상태 검증
✅ 동일 계좌 이체 방지

테스트 결과
✅ 입금: 10000원 → 잔액 10000원 ✓
✅ 출금: 5000원 → 잔액 5000원 ✓  
✅ 이체: A→B 3000원 (더블 엔트리) ✓
✅ 예외: 잔액 부족, 휴면계좌, 동일계좌 ✓