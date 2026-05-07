package com.musepay.demo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class KycDocument implements Serializable {

    private String type;
    private String country;
    private String front;
    private String back;
    private String face;
}
