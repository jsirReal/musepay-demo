package com.musepay.demo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class QueryRateRequest extends CommonRequest {
    // 基础币种
    @NotBlank
    private String baseCurrency;

    // 报价币种
    @NotBlank
    private String quoteCurrency;
}
