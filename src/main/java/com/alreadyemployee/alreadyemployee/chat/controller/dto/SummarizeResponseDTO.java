package com.alreadyemployee.alreadyemployee.chat.controller.dto;


import lombok.Data;

@Data
public class SummarizeResponseDTO {
    private String summary;
    private boolean error;
    private String error_content;
}