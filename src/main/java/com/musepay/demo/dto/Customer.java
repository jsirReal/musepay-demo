package com.musepay.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    private String country;
    private String name;
    private String email;
    private String phone;
    private String phoneAreaCode;
    private String documentId;
    private String documentType;

}