package com.musepay.demo;

import com.musepay.demo.dto.Customer;
import org.junit.jupiter.api.Test;


public class ApiTest {

    /**
     * private key, please keep it in a safe place
     */
//    2000109
//    public static String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDX1gkQDfjY17X3ctFUK4kij+soicP0ZLRsFv9LmsDtVZ3tnP7jnfdXpKZ58T8UyNj8m63A9UKaiRe8yyCX7iNKoEZiN5i4Nl7EQ3BBTks2v132mH/mtOvPiczynpEuerVVfbC+0qI50TBaDfe+lLKURzoUs9KZZUWrUyeQTs7PoK6XIB2sN7v8dW4t8kSZ8honG25G3t8PGQKybUVlj1ALhCsxcDzOIi44yhW1LQ/0irHQJRjoZkgbEYnScep1SB1ShqBuAqLSzJSr2xmQn294+CHsQTGgT6zl17GVErAT/GjAR1+DMrLcodBx1bUNoYRJpcVIi6zzw5eC1G2lSkJ7AgMBAAECggEALwM0+CVC9eCOhzagN+g/t6KHg032u84PqpebxhkQOuHyQ2Z18phe3t1DUb5lkh5pfSRfxW7msgh4fiv0joNVdpTQn+YiQsW9AJz7342xPlySl/sBqPvxfUQzuwkk8G3T/6ogDICOy28wS/1M4Fs9RqT5FrdQgQ8ZDMdRD9LKo1iizDSZGX8HhLT7xodXW28b8rC8WiIWdoG/SI69iRTH+hmGLHKKLnNfZCEeTfumV/X9uNu+lmcrAKAeUylP9teavCP3ctVMvh6s0gxcdgJapRapQMP91+bUzSmROVvi1RYVq8ZH22PznGSDDdsFX5IJI2Bw8MfWO12hdEp80Ts2eQKBgQD/kIS6uEoD6Ur1j97smhKydT/K9dAhTD/z2zr900ZWxbdT9EGg77xK0P51HxyJ2WmtTOeQu1Ffgq1nwDHIAP+mFWxDFmupDkmTsUMP5E25CI/bx2qgeuwZgpDivQdQJDfCv99OT3U7CiGPdvx40D06fWpMs0vAJjAtHV52CA9KlwKBgQDYNC/MXS19eRvK/pv2ez8XUhTBVu6O1iiPiyPlMRR/2O9kxTHKLE8UbS8rB9XNyv8OrwHuW7aJFd6MNuCMCyo4OaEWi2LVMZyBmkCMndUQPetOvoqXB7cGgLLiziARB6gxQeHlTpJtgR3mFijtbfvruryfPlkjo8+xczeAysx3vQKBgGqLG2qkjebXibKaSjuleAoCK5g3po9bNIY7bXqIbBic0PMlL38iswMV4h992u31P8NJ/nguP5xr5FMOozJYiDv7TmkhnbybAj2advTXHzf2w9LCbkd9N5TjA/gjCjt0/fIo/7WsFpmIfblLx7GuTvhldL2JOhlfUlJcnhbjAUz9AoGAIkk8X0NTiHdhQfLYluD6o18f7X00vbAWpJTjL2B2KMQlSqy+9ZnLPKkH9uH6NPWBNznnq34d12WBcmjteSf4HQx+kE2wcbC5ahAlNmd9N2MqYsVywvq1OrHELJWVnj3BnXYeNbgdHoKAVFGlUenELqDhcrt3dWBv+msUHTeL710CgYAUX9Ga+tWdSnsGIkFsgzIlDLE/UTxD1RGUYzNGb7IP7RcK3fLOnSOlIpTWc4jOSOg8noz2MM525sP/wJWWOGK1Fj9rzFSwJ1uebt8Rim2mHkQKQKnkuju6Bv0TI8NfcnmPenIyyZ0W4fxAXAYedBjPcUKtDbgaLNOfgwuIZyKFZQ==";

//    2100063
    public static String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCnI1kB3OyurSfUaqIc7QPVbehYFeAXz3wRbr0KfL3bkF42r9lKUV5s5S3Bqfwu/L2r8kCFCVg9p6BBEZQFoGNp0LHqYThm89rWfzfFom6hncnUIUu67PYxq2tjazZRj/PxKjaGckPowXe6tbLapV2SiexdXFbW9SlsQQstXQW75aug+PElCYmy4dnv2f7OTF1PAkUTxTR1WNVhWZMRdqozmko3UsWDmT92JSYIzeES2AjktWYNAFrKGv7k/66jVHbieS9JAN6XU5EzBQ1pvlBk3oLHYRk0YKJG9Xrw822OLN8hO/Ty0et7qy/f9C38Nfw4UG4b+ZySZQJ8WbKLJMAbAgMBAAECggEAHRvk5pQpjIqPw0kHDu6gmk1YB+9XZg4213pn5imvj0vnfLLHr0/YmDKZ8369cxmFlyrL3d+wxJwrJun+07QJXGaCdgWUoymZVX42om8VwYQPoKhj3hxjDGeEfn4vqajenYPylxvTg/gd+CCpE7d1Qo5O4juwzCNKoZX6cl4fH4gqUk/yxxzFtUdA3knECmC0SxxesSqKwlKhFPfkLdvH2lBuhojfE+2Yo9AMFz4GfvDA4ds7SYPplm7K/57EA0qE75IBxuCnUIBimMFViZanmh08zbHVdlUcN1fXlxJnyv5dXh65OzLU7t96S1OXsmT3dMpRY4iJkAHdLgKLcRzSQQKBgQDdXtDqgSLV9fv5W9RABfCKlabdO+jzGwglWDQyBtTTioaTMEwY4UIxRm4YR4pXg0QNJnO6ROTcGYKrOJDD+L2WilVgVE4zntsN0Aj3vWLb7Sf/0u87nbU/HydPiSEz8H1AET60oWSXM1MLVaswynBz27QklmTINtskoF6gu3dx8QKBgQDBSLMPDLKawFSU3psRRZVQpHBQQjvkeqBHFDQzeOReQvnExuTQ3F7CE7Vw57+pvyS905sirmwUGfS+1ACqeXVz4Kn9rV2GS930oCBplJQgs7aJK0p0fALvrtL+Qjsga3FDAS8xHPzTDj66NelJI1AOFiUY/VoKwdNn40D4KR3GywKBgCvrBbOgjxK3zJe6Gi/hfclgy0wU+LBSaplOGHzcUhjt4KkO6en9tq4j9O+oMdAO4M9jE46e4HCyNvRVMpNOo/5bz3hfAWzIVVk2LrFHx3cuY8MjTAcd0LmHKrtiz02IprCxOymG43gD3LPg+Sei4hB6RBEGLVRzXaK0llF5H8dhAoGAebfFgym04/1Qhnt03bibIjCbxf8f5m9OtdREV1G/RpkY31F9UQYl6kQtE8/thAEqKxyx6nI6/6Gk3fN2A+T/ER0fD/B4IBVwzhd0sehuK/Xgcps/hQF/e971YkblIzJmHhMF3ADsOiETYYKHyZYiWOybKhSJ+pI7BoY3KNADv2cCgYAWS/XUef5V+R0xnGv6PvPWjT7q/Oa1G1RJ3uSVa3qL2WEWiwJpg+dC6wBTDsx7CRp5X0kodabLUSqCXkaho61AMwgiAgPCwGTXe4dZRs99cgNJjrer9Gcf/CYVA/43tMyuFFSvV794/oZ59nBaF3JyzeZxo3NKUgGpaKIKrlixkg==";

