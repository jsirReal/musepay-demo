package com.musepay.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.musepay.demo.utils.RSAUtils;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Fiat Payout 商户接入示例（trade-center {@code /v1/fiatpayout}，plan §8/§11）。
 * <p>
 * 每个对外接口保留一个可直接运行的用例：商户信息在 {@code config/fiat-payout.properties}
 * （本地已配好，git 忽略），点运行即请求本地 trade-fiat-payout（8090）。两类代付均覆盖：
 * 国际汇款（04/SWIFT，本文件默认）、本地付款（08/LOCAL_PAYMENT，见 {@link #quoteLocal()}）。
 * <p>
 * 目录：
 * <ul>
 *     <li>能力查询（只读）：{@link #supportsCountries()} {@link #supportsBanks()}
 *         {@link #supportsNetworks()} {@link #supportsFields()} {@link #remitReasons()}</li>
 *     <li>业务链路：{@link #quoteSwift()}（国际汇款报价）→ {@link #createPayout()}
 *         → {@link #queryPayout()}；{@link #createPayout()}/{@link #queryPayout()} 内部
 *         先跑一次报价取 order_no（报价 60s 过期，不能用固定号）</li>
 * </ul>
 * 三个离线自检用例验证签名正确性，不发请求。
 */
public class FiatPayoutClientTest {

    /** 报价/创建测试数据（与三服务联调、国际汇款沙箱实测一致的跑通参数）。 */
    private static final String TEST_COUNTRY = "DE";
    private static final String TEST_CURRENCY = "EUR";
    private static final String TEST_AMOUNT = "100.00";
    private static final String TEST_BANK_ID = "2000800";
    private static final String TEST_CLEARING_NETWORK = "SWIFT";

    // ---------- 能力查询（只读，点击即通） ----------

    /** POST /supports/countries：支持的国家/币种列表。 */
    @Test
    public void supportsCountries() {
        FiatPayoutConfig config = FiatPayoutConfig.load();
        System.out.println(config.client().supportsCountries(null,
                config.getPartnerId(), nonce("countries")));
    }

    /** POST /supports/banks：指定国家/币种支持的银行（amount 为 String）。 */
    @Test
    public void supportsBanks() {
        FiatPayoutConfig config = FiatPayoutConfig.load();
        System.out.println(config.client().supportsBanks("TH", "THB", "100.00",
                config.getPartnerId(), nonce("banks")));
    }

    /** POST /supports/networks：指定国家/币种支持的清算网络（如 US/USD → [LOCAL_PAYMENT, ACH]）。 */
    @Test
    public void supportsNetworks() {
        FiatPayoutConfig config = FiatPayoutConfig.load();
        System.out.println(config.client().supportsNetworks("US", "USD",
                config.getPartnerId(), nonce("networks")));
    }

    /** POST /supports/fields：指定银行收款必填字段（key 回填到 quote 的 beneficiaryFields）。 */
    @Test
    public void supportsFields() {
        FiatPayoutConfig config = FiatPayoutConfig.load();
        System.out.println(config.client().supportsFields("TW", "TWD", "2000802",
                config.getPartnerId(), nonce("fields")));
    }

    /** POST /payouts/remitReasons：企业可用汇款用途（create 时用 code）。 */
    @Test
    public void remitReasons() {
        FiatPayoutConfig config = FiatPayoutConfig.load();
        System.out.println(config.client().remitReasons(config.getPartnerId(), nonce("reasons")));
    }

    // ---------- 业务链路 ----------

    /** POST /payouts/quote：国际汇款（04/SWIFT）报价，DE/EUR/Deutsche，个人(PERSONAL)/第三方(third)。 */
    @Test
    public void quoteSwift() {
        FiatPayoutConfig config = FiatPayoutConfig.load();
        System.out.println(config.client().quote(quoteSwiftParams("quote-swift"),
                config.getPartnerId(), nonce("quote-swift")));
    }

    /** POST /payouts/quote：本地付款（08/LOCAL_PAYMENT）报价，CN/AliPay 示例。 */
    @Test
    public void quoteLocal() {
        FiatPayoutConfig config = FiatPayoutConfig.load();
        System.out.println(config.client().quote(quoteLocalParams("quote-local"),
                config.getPartnerId(), nonce("quote-local")));
    }

    /** POST /payouts/create：先报价取 order_no（60s 有效），再创建代付；purpose_code 取 remitReasons。 */
    @Test
    public void createPayout() {
        FiatPayoutConfig config = FiatPayoutConfig.load();
        String orderNo = quoteOrderNo(config);
        System.out.println(config.client().createPayout(orderNo, "03",
                "fiat payout demo " + System.currentTimeMillis(),
                config.getPartnerId(), nonce("create")));
    }

    /** POST /payouts/query：先报价取 order_no，再查询代付状态。 */
    @Test
    public void queryPayout() {
        FiatPayoutConfig config = FiatPayoutConfig.load();
        String orderNo = quoteOrderNo(config);
        System.out.println(config.client().queryPayout(null, orderNo,
                config.getPartnerId(), nonce("query")));
    }

    // ---------- 离线自检（不发请求） ----------

    @Test
    public void shouldBuildSortedSignedRequestWithoutOptionalFields() throws Exception {
        KeyPair keyPair = generateKeyPair();
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        FiatPayoutClient client = FiatPayoutClient.build(
                "http://127.0.0.1:8090", privateKey, publicKey);

        Map<String, Object> request = new LinkedHashMap<>();
        request.put("country", "PH");
        request.put("currency", "PHP");
        request.put("amount", "");
        request.put("description", null);

        JSONObject body = JSON.parseObject(client.buildSignedJson(request, "2000051", "nonce-1"));
        assertFalse(body.containsKey("amount"));
        assertFalse(body.containsKey("description"));
        assertTrue(body.containsKey("sign"));
        assertNotNull(body.getString("timestamp"));
        assertTrue(body.getString("sign").length() > 0);
        assertTrue(RSAUtils.verify(FiatPayoutClient.assembleContent(body), body.getString("sign"),
                publicKey, "UTF-8"));
    }

    @Test
    public void shouldSortAndFilterNestedBeneficiaryMaps() throws Exception {
        KeyPair keyPair = generateKeyPair();
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        FiatPayoutClient client = FiatPayoutClient.build("http://127.0.0.1:8090", privateKey, null);

        Map<String, Object> individual = new LinkedHashMap<>();
        individual.put("last_name", "Smith");
        individual.put("first_name", "John");
        individual.put("date_of_birth", "1990-01-01");
        individual.put("nationality", "");
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("swift", "DEUTDEFF");
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("clear_network", "SWIFT");
        request.put("beneficiaryFields", fields);
        request.put("individual_beneficiary", individual);

        JSONObject body = JSON.parseObject(client.buildSignedJson(request, "2000051", "nonce-2"));
        JSONObject normalizedIndividual = body.getJSONObject("individual_beneficiary");
        JSONObject normalizedFields = body.getJSONObject("beneficiaryFields");
        assertFalse(normalizedIndividual.containsKey("nationality"));
        assertTrue(normalizedIndividual.containsKey("last_name"));
        assertTrue(normalizedIndividual.containsKey("first_name"));
        assertTrue(normalizedFields.containsKey("swift"));
        assertTrue(body.getString("sign").length() > 0);
    }

    @Test
    public void shouldFilterLiteralNullString() throws Exception {
        KeyPair keyPair = generateKeyPair();
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        FiatPayoutClient client = FiatPayoutClient.build("http://127.0.0.1:8090", privateKey, null);

        // trade-center toMetadataValue 会把未传的可选字段序列化为字符串 "null"，出站前按缺失过滤
        Map<String, Object> individual = new LinkedHashMap<>();
        individual.put("account_no", "15800012345");
        individual.put("gender", "null");
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("individual_beneficiary", individual);

        JSONObject body = JSON.parseObject(client.buildSignedJson(request, "2000051", "nonce-3"));
        JSONObject normalized = body.getJSONObject("individual_beneficiary");
        assertFalse(normalized.containsKey("gender"));
        assertTrue(normalized.containsKey("account_no"));
    }

    // ---------- 参数与工具 ----------

    /** 国际汇款（04/SWIFT）报价参数：与实测模板一致（docs/tests/2026-08-31-*.md round9）。 */
    private static Map<String, Object> quoteSwiftParams(String suffix) {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("swift", "DEUTDEFF");

        Map<String, Object> individual = new LinkedHashMap<>();
        individual.put("account_no", "DE89370400440532013000");
        individual.put("bank_name", "Deutsche Bank");
        individual.put("address", "Berlin");
        individual.put("first_name", "John");
        individual.put("last_name", "Smith");
        individual.put("nationality", "DE");
        individual.put("gender", "male");
        individual.put("date_of_birth", "1990-01-01");

        Map<String, Object> quote = new LinkedHashMap<>();
        quote.put("request_id", requestId(suffix));
        quote.put("quote_mode", "dest");
        quote.put("pay_currency", "USDT");
        quote.put("receive_currency", TEST_CURRENCY);
        quote.put("receive_amount", TEST_AMOUNT);
        quote.put("beneficiary_country", TEST_COUNTRY);
        quote.put("beneficiary_bank_id", TEST_BANK_ID);
        quote.put("account_type", "PERSONAL");
        quote.put("beneficiary_relationship", "third");
        quote.put("clear_network", TEST_CLEARING_NETWORK);
        quote.put("beneficiaryFields", fields);
        quote.put("individual_beneficiary", individual);
        return quote;
    }

    /** 本地付款（08/LOCAL_PAYMENT）报价参数：TH/SCB 示例。 */
    private static Map<String, Object> quoteLocalParams(String suffix) {
        Map<String, Object> individual = new LinkedHashMap<>();
        individual.put("account_no", "0891234567");
        individual.put("bank_name", "Siam Commercial Bank");
        individual.put("address", "TH");
        individual.put("first_name", "Somchai");
        individual.put("last_name", "Jaidee");
        individual.put("gender", "male");

        Map<String, Object> quote = new LinkedHashMap<>();
        quote.put("request_id", requestId(suffix));
        quote.put("quote_mode", "dest");
        quote.put("pay_currency", "USDT");
        quote.put("receive_currency", "THB");
        quote.put("receive_amount", "1000.00");
        quote.put("beneficiary_country", "TH");
        quote.put("beneficiary_bank_id", "1000616");
        quote.put("account_type", "PERSONAL");
        quote.put("beneficiary_relationship", "own");
        quote.put("clear_network", "LOCAL_PAYMENT");
        quote.put("individual_beneficiary", individual);
        return quote;
    }

    /** 先跑一次本地付款报价，从响应提取 order_no（报价 60s 过期，create/query 需用即时报价）。 */
    private static String quoteOrderNo(FiatPayoutConfig config) {
        String response = config.client().quote(quoteLocalParams("order-no"),
                config.getPartnerId(), nonce("order-no"));
        JSONObject root = JSON.parseObject(response);
        JSONObject data = root.getJSONObject("data");
        String orderNo = data == null ? null : data.getString("order_no");
        if (orderNo == null || orderNo.isEmpty()) {
            throw new IllegalStateException("报价未返回 order_no: " + response);
        }
        return orderNo;
    }

    private static String requestId(String suffix) {
        return "demo-fpo-" + suffix + "-" + System.currentTimeMillis();
    }

    private static String nonce(String suffix) {
        return "demo-fpo-" + suffix + "-" + System.nanoTime();
    }

    private static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }
}
