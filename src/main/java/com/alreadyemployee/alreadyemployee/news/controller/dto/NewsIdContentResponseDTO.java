package com.alreadyemployee.alreadyemployee.news.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NewsIdContentResponseDTO {
    private Long id;
    private String contents;
}