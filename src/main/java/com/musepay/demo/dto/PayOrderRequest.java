package com.musepay.demo.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @Description 支付请求参数
 * @Date 2020/5/8 14:44
 **/

@Data
@ToString(callSuper = true)
public class PayOrderRequest extends CommonRequest {


    @NotBlank
    /** 商户订单号 **/
    private String request_id;

    @NotBlank
    private String currency;
    /** 订单金额 **/
    @NotBlank
    private String amount;

    /** 支付类型：on_line=收银台，on_chain=链上地址支付，direct=直连 **/
    @NotBlank
    private String payment_method;
    /**
     * 支付方式：wallet,virtual_account,qrcode,card,sepa
     */
    private String payment_type;
    /**
     * ovo, dana, pix,bca,bni
     */
    private String payment_channel;

    @NotBlank
    /** 商品名称 **/
    private String product_name;

    private String customer_ref_id;

    /** 备注 **/
    private String remark;

    private String email;

    private String customer;

    /** 通知地址 **/
    private String notify_url;

    /** 前台跳转地址 **/
    private String return_url;

    private String pay_currency;

}
