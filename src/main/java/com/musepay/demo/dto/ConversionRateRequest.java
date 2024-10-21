package com.musepay.demo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class ConversionRateRequest extends CommonRequest {
    // 交易对 exam: ETH-USDT
    @NotBlank
    private String symbol;

    @NotBlank
    private String side;

}
