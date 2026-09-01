package com.musepay.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.musepay.demo.utils.OkHttpSSL;
import com.musepay.demo.utils.OkHttpUtils;
import com.musepay.demo.utils.RSAUtils;
import okhttp3.OkHttpClient;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Fiat Payout OpenAPI 商户接入客户端（trade-center {@code /v1/fiatpayout}，plan §11 / §8 三服务连调）。
 * <p>
 * 独立 map-based 实现，签名规则与 {@link ScanPayV2Client} 一致（平台统一 OpenAPI 规范）：
 * 业务参数 + partner_id/sign_type/timestamp/nonce 组装，RSA 签名字符串 = 按 key 排序的
 * {@code k=v&k=v} 拼接（跳过 sign 与空值，嵌套对象按 JSON MapSortField 序列化）。
 * <p>
 * 支持两类代付：本地付款（08，{@code clear_network=LOCAL_PAYMENT}，bank 特有字段走
 * {@code beneficiaryFields} 动态 Map）与国际汇款（04/SWIFT，{@code clear_network=SWIFT}，
 * 必填 {@code beneficiaryFields.swift}）。beneficiary 未传的可选字段会被过滤为空字符串
 * {@code "null"}（与 trade-center toMetadataValue 处理一致，出站不携带残缺字段）。
 * <p>
 * 所有方法返回原始响应体字符串；业务字段校验失败（sign/参数/状态）由对端以 HTTP 4xx/5xx
 * 或业务码返回，调用方自行解析。
 */
public class FiatPayoutClient {

    private static final String SIGN_TYPE_RSA = "RSA";
    private static final String UTF_8 = "UTF-8";
    private static final String FIAT_PAYOUT_PATH = "/v1/fiatpayout/";

    private final String baseUrl;
    private final String merchantPrivateKey;
    private final String platformPublicKey;
    private final OkHttpClient httpClient;

    private FiatPayoutClient(String baseUrl, String merchantPrivateKey,
                             String platformPublicKey, OkHttpClient httpClient) {
        this.baseUrl = normalizeBaseUrl(baseUrl);
        this.merchantPrivateKey = requireText(merchantPrivateKey, "merchantPrivateKey");
        this.platformPublicKey = platformPublicKey;
        this.httpClient = httpClient;
    }

    public static FiatPayoutClient build(String baseUrl, String merchantPrivateKey,
                                         String platformPublicKey) {
        return new FiatPayoutClient(baseUrl, merchantPrivateKey, platformPublicKey,
                newHttpClient());
    }

