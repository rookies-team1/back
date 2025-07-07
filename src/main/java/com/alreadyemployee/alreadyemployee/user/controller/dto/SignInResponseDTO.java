package com.alreadyemployee.alreadyemployee.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class SignInResponseDTO {
    private String accessToken;
    private String tokenType;
    private String refreshToken;
}
