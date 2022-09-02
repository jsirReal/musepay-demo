package com.musepay.demo;

import org.junit.jupiter.api.Test;


public class ApiTest {

    /**
     * private key, please keep it in a safe place
     */
    public static String privateKey = "";

    /**
     * platform public key, please download from musepay partner dashboard
     */
    public static String platformKey = "";

    private final String baseUrl = "http://api.dev.musepay.io/v1/";

    private final MusepayClient client = MusepayClient.build(baseUrl, privateKey, platformKey);

    @Test
    public void queryPartnerBalance() {
        String respStr = client.queryPartnerBalance("USDT_TRC20",
                "2000051",
                String.valueOf(System.currentTimeMillis()));
        System.out.println(respStr);
    }

    @Test
    public void depositAddress() {
        String respStr = client.queryDepositAddress("TRX",
                "C100001",
                "2000051",
                String.valueOf(System.currentTimeMillis()));
        System.out.println(respStr);
    }


    // 查询
    @Test
    public void query() {
        String respStr = client.queryTxnStatus("1660903745906",
                "2022081920000600086100906100",
                "2000051",
                String.valueOf(System.currentTimeMillis()));

        System.out.println(respStr);
    }

    // 提币
    @Test
    public void withdrawCoin() {
        String respStr = client.withdrawCoin(String.valueOf(System.currentTimeMillis()),
                "BNB_BSC",
                "0xADFB656a8D0c0D22c523404c23699f9299975216",
                "0.3",
                null,
                "2000051",
                String.valueOf(System.currentTimeMillis()));

        System.out.println(respStr);
    }

}
