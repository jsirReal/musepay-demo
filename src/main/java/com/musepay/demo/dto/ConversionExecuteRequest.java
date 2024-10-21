package com.musepay.demo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class ConversionExecuteRequest extends CommonRequest {
    // 报价单号
    @NotBlank
    private String quoteId;

}
