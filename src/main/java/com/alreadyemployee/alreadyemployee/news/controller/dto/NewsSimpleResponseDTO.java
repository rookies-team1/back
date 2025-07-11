package com.alreadyemployee.alreadyemployee.news.controller.dto;

import lombok.*;

import java.time.LocalDate;

@Data @Builder
@AllArgsConstructor
public class NewsSimpleResponseDTO {
    private Long id;
    private String title;
    private LocalDate publishDate;
}
