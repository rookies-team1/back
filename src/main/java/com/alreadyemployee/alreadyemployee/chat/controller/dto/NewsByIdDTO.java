package com.alreadyemployee.alreadyemployee.chat.controller.dto;

import lombok.Builder;

@Builder
public class NewsByIdDTO {
    private Long id;
    private String title;
    private String contents;
    private String comapnyName;
}