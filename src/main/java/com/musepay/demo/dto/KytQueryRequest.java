package com.musepay.demo.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class KytQueryRequest extends CommonRequest {

    private String request_id;
    private String order_no;
}
