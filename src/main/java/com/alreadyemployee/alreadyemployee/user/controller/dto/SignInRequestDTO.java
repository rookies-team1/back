package com.alreadyemployee.alreadyemployee.user.controller.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignInRequestDTO {
    @NotBlank
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "유효한 이메일 형식을 입력해주세요."
    )
    @Schema(description = "이메일 주소", example = "test@example.com")

    private String email;

    @NotBlank
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 각각 최소 하나 이상 포함해야 합니다."
    )
    @Schema(description = "비밀번호", example = "Test1234!")

    private String password;
}
