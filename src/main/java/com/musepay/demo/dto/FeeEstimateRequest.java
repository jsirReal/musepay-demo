package com.musepay.demo.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @Description 费用预估
 * @Date 2020/5/8 14:44
 **/

@Data
@ToString(callSuper = true)
public class FeeEstimateRequest extends CommonRequest {

    @NotBlank
    // 币种
    private String currency;

    @NotBlank
    // 金额
    private String amount;

}
