package com.musepay.demo.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class KytTransactionCheckRequest extends CommonRequest {

    private String request_id;
    private String chain;
    private String currency;
    private String amount;
    private String txn_hash;
    private String destination_address;
}
