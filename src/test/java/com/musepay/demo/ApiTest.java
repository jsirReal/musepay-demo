package com.musepay.demo;

import com.musepay.demo.dto.Customer;
import org.junit.jupiter.api.Test;


public class ApiTest {

    /**
     * private key, please keep it in a safe place
     */
    public static String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDX1gkQDfjY17X3ctFUK4kij+soicP0ZLRsFv9LmsDtVZ3tnP7jnfdXpKZ58T8UyNj8m63A9UKaiRe8yyCX7iNKoEZiN5i4Nl7EQ3BBTks2v132mH/mtOvPiczynpEuerVVfbC+0qI50TBaDfe+lLKURzoUs9KZZUWrUyeQTs7PoK6XIB2sN7v8dW4t8kSZ8honG25G3t8PGQKybUVlj1ALhCsxcDzOIi44yhW1LQ/0irHQJRjoZkgbEYnScep1SB1ShqBuAqLSzJSr2xmQn294+CHsQTGgT6zl17GVErAT/GjAR1+DMrLcodBx1bUNoYRJpcVIi6zzw5eC1G2lSkJ7AgMBAAECggEALwM0+CVC9eCOhzagN+g/t6KHg032u84PqpebxhkQOuHyQ2Z18phe3t1DUb5lkh5pfSRfxW7msgh4fiv0joNVdpTQn+YiQsW9AJz7342xPlySl/sBqPvxfUQzuwkk8G3T/6ogDICOy28wS/1M4Fs9RqT5FrdQgQ8ZDMdRD9LKo1iizDSZGX8HhLT7xodXW28b8rC8WiIWdoG/SI69iRTH+hmGLHKKLnNfZCEeTfumV/X9uNu+lmcrAKAeUylP9teavCP3ctVMvh6s0gxcdgJapRapQMP91+bUzSmROVvi1RYVq8ZH22PznGSDDdsFX5IJI2Bw8MfWO12hdEp80Ts2eQKBgQD/kIS6uEoD6Ur1j97smhKydT/K9dAhTD/z2zr900ZWxbdT9EGg77xK0P51HxyJ2WmtTOeQu1Ffgq1nwDHIAP+mFWxDFmupDkmTsUMP5E25CI/bx2qgeuwZgpDivQdQJDfCv99OT3U7CiGPdvx40D06fWpMs0vAJjAtHV52CA9KlwKBgQDYNC/MXS19eRvK/pv2ez8XUhTBVu6O1iiPiyPlMRR/2O9kxTHKLE8UbS8rB9XNyv8OrwHuW7aJFd6MNuCMCyo4OaEWi2LVMZyBmkCMndUQPetOvoqXB7cGgLLiziARB6gxQeHlTpJtgR3mFijtbfvruryfPlkjo8+xczeAysx3vQKBgGqLG2qkjebXibKaSjuleAoCK5g3po9bNIY7bXqIbBic0PMlL38iswMV4h992u31P8NJ/nguP5xr5FMOozJYiDv7TmkhnbybAj2advTXHzf2w9LCbkd9N5TjA/gjCjt0/fIo/7WsFpmIfblLx7GuTvhldL2JOhlfUlJcnhbjAUz9AoGAIkk8X0NTiHdhQfLYluD6o18f7X00vbAWpJTjL2B2KMQlSqy+9ZnLPKkH9uH6NPWBNznnq34d12WBcmjteSf4HQx+kE2wcbC5ahAlNmd9N2MqYsVywvq1OrHELJWVnj3BnXYeNbgdHoKAVFGlUenELqDhcrt3dWBv+msUHTeL710CgYAUX9Ga+tWdSnsGIkFsgzIlDLE/UTxD1RGUYzNGb7IP7RcK3fLOnSOlIpTWc4jOSOg8noz2MM525sP/wJWWOGK1Fj9rzFSwJ1uebt8Rim2mHkQKQKnkuju6Bv0TI8NfcnmPenIyyZ0W4fxAXAYedBjPcUKtDbgaLNOfgwuIZyKFZQ==";
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
        String respStr = client.queryDepositAddress("USDT",
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
                "1.2",
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

    //pkr 支付
    @Test
    public void payPkr() {
        Customer customer = new Customer();
        customer.setCountry("");
        customer.setName("");
        customer.setEmail("");
        customer.setPhone("087877958811");
        customer.setDocumentId("");
        customer.setDocumentType("");

        String respStr = client.pay(String.valueOf(System.currentTimeMillis()),
                "PKR",
                "120",
                "123156225@qq.com",
                null,
                "2000109",
                String.valueOf(System.currentTimeMillis()),
                "direct",
                "wallet",
                "jazzcash",
                null,
                customer);
        System.out.println(respStr);

    }

    @Test
    public void payOnLine() {
        Customer customer = new Customer();

        customer.setPhone("087877958811");

        String respStr = client.payOnChain(String.valueOf(System.currentTimeMillis()),
                "IDR",
                "100000",
                "12345@qq.com",
                "https://baidu.com",
                "2000109",
                String.valueOf(System.currentTimeMillis()),
                "on_line",
                "aaa",
                "USDT_BSC_TEST");

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
                "10333",
                null,
                "ID",
                "2000109",
                String.valueOf(System.currentTimeMillis()),
                "IDR",
                "EMAIL",
                "12356789",
                "BANK_TRANSFER",
                null,
                "1000120",
                "21312",
                "123@qq.com",
                "test",
                "CPF",
                "xxx",
                "SBIN0011891"
        );

        System.out.println(respStr);
    }

    @Test
    public void payoutPkr() {
        String respStr = client.payout(String.valueOf(System.currentTimeMillis()),
                "PKR",
                "130",
                null,
                "PK",
                "2000109",
                String.valueOf(System.currentTimeMillis()),
                "PKR",
                "EMAIL",
                "01234567890123",
                "BANK_TRANSFER",
                null,
                "1001113",
                "3123456789",
                "123@qq.com",
                "Ali Khan",
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
    public void changeEmail(){
        String  respStr=client.changeEmail("1110626",
                "ztmsdulv12@snapmail.cc",
                "2000109",
                String.valueOf(System.currentTimeMillis()));
        System.out.println(respStr);
    }
}
