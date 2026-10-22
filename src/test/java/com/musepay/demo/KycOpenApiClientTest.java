package com.musepay.demo;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KycOpenApiClientTest {
    @Disabled("手工联调：创建用户，需要本地商户私钥配置")
    @Test
    public void createApplicant() {
        String partnerId = "2000051";
        String requestId = "demo-kyc-create-timeout-1015-1625";
        String userXid = "demo_kyc_timeout_1015_1625";
        String email = "demo_kyc_timeout_1015_1625@example.com";
        String userName = "KYC Sandbox Final";
        String nonce = UUID.randomUUID().toString();

        KycOpenApiClient client = KycOpenApiConfig.load().client();
        String response = client.createApplicant(requestId, userXid, email, userName, partnerId, nonce);
        System.out.println(response);
        assertEquals("200", JSON.parseObject(response).getString("code"));
    }

    @Disabled("手工联调：生成收费订单，同用户所有档位共用在途限制")
    @Test
    public void link() {
        String partnerId = "2000051";
        String requestId = "demo-kyc-link-" + System.currentTimeMillis();
        String userXid = "demo_kyc_timeout_1015_1625";
        String levelName = "L1";
        String nonce = UUID.randomUUID().toString();

        KycOpenApiClient client = KycOpenApiConfig.load().client();
        String response = client.link(requestId, userXid, levelName, partnerId, nonce);
        System.out.println(response);
        assertEquals("200", JSON.parseObject(response).getString("code"));
    }

    @Disabled("手工联调：只读查询，需要本地商户私钥配置")
    @Test
    public void query() {
        String partnerId = "2000051";
        String requestId = "demo-kyc-query-" + System.currentTimeMillis();
        String userXid = "demo_kyc_timeout_1015_1625";
        String nonce = UUID.randomUUID().toString();

        KycOpenApiClient client = KycOpenApiConfig.load().client();
        String response = client.query(requestId, userXid, partnerId, nonce);
        System.out.println(response);
        assertEquals("200", JSON.parseObject(response).getString("code"));
    }
}
