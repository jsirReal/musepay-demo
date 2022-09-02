package com.musepay.demo;


import com.alibaba.fastjson.JSON;
import com.musepay.demo.dto.*;
import com.musepay.demo.utils.OkHttpUtils;
import com.musepay.demo.utils.SignUtils;
import okhttp3.OkHttpClient;

public class MusepayClient {

    private String merchantPrivateKey;

    private String platformPublicKey;

    private String baseUrl;

    private OkHttpClient httpClient;

    private void MusepayClient() {

    }

    public static MusepayClient build(String baseUrl, String merchantPrivateKey, String platformPublicKey) {
        MusepayClient client = new MusepayClient();
        client.baseUrl = baseUrl;
        client.merchantPrivateKey = merchantPrivateKey;
        client.platformPublicKey = platformPublicKey;
        client.httpClient = new OkHttpClient();
        return client;
    }

    /**
     * 查询充币地址
     * @param currency
     * @param customer_ref_id
     * @param partner_id
     * @param nonce
     * @return
     */
    public String queryDepositAddress(String currency, String customer_ref_id,
                                      String partner_id, String nonce) {
        ChargeAddressQueryRequest request = new ChargeAddressQueryRequest();
        request.setCurrency(currency);
        request.setCustomer_ref_id(customer_ref_id);

        request.setPartner_id(partner_id);
        request.setSign_type("RSA");
        request.setTimestamp(String.valueOf(System.currentTimeMillis()));
        request.setNonce(nonce);

        SignUtils.sign(request, merchantPrivateKey);

        return OkHttpUtils.doPost(httpClient, baseUrl + "order/deposit_address",
                JSON.toJSONString(request));
    }


    /**
     * 查询订单状态
     * @param request_id
     * @param order_no
     * @param partner_id
     * @param nonce
     * @return
     */
    public String queryTxnStatus(String request_id, String order_no,
                                 String partner_id, String nonce) {
        QueryOrderRequest request = new QueryOrderRequest();
        request.setRequest_id(request_id);
        request.setOrder_no(order_no);

        request.setPartner_id(partner_id);
        request.setSign_type("RSA");
        request.setTimestamp(String.valueOf(System.currentTimeMillis()));
        request.setNonce(nonce);

        SignUtils.sign(request, merchantPrivateKey);

        return OkHttpUtils.doPost(httpClient, baseUrl + "order/query",
                JSON.toJSONString(request));
    }

    /**
     * 发送提币交易
     * @param request_id
     * @param currency
     * @param address
     * @param amount
     * @param customer_ref_id
     * @param notify_url
     * @param partner_id
     * @param nonce
     * @return
     */
    public String withdrawCoin(String request_id, String currency, String address, String amount, String notify_url,
                               String partner_id, String nonce) {
        ExtractOrderRequest request = new ExtractOrderRequest();
        request.setRequest_id(request_id);
        request.setCurrency(currency);
        request.setAddress(address);
        request.setAmount(amount);
        request.setNotify_url(notify_url);

        request.setPartner_id(partner_id);
        request.setSign_type("RSA");
        request.setTimestamp(String.valueOf(System.currentTimeMillis()));
        request.setNonce(nonce);

        SignUtils.sign(request, merchantPrivateKey);

        return OkHttpUtils.doPost(httpClient, baseUrl + "order/withdraw",
                JSON.toJSONString(request));
    }

    /**
     * 查询商户余额
     * @param currency
     * @param partner_id
     * @param nonce
     * @return
     */
    public String queryPartnerBalance(String currency,
                                      String partner_id, String nonce) {
        QueryPartnerBalanceRequest request = new QueryPartnerBalanceRequest();
        request.setCurrency(currency);

        request.setPartner_id(partner_id);
        request.setSign_type("RSA");
        request.setTimestamp(String.valueOf(System.currentTimeMillis()));
        request.setNonce(nonce);

        SignUtils.sign(request, merchantPrivateKey);

        return OkHttpUtils.doPost(httpClient, baseUrl + "balance/partner",
                JSON.toJSONString(request));
    }

}