    public static FiatPayoutClient buildNoSSL(String baseUrl, String merchantPrivateKey,
                                              String platformPublicKey) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(90, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .sslSocketFactory(OkHttpSSL.getIgnoreInitedSslContext().getSocketFactory(),
                            OkHttpSSL.IGNORE_SSL_TRUST_MANAGER_X509)
                    .hostnameVerifier(OkHttpSSL.getIgnoreSslHostnameVerifier())
                    .build();
            return new FiatPayoutClient(baseUrl, merchantPrivateKey, platformPublicKey, client);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to initialize insecure HTTP client", e);
        }
    }

    private static OkHttpClient newHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /** POST /v1/fiatpayout/supports/countries：支持的国家/币种（receive_currency 可选过滤）。 */
    public String supportsCountries(String receiveCurrency, String partnerId, String nonce) {
        Map<String, Object> params = new LinkedHashMap<>();
        put(params, "receive_currency", receiveCurrency);
        return post("supports/countries", params, partnerId, nonce);
    }

    /** POST /v1/fiatpayout/supports/banks：指定国家/币种下支持的银行（amount 可选，String）。 */
    public String supportsBanks(String country, String currency, String amount,
                                String partnerId, String nonce) {
        Map<String, Object> params = new LinkedHashMap<>();
        put(params, "country", country);
        put(params, "currency", currency);
        put(params, "amount", amount);
        return post("supports/banks", params, partnerId, nonce);
    }

    /** POST /v1/fiatpayout/supports/networks：指定国家/币种下支持的清算网络。 */
    public String supportsNetworks(String country, String currency, String partnerId, String nonce) {
        Map<String, Object> params = new LinkedHashMap<>();
        put(params, "country", country);
        put(params, "currency", currency);
        return post("supports/networks", params, partnerId, nonce);
    }

    /** POST /v1/fiatpayout/supports/fields：指定银行收款必填字段（key 回填到 quote 的 beneficiaryFields）。 */
    public String supportsFields(String country, String currency, String bankCode,
                                 String partnerId, String nonce) {
        Map<String, Object> params = new LinkedHashMap<>();
        put(params, "country", country);
        put(params, "currency", currency);
        put(params, "bank_code", bankCode);
        return post("supports/fields", params, partnerId, nonce);
    }

    /** POST /v1/fiatpayout/payouts/remitReasons：企业可用汇款用途（create 时用 code）。 */
    public String remitReasons(String partnerId, String nonce) {
        return post("payouts/remitReasons", new LinkedHashMap<>(), partnerId, nonce);
    }

    /**
     * POST /v1/fiatpayout/payouts/quote：报价（quoteMode=dest/source）。
     *
     * @param quoteParams 业务参数：request_id/quote_mode/pay_currency/receive_currency/
     *                    receive_amount/beneficiary_country/beneficiary_bank_id/account_type/
     *                    beneficiary_relationship/clear_network + beneficiaryFields（动态）
     *                    + individual_beneficiary / enterprise_beneficiary
     */
    public String quote(Map<String, Object> quoteParams, String partnerId, String nonce) {
        return post("payouts/quote", quoteParams, partnerId, nonce);
    }

    /** POST /v1/fiatpayout/payouts/create：按 quote 返回的 order_no 创建代付（订单编号取 create 响应）。 */
    public String createPayout(String orderNo, String purposeCode, String description,
                               String partnerId, String nonce) {
        Map<String, Object> params = new LinkedHashMap<>();
        put(params, "order_no", orderNo);
        put(params, "purpose_code", purposeCode);
        put(params, "description", description);
        return post("payouts/create", params, partnerId, nonce);
    }

    /** POST /v1/fiatpayout/payouts/query：查询代付状态（request_id=创建时业务单号，或仅 order_no）。 */
    public String queryPayout(String requestId, String orderNo, String partnerId, String nonce) {
        Map<String, Object> params = new LinkedHashMap<>();
        put(params, "request_id", requestId);
        put(params, "order_no", orderNo);
        return post("payouts/query", params, partnerId, nonce);
    }

    /** 验证平台异步通知签名（sign/sign_type/timestamp/nonce 顶层结构同请求）。 */
    public boolean verifyNotify(String body) {
        JSONObject response = JSON.parseObject(body);
        return RSAUtils.verify(assembleContent(response), response.getString("sign"),
                requireText(platformPublicKey, "platformPublicKey"), UTF_8);
    }

    /** Package-private offline seam: signs JSON but never sends a request. */
    String buildSignedJson(Map<String, Object> businessParams, String partnerId, String nonce) {
        Map<String, Object> request = new LinkedHashMap<>();
        if (businessParams != null) {
            request.putAll(businessParams);
        }
        put(request, "partner_id", partnerId);
        put(request, "sign_type", SIGN_TYPE_RSA);
        put(request, "timestamp", String.valueOf(System.currentTimeMillis()));
        put(request, "nonce", nonce == null ? UUID.randomUUID().toString() : nonce);
        put(request, "sign", RSAUtils.sign(assembleContent(request), merchantPrivateKey, UTF_8));
        return JSON.toJSONString(normalizeMap(request), SerializerFeature.MapSortField);
    }

    private String post(String endpoint, Map<String, Object> params,
                        String partnerId, String nonce) {
        return OkHttpUtils.doPost(httpClient, baseUrl + FIAT_PAYOUT_PATH + endpoint,
                buildSignedJson(params, partnerId, nonce));
    }

    static String assembleContent(Map<String, Object> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuilder content = new StringBuilder();
        for (String key : keys) {
            if ("sign".equals(key) || isDrop(params.get(key))) {
                continue;
            }
            Object value = params.get(key);
            if (content.length() > 0) {
                content.append('&');
            }
            content.append(key).append('=').append(formatSignValue(value));
        }
        return content.toString();
    }

    private static String formatSignValue(Object value) {
        if (value instanceof Map || value instanceof JSONObject
                || value instanceof Iterable || value.getClass().isArray()) {
            return JSON.toJSONString(normalizeValue(value), SerializerFeature.MapSortField);
        }
        return String.valueOf(value);
    }

    private static Object normalizeValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Map || value instanceof JSONObject) {
            Map<String, Object> result = new TreeMap<>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                Object normalized = normalizeValue(entry.getValue());
                if (!isDrop(normalized)) {
                    result.put(String.valueOf(entry.getKey()), normalized);
                }
            }
            return result;
        }
        if (value instanceof Iterable) {
            List<Object> result = new ArrayList<>();
            for (Object item : (Iterable<?>) value) {
                Object normalized = normalizeValue(item);
                if (!isDrop(normalized)) {
                    result.add(normalized);
                }
            }
            return result;
        }
        if (value.getClass().isArray()) {
            List<Object> result = new ArrayList<>();
            for (int i = 0; i < Array.getLength(value); i++) {
                Object normalized = normalizeValue(Array.get(value, i));
                if (!isDrop(normalized)) {
                    result.add(normalized);
                }
            }
            return result;
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> normalizeMap(Map<String, Object> source) {
        return (Map<String, Object>) normalizeValue(source);
    }

    private static boolean isBlank(Object value) {
        return value instanceof String && ((String) value).trim().isEmpty();
    }

    /**
     * 空值/空白字符串/字符串 {@code "null"} 一律视为缺失并过滤：
     * trade-center toMetadataValue 会把未传的可选字段序列化为字符串 {@code "null"}，
     * demo 不得把 {@code "null"} 当有效字段出站（出站只携带真实回填字段）。
     */
    private static boolean isDrop(Object value) {
        return value == null || isBlank(value) || "null".equals(value);
    }

    private static void put(Map<String, Object> map, String key, Object value) {
        if (!isDrop(value)) {
            map.put(key, value);
        }
    }

    private static String normalizeBaseUrl(String baseUrl) {
        String normalized = requireText(baseUrl, "baseUrl").trim();
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    private static String requireText(String value, String name) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
