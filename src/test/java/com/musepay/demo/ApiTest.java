package com.musepay.demo;

import org.junit.jupiter.api.Test;


public class ApiTest {

    /**
     * private key, please keep it in a safe place
     */
    public static String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCP0P0KDjcHuI+Bhzebw/JDV1ZclAZbbAxtv9VFqhZ4dCbxd00tZoMgi8EI7m7LF4VJSeY/XeHsWjCflVyGYRfVJ2eCa+o0ModP2EJlZnVHFTjfKg1az3QILw60LhcAxcCi+UD7nax6CMZQgCsgsbwaDe+YrIgGE7eg/ti6JU8hi7i6m3GwqXc9+qH6MGoCwohxv1erg3wrK9vuDC9kBSKWvnolr0cgIsV5mxSgkmJoJdIbPbDVlTehFCfPnsO5xsThWfw5kPQIxtqkm7i762eUT1TsUqYJFabhgNKKsJsljSEo0lEDy9vwsGxXBj+XFRRdOTozUHHT+jKYnTRPACv/AgMBAAECggEAEzQVxbT2Niq3xtT2YO8+Ny2/U2dUnfqEglceNEos6/KejJmgjZJlqf/fS8ECvE2st4eNniJ8G2JDoduA2lS2qPi1Ap5ETKn+lXPMEdwnhtFXobzswZZ36OvXq8mHusutGFXuDmsUb8siJ/elSp1Q/62is6E+HImfeHjvGZQHx/O7+MdvbYNNn5EchomxDiRa8iHiF9Nrc56Vckjk3XcuCDH3eZMRghiWbii3IzYzG9ETEpK8S1gWKaYUWZGSIKvHz3k4W05/+WF98w72B4S78xjxa8SuRJgoeE5U0qoJ1UrX5CvaOOWuuGknwhzstEuMWNhZ6V/GdwZR9lz9l/p4sQKBgQDFR8t1+8ncM1Aa5jjcI+/CoW9bdf17VJBWyXAP/sJyHC6XtaP0yT7GQrjC8t7PPtPwBQnmnSD6ZTKbI0+Br9Dj4jsimnaL0EfJyDlcu6j7etCoOQSVDlw8w1T4RE3J+mqrBHNNEcxLUNVArPBPWpwN8lb7R1PKIULrg9acm7c9+QKBgQC6n2IhEK4ENn7+y3hA6JUD7z0cIivzs8Pjbo2yNbjFvDyjSsnKa+RcoRIy59kgmmFkEliuzrDAH4br2JKq6B3TU94sxk60ZqypsiTz/V7I5cb5jv99U9GigW9s1iwoCugJSctUqfLwQJwBtOO+hxADr3aoTVLP7Y02CRNbFl2XtwKBgQDCrfvBHUzxaQ22zdP0od37glWiuwf+yc96ZWSZ1DzMYLU17wCyElpJShSMBSINACIjbMV9dzfRAUZ0Q980ymxoRZs3pZgwlsQRAu5gbavvJx57s3CrKzWonNXf/X+KPv0+cLDbsCGbfVREc6Tdmjv/o1Nkutmb0UD5quuBNkUY+QKBgDr/E9h8G2b9i1wlGpj6bdFWmi0AqIBcPfryAh1qWkU4YrsEc5JoVULrMIOjQ8LIyy0Fl797W+kAjniUeJlK09Lw2nWxI0RoiQEEbiYr3QEJksNl72LBUq5a2MzBUChAemYlTAAx0bkd07O+aZjbvbZMi+hcuWc+I7wHVnUfNus1AoGAPrtLMEom1lTCFiboxirHMHCQOYJPPSZxGuYjwJ7WC++GeEt3SSEDPE470dw3lecJ6cmpQOQla5hppqUMAVefgyDuxwl+eo0b8PqFtehvRh5ME9aPQFZJbD5i1vyADbPs1OF6UwlfsFP6bSdVBg9bW6MBihB5eDu4Dn6P+gLN6D4=";
    /**
     * platform public key, please download from musepay partner dashboard
     */
    public static String platformKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlzLOiHRaKpPkOnBVtCUQnFVKyKn0BNNjc4NUPAXE/jrcE/mnO6uBBEpj6Xp3yCTqCU0yL1EhLZqyvEldPahVNVhAVet2joh6LLePd7wGMf96Il+Oh8g/lj9XNfKjqRm6ed0H5j+DeV0//tMOyU0f07PjxGTVCC//EcYLLLjR/bq5LVoczypGELWaRFYRbN8bZqs2/uJ8x46mFdK+vmMYtswCdGm9xQwOB9Q4l2M/zXh4pOZqdWfmgpvhUhHjG2/PcmYT4a3R3NTFhjc4RU8LRjaoxLGdHX6qWLBZhUrsCREYKfRSA+7L0ME8Y16wySH8yqb/QuC+H7d7AbLraUBf/wIDAQAB";

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
                "C100001",
                null,
                "2000051",
                String.valueOf(System.currentTimeMillis()));

        System.out.println(respStr);
    }

    @Test
    public void verifyNotify(){
        /**
         * Example notify data
         * sign : BOqBjmZ5udZm/IuabYTUPy81PWXU6z6Et0/ns6vpCzpQIO2gXi/f4ZyBpoiDhiFR5nHQnkMaExNAME3FtnRpt0BND0b/xUHjo3Pzn0jt9NHtxcCrfE/z6nlWEibL0XbFRKBBNXkGFPHNVeWGh87e1KNdukCAOIqlyNHmyHhAJRaahYbYM+CtxNMIsEwwqgmdkI4GmGyeVgc1tAfD1SAtvi0Qu1mI2Siy0tWqMAV1zAc3ALi0CE3w7JdfbQi2pearlUGhUni21tvbv73hUID98jB8CXQR0ajyLZ3ctV2mrKEgtnZgAxp9lTaGcYxm5J1pjjjMnUCmYQQM49C698KoAw==
         * params : actual_amount=0.000000990000000000&currency=TRX&customer_ref_id=C100001&extra_info={"txnHash": "b715736010e39a9b373a17160e78c595c053e366d579be424d3d4a769e381d77", "networkFee": "0.000000000000000000", "blockHeight": "43951251", "customerRefId": "C100001", "numOfConfirms": "1", "sourceAddress": "TLADrBG17GJ1Fu2N4DkqKBMhj6jR3pEb7t", "networkCurrency": "TRX", "destinationAddress": "TUUg6ME6K5F1hKn2n2P4yyPgNzzTd5bSZ4"}&fee_amount=0.000000010000000000&finish_time=1662432675000&order_amount=0.000001000000000000&order_no=2022090680000400002025115135&order_type=charge&partner_id=2000051&product_code=m_charge&request_id=2022090608009900881483970015&status=99
         */
        String sign = "BOqBjmZ5udZm/IuabYTUPy81PWXU6z6Et0/ns6vpCzpQIO2gXi/f4ZyBpoiDhiFR5nHQnkMaExNAME3FtnRpt0BND0b/xUHjo3Pzn0jt9NHtxcCrfE/z6nlWEibL0XbFRKBBNXkGFPHNVeWGh87e1KNdukCAOIqlyNHmyHhAJRaahYbYM+CtxNMIsEwwqgmdkI4GmGyeVgc1tAfD1SAtvi0Qu1mI2Siy0tWqMAV1zAc3ALi0CE3w7JdfbQi2pearlUGhUni21tvbv73hUID98jB8CXQR0ajyLZ3ctV2mrKEgtnZgAxp9lTaGcYxm5J1pjjjMnUCmYQQM49C698KoAw==";
        String params = "actual_amount=0.000000990000000000&currency=TRX&customer_ref_id=C100001&extra_info={\"txnHash\": \"b715736010e39a9b373a17160e78c595c053e366d579be424d3d4a769e381d77\", \"networkFee\": \"0.000000000000000000\", \"blockHeight\": \"43951251\", \"customerRefId\": \"C100001\", \"numOfConfirms\": \"1\", \"sourceAddress\": \"TLADrBG17GJ1Fu2N4DkqKBMhj6jR3pEb7t\", \"networkCurrency\": \"TRX\", \"destinationAddress\": \"TUUg6ME6K5F1hKn2n2P4yyPgNzzTd5bSZ4\"}&fee_amount=0.000000010000000000&finish_time=1662432675000&order_amount=0.000001000000000000&order_no=2022090680000400002025115135&order_type=charge&partner_id=2000051&product_code=m_charge&request_id=2022090608009900881483970015&status=99";
        boolean signOk = client.verifyNotify(sign,params);
        System.out.println(signOk);
    }
}
