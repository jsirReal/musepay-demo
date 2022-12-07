package com.musepay.demo.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @Date 2020/5/8 15:10
 **/

@Data
@ToString
public class VerifyDepositAddressRequest extends CommonRequest {


    @NotBlank
    /** 币种 **/
    private String currency;

    @NotBlank
    /**
     * 地址
     */
    private String address;


    /**
     * 地址备注
     */
    private String tag;

}
