# musepay-demo
musepay api demo

## ScanPay V2 本地配置

复制 `config/scanpay-v2.properties.example` 为 `config/scanpay-v2.properties`，再填写
`partner_id` 和密钥。`user_xid`、二维码属于具体测试数据，在测试代码的局部变量中按场景替换。
实际配置文件已加入 `.gitignore`，不会提交商户信息。

也可以在配置值中使用 `${ENV_NAME}` 引用环境变量，适合私钥等敏感值；未找到配置文件时，
`ScanPayV2Config` 会兼容读取已有的 `MUSEPAY_*` 环境变量。
