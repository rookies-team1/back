package com.alreadyemployee.alreadyemployee.chat.controller.dto;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class SummarizeResponseDTO {
    private String summary;
    private boolean error;
    private String error_content;
    
}