package com.alreadyemployee.alreadyemployee.chat.controller.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SummarizeRequestDTO {
    @NotBlank
    private Long newsId;
}