    /**
     * platform public key, please download from musepay partner dashboard
     */
    public static String platformKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlzLOiHRaKpPkOnBVtCUQnFVKyKn0BNNjc4NUPAXE/jrcE/mnO6uBBEpj6Xp3yCTqCU0yL1EhLZqyvEldPahVNVhAVet2joh6LLePd7wGMf96Il+Oh8g/lj9XNfKjqRm6ed0H5j+DeV0//tMOyU0f07PjxGTVCC//EcYLLLjR/bq5LVoczypGELWaRFYRbN8bZqs2/uJ8x46mFdK+vmMYtswCdGm9xQwOB9Q4l2M/zXh4pOZqdWfmgpvhUhHjG2/PcmYT4a3R3NTFhjc4RU8LRjaoxLGdHX6qWLBZhUrsCREYKfRSA+7L0ME8Y16wySH8yqb/QuC+H7d7AbLraUBf/wIDAQAB";

    private final String baseUrl = "https://api.test.musepay.io/v1/";

    private final MusepayClient client = MusepayClient.build(baseUrl, privateKey, platformKey);

    @Test
    public void queryPartnerBalance() {
        String respStr = client.queryPartnerBalance("USDT_BSC_TEST",
                "2000109",
                String.valueOf(System.currentTimeMillis()));
        System.out.println(respStr);
    }

