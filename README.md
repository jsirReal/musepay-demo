# musepay-demo
musepay api demo

## Fiat Payout 本地配置

复制 `config/fiat-payout.properties.example` 为 `config/fiat-payout.properties`，再填写
`partner_id` 和密钥。`partner_id` 为平台数字 userId（全平台 OpenAPI 统一约定）；报价/创建等
测试数据（国家/币种/银行/收款人）在测试代码的局部变量中按场景替换。
实际配置文件已加入 `.gitignore`，不会提交商户信息。

也可以在配置值中使用 `${ENV_NAME}` 引用环境变量，适合私钥等敏感值；未找到配置文件时，
`FiatPayoutConfig` 会兼容读取已有的 `MUSEPAY_*` 环境变量。

### 覆盖两类代付

- **本地付款（08 / LOCAL_PAYMENT）**：`GET` 能力查询（supports/countries、banks、networks、
  fields）→ `quote`（`clear_network=LOCAL_PAYMENT`，收款人走 `individual_beneficiary`/
  `enterprise_beneficiary`）→ `create`（order_no + purpose_code）→ `query`
- **国际汇款（04 / SWIFT）**：同上，但 `clear_network=SWIFT`，且 `beneficiaryFields` 动态传
  `swift` 银行代码，个人款必填 `nationality/gender/date_of_birth`（见 `manualQuoteSwift`）

> 说明：`create` 时 `purpose_code` 取自 `payouts/remitReasons` 返回的编码；报价有 60 秒有效期，
> 超时需重新报价（`FiatPayoutClientTest#createPayout`/`queryPayout` 会自动先做一次即时报价再调用）。
> 联调用例在 `FiatPayoutClientTest`，默认即可运行（点击即通），商户与密钥见 `config/fiat-payout.properties`。
