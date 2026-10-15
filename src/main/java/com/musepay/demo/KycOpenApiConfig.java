package com.musepay.demo;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class KycOpenApiConfig {
    public static final String DEFAULT_FILE = "config/kyc-openapi.properties";
    private final Properties properties;

    private KycOpenApiConfig(Properties properties) {
        this.properties = properties;
    }

    public static KycOpenApiConfig load() {
        String file = System.getProperty("musepay.kyc.openapi.config");
        if (file == null || file.trim().isEmpty()) file = System.getenv("MUSEPAY_KYC_OPENAPI_CONFIG");
        Path path = Paths.get(file == null || file.trim().isEmpty() ? DEFAULT_FILE : file);
        Properties properties = new Properties();
        if (Files.exists(path)) {
            try (InputStream input = Files.newInputStream(path)) {
                properties.load(input);
            } catch (Exception e) {
                throw new IllegalStateException("Unable to read KYC config: " + path, e);
            }
        }
        return new KycOpenApiConfig(properties);
    }

    public String getPartnerId() {
        return required("partner_id", "MUSEPAY_PARTNER_ID");
    }

    public KycOpenApiClient client() {
        String url = required("base_url", "MUSEPAY_KYC_BASE_URL");
        String key = required("merchant_private_key", "MUSEPAY_MERCHANT_PRIVATE_KEY");
        return Boolean.parseBoolean(value("insecure_ssl", null))
                ? KycOpenApiClient.buildNoSSL(url, key) : KycOpenApiClient.build(url, key);
    }

    private String required(String key, String env) {
        String value = value(key, env);
        if (value.isEmpty()) throw new IllegalArgumentException("Missing KYC setting: " + key);
        return value;
    }

    private String value(String key, String env) {
        String value = properties.getProperty(key);
        if (value != null && value.startsWith("${") && value.endsWith("}")) {
            value = System.getenv(value.substring(2, value.length() - 1));
        }
        if ((value == null || value.trim().isEmpty()) && env != null) value = System.getenv(env);
        return value == null ? "" : value.trim();
    }
}
