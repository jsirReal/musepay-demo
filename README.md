# musepay-demo

MusePay OpenAPI Java demo for partner integration reference.

## Usage

1. Configure `baseUrl`, `privateKey`, `platformKey` in `src/test/java/com/musepay/demo/ApiTest.java`.
2. Use `MusepayClient.build(...)` to initialize client.
3. Run JUnit methods in `ApiTest` as integration examples.

## Covered APIs

- Payment / Payout / Withdraw / Query
- Deposit address / Verify deposit address
- Rate / Conversion / Fee estimate
- KYC OpenAPI:
  - `POST /v1/kyc/link`
  - `POST /v1/kyc/upload`
  - `POST /v1/kyc/query`
- KYT OpenAPI:
  - `POST /v1/kyt/wallet-check`
  - `POST /v1/kyt/transaction-check`
  - `POST /v1/kyt/query`

## KYC/KYT Notes

- `kyt/query` supports both:
  - `session_id` (preferred)
  - `request_id` fallback (mapped to risk `riskOrderNo`) when `session_id` is empty
- `kyc/upload` demo uses placeholder image data:
  - `REPLACE_WITH_FRONT_BASE64`
  - `REPLACE_WITH_BACK_BASE64`
  - `REPLACE_WITH_FACE_BASE64`
