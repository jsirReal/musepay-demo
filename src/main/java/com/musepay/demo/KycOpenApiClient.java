package com.musepay.demo;

import com.alibaba.fastjson.JSON;
import com.musepay.demo.utils.OkHttpSSL;
import com.musepay.demo.utils.OkHttpUtils;
import com.musepay.demo.utils.RSAUtils;
import okhttp3.OkHttpClient;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class KycOpenApiClient {
    private final String baseUrl;
    private final String privateKey;
    private final OkHttpClient httpClient;

    private KycOpenApiClient(String baseUrl, String privateKey, OkHttpClient client) {
        if (baseUrl == null || privateKey == null || privateKey.trim().isEmpty()) {
            throw new IllegalArgumentException("baseUrl and merchantPrivateKey are required");
        }
        this.baseUrl = baseUrl.replaceAll("/+$", "");
        this.privateKey = privateKey;
        this.httpClient = client;
    }

    public static KycOpenApiClient build(String baseUrl, String privateKey) {
        return new KycOpenApiClient(baseUrl, privateKey, builder().build());
    }

    public static KycOpenApiClient buildNoSSL(String baseUrl, String privateKey) {
        try {
            return new KycOpenApiClient(baseUrl, privateKey, builder()
                    .sslSocketFactory(OkHttpSSL.getIgnoreInitedSslContext().getSocketFactory(),
                            OkHttpSSL.IGNORE_SSL_TRUST_MANAGER_X509)
                    .hostnameVerifier(OkHttpSSL.getIgnoreSslHostnameVerifier()).build());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to initialize insecure HTTP client", e);
        }
    }

    private static OkHttpClient.Builder builder() {
        return new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS);
    }

    public String createApplicant(String requestId, String userXid, String email,
                                  String userName, String partnerId, String nonce) {
        Map<String, Object> params = params(requestId, userXid);
        params.put("email", email);
        params.put("user_name", userName);
        return post("applicant/create", params, partnerId, nonce);
    }

    public String link(String requestId, String userXid, String levelName,
                       String partnerId, String nonce) {
        if (!java.util.Arrays.asList("L1", "L2", "L1_AML", "L2_AML").contains(levelName)) {
            throw new IllegalArgumentException("levelName is required and must be L1/L2/L1_AML/L2_AML");
        }
        Map<String, Object> params = params(requestId, userXid);
        params.put("level_name", levelName);
        return post("link", params, partnerId, nonce);
    }

    public String query(String requestId, String userXid, String partnerId, String nonce) {
        return post("query", params(requestId, userXid), partnerId, nonce);
    }

    private static Map<String, Object> params(String requestId, String userXid) {
        Map<String, Object> params = new TreeMap<>();
        params.put("request_id", requestId);
        params.put("user_xid", userXid);
        return params;
    }

    private String post(String endpoint, Map<String, Object> params, String partnerId, String nonce) {
        return OkHttpUtils.doPost(httpClient, baseUrl + "/kyc/" + endpoint,
                buildSignedJson(params, partnerId, nonce));
    }

    String buildSignedJson(Map<String, Object> params, String partnerId, String nonce) {
        Map<String, Object> request = new TreeMap<>(params);
        request.put("partner_id", partnerId);
        request.put("sign_type", "RSA");
        request.put("timestamp", String.valueOf(System.currentTimeMillis()));
        request.put("nonce", nonce == null ? UUID.randomUUID().toString() : nonce);
        request.entrySet().removeIf(entry -> entry.getValue() == null
                || entry.getValue().toString().trim().isEmpty());
        request.put("sign", RSAUtils.sign(assembleContent(request), privateKey, "UTF-8"));
        return JSON.toJSONString(request);
    }

    static String assembleContent(Map<String, Object> request) {
        StringBuilder content = new StringBuilder();
        new TreeMap<>(request).forEach((key, value) -> {
            if (!"sign".equals(key) && value != null && !value.toString().trim().isEmpty()) {
                if (content.length() > 0) content.append('&');
                content.append(key).append('=').append(value);
            }
        });
        return content.toString();
    }
}
