package com.musepay.demo.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class KycUploadRequest extends CommonRequest {

    private String request_id;
    private String user_xid;
    private KycDocument document;
}
