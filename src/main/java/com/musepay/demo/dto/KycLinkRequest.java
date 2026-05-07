package com.musepay.demo.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class KycLinkRequest extends CommonRequest {

    private String request_id;
    private String user_xid;
    private String level_name;
}
