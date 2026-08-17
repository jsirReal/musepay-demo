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

/** Independent map-based ScanPay V2 OpenAPI client. */
public class ScanPayV2Client {

    private static final String SIGN_TYPE_RSA = "RSA";
    private static final String UTF_8 = "UTF-8";
    private static final String V2_SCANPAY_PATH = "/v2/scanpay/";

    private final String baseUrl;
    private final String merchantPrivateKey;
    private final String platformPublicKey;
    private final OkHttpClient httpClient;

    private ScanPayV2Client(String baseUrl, String merchantPrivateKey,
                            String platformPublicKey, OkHttpClient httpClient) {
        this.baseUrl = normalizeBaseUrl(baseUrl);
        this.merchantPrivateKey = requireText(merchantPrivateKey, "merchantPrivateKey");
        this.platformPublicKey = platformPublicKey;
        this.httpClient = httpClient;
    }

    public static ScanPayV2Client build(String baseUrl, String merchantPrivateKey,
                                        String platformPublicKey) {
        return new ScanPayV2Client(baseUrl, merchantPrivateKey, platformPublicKey,
                newHttpClient());
    }

    public static ScanPayV2Client buildNoSSL(String baseUrl, String merchantPrivateKey,
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
            return new ScanPayV2Client(baseUrl, merchantPrivateKey, platformPublicKey, client);
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

    public String createUser(String userXid, String email,
                             String userName, String ip, String partnerId, String nonce) {
        Map<String, Object> params = new LinkedHashMap<>();
        put(params, "user_xid", userXid);
        put(params, "email", email);
        put(params, "user_name", userName);
        put(params, "ip", ip);
        return post("user/create", params, partnerId, nonce);
    }

    /** 查询商户子用户；只读，不创建用户、不收费。 */
    public String queryUser(String userXid, String partnerId, String nonce) {
        Map<String, Object> params = new LinkedHashMap<>();
        put(params, "user_xid", userXid);
        return post("user/query", params, partnerId, nonce);
    }

    /** Alias matching the endpoint name. */
    public String userQuery(String userXid, String partnerId, String nonce) {
        return queryUser(userXid, partnerId, nonce);
    }

    public String uploadKyc(String requestId, String userXid,
                            Map<String, Object> individual,
                            Map<String, Object> document,
                            Map<String, Object> address,
                            String partnerId, String nonce) {
        Map<String, Object> params = new LinkedHashMap<>();
        put(params, "request_id", requestId);
        put(params, "user_xid", userXid);
        put(params, "individual", individual);
        put(params, "document", document);
        put(params, "address", address);
        return post("user/kyc/upload", params, partnerId, nonce);
    }

    public String createKycLink(String requestId, String userXid, String levelName,
                                String partnerId, String nonce) {
        Map<String, Object> params = new LinkedHashMap<>();
        put(params, "request_id", requestId);
        put(params, "user_xid", userXid);
        put(params, "level_name", levelName);
        return post("user/kyc/link", params, partnerId, nonce);
    }

    /** Alias matching the endpoint name. */
    public String kycLink(String requestId, String userXid, String levelName,
                          String partnerId, String nonce) {
        return createKycLink(requestId, userXid, levelName, partnerId, nonce);
    }

    public String queryKyc(String userXid, String partnerId, String nonce) {
        Map<String, Object> params = new LinkedHashMap<>();
        put(params, "user_xid", userXid);
        return post("user/kyc/query", params, partnerId, nonce);
    }

    /** Alias matching the endpoint name. */
    public String kycQuery(String userXid, String partnerId, String nonce) {
        return queryKyc(userXid, partnerId, nonce);
    }

    public String qrcodeInfo(String userXid, String qrcode, String amount,
                             String partnerId, String nonce) {
        Map<String, Object> params = new LinkedHashMap<>();
        put(params, "user_xid", userXid);
        put(params, "qrcode", qrcode);
        put(params, "amount", amount);
        return post("qrcode/info", params, partnerId, nonce);
    }

    /** V2 does not support beneficiary_account; recipient data comes from QR. */
    public String submit(String requestId, String userXid, String qrcode, String amount,
                         String notifyUrl, String partnerId, String nonce) {
        Map<String, Object> params = new LinkedHashMap<>();
        put(params, "request_id", requestId);
        put(params, "user_xid", userXid);
        put(params, "qrcode", qrcode);
        put(params, "amount", amount);
        put(params, "notify_url", notifyUrl);
        return post("submit", params, partnerId, nonce);
    }

    public String query(String requestId, String orderNo, String partnerId, String nonce) {
        Map<String, Object> params = new LinkedHashMap<>();
        put(params, "request_id", requestId);
        put(params, "order_no", orderNo);
        return post("query", params, partnerId, nonce);
    }

    /** 验证平台异步通知签名。 */
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
        return OkHttpUtils.doPost(httpClient, baseUrl + V2_SCANPAY_PATH + endpoint,
                buildSignedJson(params, partnerId, nonce));
    }

    static String assembleContent(Map<String, Object> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuilder content = new StringBuilder();
        for (String key : keys) {
            if ("sign".equals(key)) {
                continue;
            }
            Object value = params.get(key);
            if (value == null || isBlank(value)) {
                continue;
            }
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
                if (normalized != null && !isBlank(normalized)) {
                    result.put(String.valueOf(entry.getKey()), normalized);
                }
            }
            return result;
        }
        if (value instanceof Iterable) {
            List<Object> result = new ArrayList<>();
            for (Object item : (Iterable<?>) value) {
                Object normalized = normalizeValue(item);
                if (normalized != null && !isBlank(normalized)) {
                    result.add(normalized);
                }
            }
            return result;
        }
        if (value.getClass().isArray()) {
            List<Object> result = new ArrayList<>();
            for (int i = 0; i < Array.getLength(value); i++) {
                Object normalized = normalizeValue(Array.get(value, i));
                if (normalized != null && !isBlank(normalized)) {
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

    private static void put(Map<String, Object> map, String key, Object value) {
        if (value != null && !isBlank(value)) {
            map.put(key, value);
        }
    }

    private static String normalizeBaseUrl(String baseUrl) {
        String normalized = requireText(baseUrl, "baseUrl").trim();
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        if (normalized.endsWith("/v1") || normalized.endsWith("/v2/scanpay")) {
            throw new IllegalArgumentException("baseUrl must omit /v1 and /v2/scanpay");
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
