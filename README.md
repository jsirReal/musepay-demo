# musepay-demo
musepay api demo

## KYC OpenAPI

独立客户端 `KycOpenApiClient`，不修改已有支付客户端。参考 feature_scanpay_v2 的配置与示例方式：

1. 复制 `config/kyc-openapi.properties.example` 为 `config/kyc-openapi.properties`，填写商户号、RSA 私钥；实际配置已忽略，不提交。
2. `base_url` 为完整 API 前缀，本地为 `http://localhost:8093/v1`；网关填写已发布的 API base，不自动补 `/v1`。
3. 可通过 `-Dmusepay.kyc.openapi.config=<path>` 或 `MUSEPAY_KYC_OPENAPI_CONFIG` 指定配置文件，配置值支持 `${ENV_NAME}`。

调用顺序：`createApplicant` → `link` → `query`，对应 `/kyc/applicant/create`、`/kyc/link`、`/kyc/query`。不提供 upload。

- `level_name` 必填：L1 / L2 / L1_AML / L2_AML；不能留空，不提供默认档。不是 Sumsub 后台 level 名。
- create 按同商户＋user_xid 幂等；link 使用唯一 request_id，同 request_id 重试返回原响应。
- 同商户＋同用户所有档位只允许一笔在途订单；完成或关闭后才能再次申请。
- link 创建收费订单，最终审核 GREEN 或 RED＋FINAL 才完成收费；RED＋RETRY 不完成收费。
- 当前商户 KYC 结果回调与 VCC 一致，没有 RSA 签名，不要套用平台支付回调验签。

`KycOpenApiClientTest` 仅有 createApplicant/link/query 三个手工测试方法，各方法内展开 requestId、userXid、email/userName、levelName、partnerId、nonce，商户号默认使用联调商户2000051。示例用户为已存在的kyc4_final_1014；参数可按场景修改，timestamp和sign由客户端生成，base_url与私钥在properties中配置。默认 `@Disabled`，显式移除后在IDE单独运行；link会产生收费订单，不要批量自动执行。

验证命令（现有 pom 未固定 Surefire 版本，用支持 JUnit 5 的版本运行）：
```bash
mvn package -DskipTests
mvn org.apache.maven.plugins:maven-surefire-plugin:2.22.2:test -Dtest=KycOpenApiClientTest
```
