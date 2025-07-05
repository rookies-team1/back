package com.alreadyemployee.alreadyemployee.news.controller.dto;

import lombok.*;

@Data @Builder
@AllArgsConstructor
public class NewsSimpleResponseDTO {
    private Long id;
    private String title;
}
