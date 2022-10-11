package com.musepay.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChargeAddressQueryRequest extends CommonRequest{

    // 币种
    @NotBlank
    private String currency;


    @NotBlank
    // 商户用户ID
    private String customer_ref_id;

    // 描述
    private String description;

}
