package com.musepay.demo.dto;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ToString(callSuper = true)
public class ScanPayOrderRequest extends CommonRequest {

    @NotBlank
    private String request_id;

    @NotBlank
    @Length(min = 1, max = 20)
    private String user_xid;

    @NotBlank
    @Length(min = 1, max = 500)
    private String qrcode;

    @NotNull
    private String amount;

    /** 可选参数  */

    @Length(min = 1, max = 500)
    private String notify_url;

}