    @Test
    public void depositAddress() {
        String respStr = client.queryDepositAddress("USDT_BSC_TEST",
                "C100036",
                "desc",
                "2000109",
                String.valueOf(System.currentTimeMillis()));
        System.out.println(respStr);
    }


    // 查询
    @Test
    public void query() {
        String respStr = client.queryTxnStatus("1737700456885",
                "2025012414000500043063417386",
                "2000109",
                String.valueOf(System.currentTimeMillis()));

        System.out.println(respStr);
    }

    // 提币
    @Test
    public void withdrawCoin() {
        String respStr = client.withdrawCoin(String.valueOf(System.currentTimeMillis()),
                "USDT_BSC_TEST",
                "0x7B8a14Bb5AFaC03b38b3631d069B4D954ea3DB1F",
                null,
                "1.6",
                "C100011",
                null,
                "desc",
                "2000109",
                String.valueOf(System.currentTimeMillis()));

        System.out.println(respStr);
    }

    // 支付
    @Test
    public void pay() {
        Customer customer = new Customer();
        customer.setCountry("");
        customer.setName("");
        customer.setEmail("");
        customer.setPhone("087877958811");
        customer.setDocumentId("");
        customer.setDocumentType("");

        String respStr = client.pay(String.valueOf(System.currentTimeMillis()),
                "USDT_BSC_TEST",
                "6",
                "123156225@qq.com",
                null,
                "2000109",
                String.valueOf(System.currentTimeMillis()),
                "on_chain",
                "wallet",
                "dana",
                null,
                customer);
        System.out.println(respStr);

    }

    /**
     * 印尼
     */
    @Test
    public void payIdr() {
        Customer customer = new Customer();
        customer.setCountry("");
        customer.setName("");
        customer.setEmail("");
        customer.setPhone("087877958811");
        customer.setDocumentId("");
        customer.setDocumentType("");

        String respStr = client.pay(String.valueOf(System.currentTimeMillis()),
                "IDR",
                "15555",
                "123156225@qq.com",
                null,
                "2000109",
                String.valueOf(System.currentTimeMillis()),
                "direct",
                "wallet",
                "dana",
                null,
                customer);
        System.out.println(respStr);

    }

    /**
     * 印度
     */
    @Test
    public void payInr() {
        Customer customer = new Customer();
        customer.setCountry("");
        customer.setName("");
        customer.setEmail("");
        customer.setPhone("087877958811");
        customer.setDocumentId("");
        customer.setDocumentType("");

        String respStr = client.pay(String.valueOf(System.currentTimeMillis()),
                "INR",
                "10000",
                "123156225@qq.com",
                null,
                "2000109",
                String.valueOf(System.currentTimeMillis()),
                "direct",
                "wallet",
                "upi",
                null,
                customer);
        System.out.println(respStr);

    }

    //pkr 支付
    @Test
    public void payPkr() {
        Customer customer = new Customer();
        customer.setCountry("");
        customer.setName("");
        customer.setEmail("");
        customer.setPhone("");
        customer.setDocumentId("");
        customer.setDocumentType("");

        String respStr = client.pay(String.valueOf(System.currentTimeMillis()),
                "PKR",
                "50",
                "",
                null,
                "2000109",
                String.valueOf(System.currentTimeMillis()),
                "direct",
                "wallet",
                "easypaisa",
                null,
                customer);
        System.out.println(respStr);

    }

