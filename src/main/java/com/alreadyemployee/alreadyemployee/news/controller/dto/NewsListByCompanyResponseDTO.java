package com.alreadyemployee.alreadyemployee.news.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsListByCompanyResponseDTO {
    private Long id;
    private String title;
    private LocalDate publishDate;

}
