package com.musepay.demo.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @Date 2020/5/8 15:10
 **/

@Data
@ToString
public class QueryUserBalanceRequest extends CommonRequest {

    // 币种
    @NotBlank
    private String currency;

    // 商户用户ID
    @NotBlank
    private String customer_ref_id;

}
