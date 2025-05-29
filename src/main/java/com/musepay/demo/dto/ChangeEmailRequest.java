package com.musepay.demo.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
public class ChangeEmailRequest extends CommonRequest{

    @NotBlank
    private String email;

    @NotBlank
    private String user_id;
}
