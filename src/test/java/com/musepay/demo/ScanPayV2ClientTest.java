package com.musepay.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.musepay.demo.utils.RSAUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Offline checks plus disabled manual examples for ScanPay V2. */
public class ScanPayV2ClientTest {

    // 手工联调数据：商户信息在 config/scanpay-v2.properties 中维护。
    private static final String TEST_USER_XID = "202501201052";
    private static final String TEST_QRCODE = "00020101021228530011ph.ppmi.p2m0111SRCPPHM2XXX0312MRCHNT-4GTV105030005204481653036085406639.305802PH5912MuseShopping6010MakatiCity62610010ph.starpay0312MuseShopping0508OR#K4WA50708PayQR   0803***88280012ph.ppmi.qrph0108OR#K4WA56304B04C";
    private static final String TEST_QRCODE_AMOUNT = "639.3";
    private static final String TEST_ORDER_REQUEST_ID = "replace-with-submit-request-id";

    @Test
    public void shouldBuildSortedSignedRequestWithoutOptionalFields() throws Exception {
        KeyPair keyPair = generateKeyPair();
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        ScanPayV2Client client = ScanPayV2Client.build(
                "http://127.0.0.1:8090", privateKey, publicKey);

        Map<String, Object> request = new LinkedHashMap<>();
        request.put("qrcode", "QR-DEMO");
        request.put("user_xid", "user-demo");
        request.put("amount", "");
        request.put("ignored", null);

        JSONObject body = JSON.parseObject(client.buildSignedJson(request, "2000109", "nonce-1"));
        assertFalse(body.containsKey("amount"));
        assertFalse(body.containsKey("ignored"));
        assertTrue(body.containsKey("sign"));
        assertNotNull(body.getString("timestamp"));
        assertTrue(body.getString("sign").length() > 0);
        assertTrue(RSAUtils.verify(ScanPayV2Client.assembleContent(body), body.getString("sign"),
                publicKey, "UTF-8"));
    }

    @Test
    public void shouldSortAndFilterNestedKycMaps() throws Exception {
        KeyPair keyPair = generateKeyPair();
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        ScanPayV2Client client = ScanPayV2Client.build("http://127.0.0.1:8090", privateKey, null);

        Map<String, Object> individual = new LinkedHashMap<>();
        individual.put("last_name", "Demo");
        individual.put("first_name", "Alice");
        individual.put("gender", "WOMAN");
        individual.put("occupation", "");
        Map<String, Object> document = new LinkedHashMap<>();
        document.put("face", "data:image/jpeg;base64,ZmFrZQ==");
        document.put("front", "data:image/jpeg;base64,ZmFrZQ==");
        document.put("type", "national_id");
        document.put("back", null);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("document", document);
        params.put("individual", individual);

        JSONObject body = JSON.parseObject(client.buildSignedJson(params, "2000109", "nonce-2"));
        JSONObject normalizedIndividual = body.getJSONObject("individual");
        JSONObject normalizedDocument = body.getJSONObject("document");
        assertFalse(normalizedIndividual.containsKey("occupation"));
        assertFalse(normalizedDocument.containsKey("back"));
        assertTrue(normalizedDocument.containsKey("face"));
        assertTrue(body.getString("sign").length() > 0);
    }

    /** POST /v2/scanpay/user/create; creates a user, so it is disabled by default. */
    @Disabled("Manual integration only; creates a user")
    @Test
    public void manualUserCreate() {
        ScanPayV2Config config = ScanPayV2Config.load();
        ScanPayV2Client client = config.client();
        System.out.println(client.createUser(TEST_USER_XID,
                "scanpay-demo@example.com", "ScanPay Demo", "127.0.0.1",
                config.getPartnerId(), nonce("create")));
    }

    /** POST /v2/scanpay/user/query; read-only user existence query. */
    @Disabled("Manual integration only; requires local credentials")
    @Test
    public void manualUserQuery() {
        ScanPayV2Config config = ScanPayV2Config.load();
        System.out.println(config.client().queryUser(TEST_USER_XID, config.getPartnerId(),
                nonce("user-query")));
    }

    /** POST /v2/scanpay/user/kyc/upload; uploads KYC data, so it is disabled by default. */
    @Disabled("Manual integration only; uploads KYC data")
    @Test
    public void manualKycUpload() {
        ScanPayV2Config config = ScanPayV2Config.load();
        ScanPayV2Client client = config.client();
        System.out.println(client.uploadKyc(requestId("kyc"), TEST_USER_XID,
                individual(), document(), null, config.getPartnerId(), nonce("kyc")));
    }

    /** POST /v2/scanpay/user/kyc/link; may trigger a KYC fee, so it is disabled by default. */
    @Disabled("Manual integration only; may charge KYC fee")
    @Test
    public void manualKycLink() {
        ScanPayV2Config config = ScanPayV2Config.load();
        System.out.println(config.client().kycLink(requestId("link"), TEST_USER_XID, null,
                config.getPartnerId(), nonce("link")));
    }

    /** POST /v2/scanpay/user/kyc/query; read-only manual integration example. */
    @Disabled("Manual integration only; requires local credentials")
    @Test
    public void manualKycQuery() {
        ScanPayV2Config config = ScanPayV2Config.load();
        System.out.println(config.client().kycQuery(TEST_USER_XID, config.getPartnerId(),
                nonce("kyc-query")));
    }

    /** POST /v2/scanpay/qrcode/info; read-only manual integration example. */
    @Disabled("Manual integration only; requires local credentials")
    @Test
    public void manualQrcodeInfo() {
        ScanPayV2Config config = ScanPayV2Config.load();
        System.out.println(config.client().qrcodeInfo(TEST_USER_XID, TEST_QRCODE, null,
                config.getPartnerId(), nonce("qrcode")));
    }

    /** POST /v2/scanpay/submit; creates a payment order, so it is disabled by default. */
    @Disabled("Manual integration only; creates a payment order")
    @Test
    public void manualSubmit() {
        ScanPayV2Config config = ScanPayV2Config.load();
        System.out.println(config.client().submit(requestId("submit"), TEST_USER_XID,
                TEST_QRCODE, TEST_QRCODE_AMOUNT, null, config.getPartnerId(), nonce("submit")));
    }

    /** POST /v2/scanpay/query; read-only order query example. */
    @Disabled("Manual integration only; requires an existing order")
    @Test
    public void manualQuery() {
        ScanPayV2Config config = ScanPayV2Config.load();
        System.out.println(config.client().query(TEST_ORDER_REQUEST_ID, null,
                config.getPartnerId(), nonce("query")));
    }

    private static String requestId(String suffix) {
        return "demo-v2-" + suffix + "-" + System.currentTimeMillis();
    }

    private static String nonce(String suffix) {
        return "demo-v2-" + suffix + "-" + System.nanoTime();
    }

    private static Map<String, Object> individual() {
        Map<String, Object> individual = new LinkedHashMap<>();
        individual.put("last_name", "Demo");
        individual.put("first_name", "Alice");
        individual.put("date_of_birth", "1990-01-01");
        individual.put("gender", "WOMAN");
        return individual;
    }

    private static Map<String, Object> document() {
        String image = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII=";
        Map<String, Object> document = new LinkedHashMap<>();
        document.put("type", "national_id");
        document.put("front", image);
        document.put("back", image);
        document.put("face", image);
        document.put("number", "DEMO-001");
        document.put("country", "VN");
        document.put("issue_date", "2020-01-01");
        document.put("expiry_date", "2030-01-01");
        return document;
    }

    private static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }
}
