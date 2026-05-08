package com.musepay.demo.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class KytWalletCheckRequest extends CommonRequest {

    private String request_id;
    private String wallet_address;
    private String chain;
}
