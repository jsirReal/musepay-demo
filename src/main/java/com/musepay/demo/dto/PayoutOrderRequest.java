package com.musepay.demo.dto;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @Description 代发请求参数
 * @Date 2020/5/8 14:44
 **/


@Data
@ToString(callSuper = true)
public class PayoutOrderRequest extends CommonRequest {

    /**
     * 代发订单基本信息
     */

    @NotBlank
    /** 商户订单号 **/
    private String request_id;
    /** 扣款币种 Merchant's account currency**/
    @NotBlank
    private String currency;
    /** 扣款金额 **/
    @NotBlank
    private String amount;
    /** 结算收款币种 Beneficiary's account currency**/
    @NotBlank
    private String settle_currency;


    /**
     * 渠道信息
     */

    /** 出款方式 bank_transfer, wallet_transfer**/
    @NotBlank
    private String payout_method;
    /** 如果payout_method = wallet_transfer， 则必填；
     ** 支持muse,ovo,dana,paypal等**/
    private String wallet_code;
    /** 如果payout_method = bank_transfer， 则必填**/
    private String bank_code;

    /**
     * 收款人信息
     */

    @NotBlank
    private String country;
    @NotBlank
    private String account_name;
    @NotBlank
    private String account_no;
    /** 帐户类型， EMAIL,PHONE, BANKCARD**/
    private String account_type;
    /** 证件编号 **/
    private String document_id;
    /** 证件类型 **/
    private String document_type;

    @Email
    private String email;

    private String phone;

    @Length(max = 100)
    private String bank_routing_code;

    /**
     * 其他信息
     */
    /** 打款备注 **/
    private String remark;
    /** 通知地址 **/
    private String notify_url;
}
