package com.alreadyemployee.alreadyemployee.chat.controller.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SummarizeRequestDTO {
    @NotNull
    private Long id;  // 뉴스 id

    @NotBlank
    private String companyName; // 회사 이름

    @NotBlank
    private String title; // 뉴스 제목

    @NotBlank
    private String content; // 뉴스 원문

}
