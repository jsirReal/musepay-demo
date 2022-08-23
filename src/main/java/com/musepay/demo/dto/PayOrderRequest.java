package com.musepay.demo.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @Description 余额支付请求参数
 * @Date 2020/5/8 14:44
 **/

@Data
@ToString(callSuper = true)
public class PayOrderRequest extends CommonRequest {


    // 商户订单号
    @NotBlank
    private String request_id;

    // 订单类型
    @NotBlank
    private String pay_type;

    // 币种
    @NotBlank
    private String currency;

    // 订单金额
    @NotBlank
    private String amount;

    // 商户用户ID
    @NotBlank
    private String customer_ref_id;

    // 通知地址
    private String notify_url;
}
