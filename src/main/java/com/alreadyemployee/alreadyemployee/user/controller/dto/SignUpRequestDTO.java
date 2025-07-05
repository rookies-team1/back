package com.alreadyemployee.alreadyemployee.user.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUpRequestDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
