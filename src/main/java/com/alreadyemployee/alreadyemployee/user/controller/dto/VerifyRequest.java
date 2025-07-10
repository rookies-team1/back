package com.alreadyemployee.alreadyemployee.user.controller.dto;

import lombok.Data;

@Data
public class VerifyRequest {
    private String email;
    private String code;
}