    /**
     * 商户收付-收银台 交易币种-法币
     */
    @Test
    public void payOnLine() {
        Customer customer = new Customer();

        customer.setPhone("087877958811");

        String respStr = client.payOnChain(String.valueOf(System.currentTimeMillis()),
                "IDR",
                "100000",
                "12345@qq.com",
                "https://baidu.com",
                "2100063",
                String.valueOf(System.currentTimeMillis()),
                "on_line",
                "aaa",
                "USDT_BSC_TEST");

        System.out.println(respStr);
    }

    /**
     * 商户收付-收银台 交易币种USDT
     */
    @Test
    public void payOnLineUsdt() {
        Customer customer = new Customer();

        customer.setPhone("087877958811");

        String respStr = client.payOnChain(String.valueOf(System.currentTimeMillis()),
                "USDT_BSC_TEST",
                "3",
                "12345@qq.com",
                "https://baidu.com",
                "2100063",
                String.valueOf(System.currentTimeMillis()),
                "on_line",
                "aaa",
                "USDT_BSC_TEST");

        System.out.println(respStr);
    }

    /**
     * 扫码支付接口
     */
    @Test
    public void scanPaySubmit() {
        String respStr = client.scanPay(String.valueOf(System.currentTimeMillis()),
                "boom",
                "00020101021229370016A000000677010111011300666102576555802TH530376454044.22630464C9",
                "100",
                "https://www.baidu.com",
                "2100063",
                String.valueOf(System.currentTimeMillis()));
        System.out.println(respStr);
    }

    @Test
    public void payFiat() {
        Customer customer = new Customer();

        customer.setPhone("087877958811");

        String respStr = client.payOnChain(String.valueOf(System.currentTimeMillis()),
                "IDR",
                "30066",
                "12345@qq.com",
                "https://baidu.com",
                "2000109",
                String.valueOf(System.currentTimeMillis()),
                "on_chain",
                "aaa",
                "USDT_BSC_TEST");

        System.out.println(respStr);
    }

    //代付
    @Test
    public void payout() {
        String respStr = client.payout(String.valueOf(System.currentTimeMillis()),
                "IDR",
                "13555",
                null,
                "ID",
                "2000109",
                String.valueOf(System.currentTimeMillis()),
                "IDR",
                "EMAIL",
                "00000000",
                "BANK_TRANSFER",
                null,
                "1000120",
                "62800000000",
                "123@qq.com",
                "test",
                "CPF",
                "xxx",
                "SBIN0011891"
        );

        System.out.println(respStr);
    }

    @Test
    public void payoutThb() {
        String respStr = client.payout(String.valueOf(System.currentTimeMillis()),
                "THB",
                "102",
                null,
                "TH",
                "2000109",
                String.valueOf(System.currentTimeMillis()),
                "THB",
                "EMAIL",
                "9390301571",
                "BANK_TRANSFER",
                null,
                "1000620",
                "62800000000",
                "123@qq.com",
                "jing zhang",
                "CPF",
                "xxx",
                "SBIN0011891"
        );

        System.out.println(respStr);
    }

    @Test
    public void payoutInr() {
        String respStr = client.payout(String.valueOf(System.currentTimeMillis()),
                "INR",
                "500",
                null,
                "IN",
                "2000109",
                String.valueOf(System.currentTimeMillis()),
                "INR",
                "EMAIL",
                "925010031164305",
                "BANK_TRANSFER",
                null,
                "bank_card",
                "62800000000",
                "123@qq.com",
                "Rajan",
                "CPF",
                "xxx",
                "UTIB0003234"
        );

        System.out.println(respStr);
    }

    @Test
    public void payoutPkr() {
        String respStr = client.payout(String.valueOf(System.currentTimeMillis()),
                "PKR",
                "100",
                null,
                "PK",
                "2000109",
                String.valueOf(System.currentTimeMillis()),
                "PKR",
                "PHONE",
                "030947132012345",
                "WALLET_TRANSFER",
                "JAZZCASH",
                "1001113",
                "030947132012345",
                "123@qq.com",
                "jing zhang",
                "CPF",
                "4210112345679",
                "SBIN0011891"
        );

        System.out.println(respStr);
    }


