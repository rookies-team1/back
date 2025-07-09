package com.alreadyemployee.alreadyemployee.chat.controller.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class SummarizeRequestDTO {
    private Long id;
    private String title;
    private String content;

    private String companyName;
}