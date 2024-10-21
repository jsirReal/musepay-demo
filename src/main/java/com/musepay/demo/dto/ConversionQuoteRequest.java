package com.musepay.demo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class ConversionQuoteRequest extends CommonRequest {
    // 交易对
    @NotBlank
    private String symbol;

    @NotBlank
    private String amount;

    @NotBlank
    private String currency;

    @NotBlank
    private String side;

    @NotBlank
    private String xid;
}
