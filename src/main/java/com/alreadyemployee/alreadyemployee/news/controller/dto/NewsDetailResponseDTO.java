package com.alreadyemployee.alreadyemployee.news.controller.dto;

import lombok.*;

import java.time.LocalDate;

@Data @Builder
@AllArgsConstructor
public class NewsDetailResponseDTO {
    private String title;
    private String contents;
    private LocalDate publishDate;

}
