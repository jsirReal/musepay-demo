package com.musepay.demo.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @Description 转账请求参数
 * @Date 2020/5/8 14:44
 **/


@Data
@ToString(callSuper = true)
public class TransferOrderRequest extends CommonRequest {


    // 商户订单号
    @NotBlank
    private String request_id;

    // 币种
    @NotBlank
    private String currency;

    // 订单金额
    @NotBlank
    private String amount;

    // from 用户ID
    @NotBlank
    private String from_customer_ref_id;

    // to 用户ID
    @NotBlank
    private String to_customer_ref_id;

    // 通知地址
    private String notify_url;
}
