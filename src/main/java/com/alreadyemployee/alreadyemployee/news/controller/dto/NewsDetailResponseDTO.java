package com.alreadyemployee.alreadyemployee.news.controller.dto;

import lombok.*;

@Data @Builder
@AllArgsConstructor
public class NewsDetailResponseDTO {
    private String title;
    private String contents;
}
