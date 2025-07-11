package com.alreadyemployee.alreadyemployee.chat.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
@Getter
public class NewsByIdDTO {
    private Long id;
    private String title;
    private String content;
    private String company_name;
}