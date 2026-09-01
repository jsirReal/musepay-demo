package com.musepay.demo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/** Local-only credentials and endpoint configuration for Fiat Payout examples. */
public final class FiatPayoutConfig {

    public static final String DEFAULT_FILE = "config/fiat-payout.properties";

    private final String baseUrl;
    private final String partnerId;
    private final String merchantPrivateKey;
    private final String platformPublicKey;
    private final boolean insecureSsl;

    private FiatPayoutConfig(Properties properties) {
        baseUrl = required(properties, "base_url", "MUSEPAY_FIAT_PAYOUT_BASE_URL");
        partnerId = required(properties, "partner_id", "MUSEPAY_PARTNER_ID");
        merchantPrivateKey = required(properties, "merchant_private_key",
                "MUSEPAY_MERCHANT_PRIVATE_KEY");
        platformPublicKey = optional(properties, "platform_public_key",
                "MUSEPAY_PLATFORM_PUBLIC_KEY");
        insecureSsl = Boolean.parseBoolean(optional(properties, "insecure_ssl", null));
    }

    /** Loads the configured file, or falls back to the existing environment variables. */
    public static FiatPayoutConfig load() {
        String configuredPath = System.getProperty("musepay.fiat.payout.config");
        if (configuredPath == null || configuredPath.trim().isEmpty()) {
            configuredPath = System.getenv("MUSEPAY_FIAT_PAYOUT_CONFIG");
        }
        Path path = Paths.get(configuredPath == null || configuredPath.trim().isEmpty()
                ? DEFAULT_FILE : configuredPath);
        if (!Files.exists(path)) {
            return new FiatPayoutConfig(new Properties());
        }
        try (InputStream input = Files.newInputStream(path)) {
            Properties properties = new Properties();
            properties.load(input);
            return new FiatPayoutConfig(properties);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read Fiat Payout config: " + path, e);
        }
    }

    public FiatPayoutClient client() {
        return insecureSsl
                ? FiatPayoutClient.buildNoSSL(baseUrl, merchantPrivateKey, platformPublicKey)
                : FiatPayoutClient.build(baseUrl, merchantPrivateKey, platformPublicKey);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getPartnerId() {
        return partnerId;
    }

    private static String required(Properties properties, String key, String envKey) {
        String value = optional(properties, key, envKey);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing Fiat Payout setting: " + key);
        }
        return value.trim();
    }

    private static String optional(Properties properties, String key, String envKey) {
        String value = resolveReference(properties.getProperty(key));
        if ((value == null || value.trim().isEmpty()) && envKey != null) {
            value = System.getenv(envKey);
        }
        return value == null ? "" : value.trim();
    }

    private static String resolveReference(String value) {
        if (value != null && value.startsWith("${") && value.endsWith("}")) {
            return System.getenv(value.substring(2, value.length() - 1));
        }
        return value;
    }
}
