package com.musepay.demo.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @Description //TODO
 * @Date 2020/5/8 15:10
 **/

@Data
@ToString
public class QueryOrderRequest extends CommonRequest {


    // 商户订单号
    @NotBlank
    private String request_id;

    // 系统订单号
    private String order_no;

}