    @Test
    /***
     * 商户提MPU到musewallet
     */
    public void payoutMuse() {
        String respStr = client.payout(String.valueOf(System.currentTimeMillis()),
                "MPU",
                "0.2",
                null,
                "BR",
                "2000109",
                String.valueOf(System.currentTimeMillis()),
                "MPU",
                "UID",
                "1000743",
                "wallet_transfer",
                "muse",
                null,
                "21312",
                "ztmsdu137@163.com",
                "test",
                "CPF",
                "xxx",
                null
        );
        System.out.println(respStr);
    }


    @Test
    public void verifyNotify() {
        String body = "{\"actual_amount\":\"0.000000002000000000\",\"currency\":\"USDT_BSC\",\"customer_ref_id\":\"buck2024\",\"extra_info\":\"{\\\"customerRefId\\\": \\\"buck2024\\\", \\\"destinationAddress\\\": \\\"19acE46vv1NUCzqCyP7naLPpVRjk2e4r2Z\\\"}\",\"fee_amount\":\"0.000000000000000000\",\"finish_time\":\"1663654745000\",\"order_amount\":\"0.000000002000000000\",\"order_no\":\"2022092020000600046060325168\",\"order_type\":\"extract\",\"partner_id\":\"2000051\",\"product_code\":\"m_extract_api\",\"request_id\":\"tk13\",\"sign\":\"kbJnxVJF73eagNTD5BNSpJJp8wvwHWEqnBBOrZ8ZGcbRooq4xflyb2OGJIwrbS1bhcvozNFi0BJy6VLvPdrmLpzRtsw9moWk/yLBt9x4K7iRUkkuLvQ8xQULj2k25KWWzXkk83SX44FVZjk77mJICbI6HKWWbti4xcX///8QudzhYmttEud9VSGjcxcHVTEmWQKX1O0X3zVTC+xWdaQrbgPNhhyFjlr1gNulOs77RPBK6NTg7RHYekr4840dxWZb+Ztnn9795aurm3z/jZTK4uO2FE/wLcidat+7pavackh/JXLkIHC5G5sEPThNOx2KvneClc+n9QCC2CJHT3/Bcg==\",\"status\":88}";
        boolean signOk = client.verifyNotify(body);
        System.out.println(signOk);
    }

    @Test
    public void estimate() {
        String respStr = client.estimate("USDT_BSC",
                "100",
                "2000109",
                String.valueOf(System.currentTimeMillis()));
        System.out.println(respStr);
    }

    @Test
    public void verifyDepositAddress() {
        String respStr = client.verifyDepositAddress("BTC_TEST",
                "n2iXfkTK1djWmBc9j6w8r2KkW59BQyikuX",
                "",
                "2000109",
                String.valueOf(System.currentTimeMillis()));
        System.out.println(respStr);
    }

    @Test
    public void queryTradeRate() {
        String respStr = client.queryTradeRate("DOGE",
                "USDT",
                "2000109",
                String.valueOf(System.currentTimeMillis()));
        System.out.println(respStr);
    }

    @Test
    public void queryConversionTradeRate() {
        String respStr = client.queryConversionTradeRate(
                "DOGE-USDT",
                "BUY",
                "2000109",
                String.valueOf(System.currentTimeMillis()));
        System.out.println(respStr);
    }

    @Test
    public void conversionQuote() {
        String respStr = client.conversionQuote("DOGE-USDT",
                "5",
                "USDT",
                "BUY",
                String.valueOf(System.currentTimeMillis()),
                "2000109",
                String.valueOf(System.currentTimeMillis()));
        System.out.println(respStr);
    }

    @Test
    public void conversionExecute() {
        String respStr = client.conversionExecute("2024110419000400190015716755",
                "2000109",
                String.valueOf(System.currentTimeMillis()));
        System.out.println(respStr);
    }

    @Test
    public void changeEmail() {
        String respStr = client.changeEmail("1110626",
                "ztmsdulv12@snapmail.cc",
                "2000109",
                String.valueOf(System.currentTimeMillis()));
        System.out.println(respStr);
    }
}
