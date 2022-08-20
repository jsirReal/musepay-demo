package com.musepay.demo.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @Description 提币下发请求参数
 * @Date 2020/5/8 14:44
 **/

@Data
@ToString(callSuper = true)
public class ExtractOrderRequest extends CommonRequest {

    // 商户订单号
    @NotBlank
    private String request_id;

    // 币种
    @NotBlank
    private String currency;

    // 地址
    @NotBlank
    private String address;
    private String tag;

    // 订单金额
    @NotBlank
    private String amount;

    // 商户用户ID
    @NotBlank
    private String customer_ref_id;

    // 通知地址
    private String notify_url;
}
